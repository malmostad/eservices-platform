package org.motrice.coordinatrice

import org.activiti.bpmn.model.BusinessRuleTask
import org.activiti.bpmn.model.ManualTask
import org.activiti.bpmn.model.ReceiveTask
import org.activiti.bpmn.model.ScriptTask
import org.activiti.bpmn.model.SendTask
import org.activiti.bpmn.model.ServiceTask
import org.activiti.bpmn.model.Task
import org.activiti.bpmn.model.UserTask
import org.activiti.engine.ActivitiObjectNotFoundException
import org.activiti.engine.repository.ProcessDefinition
import org.apache.commons.logging.LogFactory

/**
 * Service for maintaining consistency between Coordinatrice and Activiti
 * with respect to process definitions.
 * Coordinatrice publishes a table crd_procdef (and corresponding REST services).
 * Coordinatrice uses the non-persisted class Procdef for its internal logic.
 * Those are the components to keep in synch with each other.
 * ProcessEngineService is not transactional, so all operations involving
 * CrdProcdef must be performed here.
 */
class ProcdefService {
  // Transaction support mainly for CrdProcdef
  static transactional = true
  // Injection magic, see resources.groovy
  def activitiRepositoryService
  // Datasource injected to allow direct SQL query
  javax.sql.DataSource dataSource
  private static final log = LogFactory.getLog(this)

  // Native SQL to query the Activiti process definition table.
  static final PROCDEF_BY_NAME_Q = 'select name_ as name, key_ as key, ' +
    'count(version_) as versions ' +
    'from act_re_procdef group by name_, key_ order by name_'

  /**
   * Get a list of all process definition names and the number of versions
   * of each.
   * A bit of magic for reading an Activiti table directly.
   * The datasource is injected above.
   * The CrdProcdef table is not involved.
   * Return a list of maps. Each map contains the entries,
   * name: process definition name (String),
   * key: process definition key (String),
   * versions: number of versions (Integer).
   * NOTE: If the name is empty, key is used.
   */
  List allProcessDefinitionsGroupByName() {
    def db = new groovy.sql.Sql(dataSource)
    def result = []
    db.eachRow(PROCDEF_BY_NAME_Q) {tuple ->
      def item = [name: tuple.NAME as String, key: tuple.KEY as String,
      versions: tuple.VERSIONS as Integer]
      if (!tuple.name) item.name = item.key
      result << item
    }

    return result
  }

  /**
   * Get all process definitions from Activiti.
   * Return a list of process definitions (List of Procdef)
   * SIDE EFFECT: Updates crd_procdef
   */
  List allProcessDefinitions() {
    if (log.debugEnabled) log.debug "allProcessDefinitions <<"
    def entityList = activitiRepositoryService.createProcessDefinitionQuery().list()
    def result = entityList.collect {entity ->
      createProcdef(entity)
    }
    if (log.debugEnabled) log.debug "allProcessDefinitions >> ${result.size()}"
    return result
  }

  /**
   * Get all process definitions from Activiti.
   * deploymentId must be a deployment id.
   * Return a list of process definitions (List of Procdef)
   * SIDE EFFECT: Updates crd_procdef
   */
  List allProcessDefinitionsByDeployment(String deploymentId) {
    if (log.debugEnabled) log.debug "allProcessDefinitionsByDeployment << ${deploymentId}"
    def entityList = activitiRepositoryService.createProcessDefinitionQuery().
    deploymentId(deploymentId).orderByProcessDefinitionName().asc().list()
    def result = entityList.collect {entity ->
      createProcdef(entity)
    }
    if (log.debugEnabled) log.debug "allProcessDefinitionsByDeployment >> ${result.size()}"
    return result
  }

  /**
   * Beginning from a list of process definition ids, find all process
   * definitions from the same deployments.
   * Return a list containing all these process definitions sorted in
   * backward deployment date order (latest first), List of Procdef.
   */
  List allProcessDefinitionsByDeployment(List procdefList) {
    if (log.debugEnabled) log.debug "allProcessDefinitionsByDeployment << ${procdefList?.size()}"
    def procSet = new TreeSet(new ProcdefByDateAndIdComparator())
    procdefList.each {procdefId ->
      def procdef = findProcessDefinition(procdefId)
      if (!procdef) {
	throw new ServiceException("Proc def ${procdefId} not found",
				   'procdef.not.found', [procdefId])
      }
      // Process definitions in this deployment
      def deplList = findProcessDefinitionsFromDeployment(procdef.deploymentId)
      deplList.each {deplProc ->
	if (!deplProc.deletable) {
	  throw new ServiceException("Proc def ${deplProc} is not deletable",
				     'procdef.deletion.not.deletable', [deplProc.toString()])
	}

	procSet.add(deplProc)
      }
    }

    def result = new ArrayList(procSet)
    if (log.debugEnabled) log.debug "allProcessDefinitionsByDeployment >> ${result?.size()}"
    return result
  }

  /**
   * Get all process definitions from Activiti.
   * Return a list of process definitions (List of Procdef)
   * SIDE EFFECT: Updates crd_procdef
   */
  List allProcessDefinitionsByKey(String key) {
    if (log.debugEnabled) log.debug "allProcessDefinitionsByKey << ${key}"
    def entityList = activitiRepositoryService.createProcessDefinitionQuery().
    processDefinitionKey(key).orderByProcessDefinitionVersion().desc().list()
    def result = entityList.collect {entity ->
      createProcdef(entity)
    }
    if (log.debugEnabled) log.debug "allProcessDefinitionsByKey >> ${result.size()}"
    return result
  }

  /**
   * Get all process definitions from Activiti.
   * Return a list of process definitions (List of Procdef)
   * SIDE EFFECT: Updates crd_procdef
   */
  List allProcdefsByKeyAndState(String key, Long stateId) {
    if (log.debugEnabled) log.debug "allProcdefsByKeyAndState << ${key}, ${stateId}"
    def entityList = activitiRepositoryService.createProcessDefinitionQuery().
    processDefinitionKey(key).orderByProcessDefinitionVersion().desc().list()

    def result = entityList.inject([]) {list, entity ->
      def pd = createProcdef(entity)
      return (pd.state.id == stateId)? list + pd : list
    }

    if (log.debugEnabled) log.debug "allProcdefsByKeyAndState >> ${result.size()}"
    return result
  }

  /**
   * Create a process definition domain object from an Activiti entity
   * Does not populate activities
   */
  Procdef createProcdef(ProcessDefinition entity, CrdProcdefState initState) {
    if (log.debugEnabled) log.debug "createProcdef << ${entity}"

    // Find or create persisted record
    def persisted = CrdProcdef.findByActid(entity.id)
    if (persisted) {
      if (initState && persisted.state.id != initState.id) {
	persisted.state = initState
	if (!persisted.save()) log.error "CrdProcdef update: ${persisted.errors.allErrors.join(',')}"
      }

      propagateStateToActiviti(persisted, entity)
    } else {
      // No persistent record
      def state = null
      if (initState) {
	state = initState
      } else {
	state = CrdProcdefState.get(entity.suspended?
	CrdProcdefState.STATE_SUSPENDED_ID : CrdProcdefState.STATE_ACTIVE_ID)
      }
      persisted = new CrdProcdef(actid: entity.id, actver: entity.version,
      actdepl: entity.deploymentId, state: state)
      if (!persisted.save()) log.error "CrdProcdef insert: ${persisted.errors.allErrors.join(',')}"
    }

    def category = findOrCreateCategory(entity)
    def deployment = activitiRepositoryService.createDeploymentQuery().
    deploymentId(entity.deploymentId).singleResult()

    def procdef = new Procdef(uuid: entity.id, key: entity.key, vno: entity.version,
    name: entity.name, category: category, description: entity.description,
    state: persisted.state, resourceName: entity.resourceName,
    diagramResourceName: entity.diagramResourceName, deployment: deployment)
    if (log.debugEnabled) log.debug "createProcdef >> ${procdef}"
    return procdef
  }

  Procdef createProcdef(ProcessDefinition entity) {
    createProcdef(entity, null)
  }

  /**
   * Make sure the process definition state in Activiti matches CrdProcdef.
   */
  private propagateStateToActiviti(CrdProcdef motProcdef, ProcessDefinition actProcdef) {
    def actState = motProcdef.state.activitiState
    if (actState == CrdProcdefState.STATE_ACTIVE_ID && actProcdef.suspended) {
      activitiRepositoryService.activateProcessDefinitionById(actProcdef.id)
    } else if (actState == CrdProcdefState.STATE_SUSPENDED_ID && !actProcdef.suspended) {
      activitiRepositoryService.suspendProcessDefinitionById(actProcdef.id)
    }
  }

  /**
   * Create a process definition domain object and populate its activities
   */
  Procdef createFullProcdef(ProcessDefinition entity) {
    if (log.debugEnabled) log.debug "createFullProcdef << ${entity}"
    def procdef = createProcdef(entity)
    def model = activitiRepositoryService.getBpmnModel(procdef.uuid)
    def processModel = model.processes.find {process ->
      process.name == procdef.name
    }

    processModel.findFlowElementsOfType(Task.class).each {element ->
      def taskType = findTaskType(element)
      if (taskType) {
	def actDef = new ActDef(uuid: element.id, name: element.name,
	type: taskType, documentation: element.documentation)
	procdef.addToActivities(actDef)
      }
    }

    if (log.debugEnabled) log.debug "createFullProcdef >> ${procdef?.toDump()}"
    return procdef
  }

  /**
   * Delete deployments.
   * deploymentIdList must be a list of deployment ids.
   * Return the number of deployments deleted.
   */
  Integer deleteDeployments(List deploymentIdList) {
    if (log.debugEnabled) log.debug "deleteDeployments << ${deploymentIdList}"
    // Check that all process definitions are deletable
    // deployments is a List of Map
    def deployments = deploymentIdList.collect {checkDeploymentDeletable(it)}
    def deleteCount =  deployments.inject(0) {count, deploymentMap ->
      doDeleteDeployment(deploymentMap)
    }
    if (log.debugEnabled) log.debug "deleteDeployments >> ${deleteCount}"
    return deleteCount
  }

  /**
   * Check if a deployment is deletable.
   * Throw a ServiceException otherwise (causing transaction rollback).
   * Return a map with the following entries:
   * depl: deployment id
   * procdefs: process definitions (List of Procdef)
   */
  private Map checkDeploymentDeletable(String id) {
    def procdefList = findProcessDefinitionsFromDeployment(id)
    procdefList.each {procdef ->
      if (!procdef.deletable) {
	throw new ServiceException("Proc def ${procdef} is not deletable",
				   'procdef.deletion.not.deletable', [procdef.toString()])
      }
    }

    return [depl: id, procdefs: procdefList]
  }

  /**
   * Delete a deployment.
   * deploymentMap must be a map as returned by checkDeploymentDeletable.
   * Return 1 if successful, throw ServiceException otherwise.
   */
  private int doDeleteDeployment(Map deploymentMap) {
    // Delete activity form connections
    deploymentMap.procdefs.each {doDeleteFormConnections(it)}
    // Delete from the cache
    def cacheList = CrdProcdef.findAllByActdepl(deploymentMap.depl)
    cacheList.each {it.delete()}
    // Delete from Activiti
    activitiRepositoryService.deleteDeployment(deploymentMap.depl)
    return 1
  }

  /**
   * Delete form connections for a given process definition
   */
  private doDeleteFormConnections(Procdef procdef) {
    def connections = MtfActivityFormDefinition.findAllByProcdefId(procdef.uuid)
    connections.each {it.delete()}
    connections = MtfStartFormDefinition.findAllByProcdefId(procdef.uuid)
    connections.each {it.delete()}
  }

  /**
   * Find the process definition with the given id.
   * Populate with activities.
   * Return the process definition or null if not found.
   */
  Procdef findProcessDefinition(String id) {
    if (log.debugEnabled) log.debug "findProcessDefinition << ${id}"
    assert id
    def procdef = null

    try {
      def entity = activitiRepositoryService.getProcessDefinition(id)
      procdef = createFullProcdef(entity)
    } catch (ActivitiObjectNotFoundException exc) {
      // Ignore and return null
    }

    if (log.debugEnabled) log.debug "findProcessDefinition >> ${procdef}"
    return procdef
  }

  /**
   * Find the process definition with the given id.
   * Do not populate with activities.
   * Return the process definition or null if not found.
   */
  Procdef findShallowProcdef(String id) {
    if (log.debugEnabled) log.debug "findShallowProcdef << ${id}"
    assert id
    def procdef = null

    try {
      def entity = activitiRepositoryService.getProcessDefinition(id)
      procdef = createProcdef(entity)
    } catch (ActivitiObjectNotFoundException exc) {
      // Ignore and return null
    }

    if (log.debugEnabled) log.debug "findShallowProcdef >> ${procdef}"
    return procdef
  }

  /**
   * Find all process definitions from a given deployment id
   * Activity definitions are not populated.
   * initState may be a process state for initializing the process definition,
   * or null.
   */
  List findProcessDefinitionsFromDeployment(String id, CrdProcdefState initState) {
    if (log.debugEnabled) log.debug "findProcdefFromDeployment << ${id}"
    def entityList = activitiRepositoryService.createProcessDefinitionQuery().
    deploymentId(id).list()
    def result = entityList.collect {entity ->
      createProcdef(entity, initState)
    }
    if (log.debugEnabled) log.debug "findProcdefFromDeployment >> ${result?.size()}"
    return result
  }

  List findProcessDefinitionsFromDeployment(String id) {
    findProcessDefinitionsFromDeployment(id, null)
  }

  /**
   * Find all process definitions from a process key (the version-independent
   * part of the id) and populate their activities.
   * Returns a list of full process definitions ordered by descending version.
   */
  List findProcessDefinitionsFromKey(String key) {
    if (log.debugEnabled) log.debug "findProcdefFromKey << ${key}"
    def entityList = activitiRepositoryService.createProcessDefinitionQuery().
    processDefinitionKey(key).orderByProcessDefinitionVersion().desc().list()
    def result = entityList.collect {entity ->
      createFullProcdef(entity)
    }
    if (log.debugEnabled) log.debug "findProcdefFromKey >> ${result?.size()}"
    return result
  }

  /**
   * Make sure the category assigned to a process definition exists
   * Return CrdProcCategory
   */
  CrdProcCategory findOrCreateCategory(ProcessDefinition entity) {
    def category = CrdProcCategory.findByName(entity.category)
    if (!category) {
      category = new CrdProcCategory(name: entity.category)
      if (log.debugEnabled) log.debug "findOrCreateCategory NEW: ${category}"
      if (!category.save()) log.error "CrdProcCategory save: ${category.errors.allErrors.join(',')}"
    }

    return category
  }

  TaskType findTaskType(obj) {
    def id = 0
    switch (obj) {
    case BusinessRuleTask:
    id = TaskType.TYPE_BUSINESS_RULE_ID
    break
    case ManualTask:
    id = TaskType.TYPE_MANUAL_ID
    break
    case ReceiveTask:
    id = TaskType.TYPE_RECEIVE_ID
    break
    case ScriptTask:
    id = TaskType.TYPE_SCRIPT_ID
    break
    case SendTask:
    id = TaskType.TYPE_SEND_ID
    break
    case ServiceTask:
    id = TaskType.TYPE_SERVICE_ID
    break
    case UserTask:
    id = TaskType.TYPE_USER_ID
    break
    }

    return TaskType.get(id) ?: null
  }

  /**
   * Update process definition state
   */
  def updateProcdefState(Procdef procdef, CrdProcdefState updatedState) {
    if (log.debugEnabled) log.debug "updateProcdefState << ${procdef}, ${updatedState}"
    def motProcdef = CrdProcdef.get(procdef.uuid)
    motProcdef.state = updatedState
    if (!motProcdef.save()) log.error "CrdProcdef update: ${motProcdef.errors.allErrors.join(',')}"
    def actState = motProcdef.state.activitiState
    // Get the Activiti state
    def actProcdef = activitiRepositoryService.createProcessDefinitionQuery().
    processDefinitionId(procdef.uuid).singleResult()
    if (log.debugEnabled) log.debug "actProcdef ${actProcdef}"
    if (actState == CrdProcdefState.STATE_ACTIVE_ID && actProcdef.suspended) {
      activitiRepositoryService.activateProcessDefinitionById(actProcdef.id)
    } else if (actState == CrdProcdefState.STATE_SUSPENDED_ID && !actProcdef.suspended) {
      activitiRepositoryService.suspendProcessDefinitionById(actProcdef.id)
    }

    if (log.debugEnabled) log.debug "updateProcdefState >> ${motProcdef}"
  }
  
}
