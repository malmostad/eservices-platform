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
  private static final log = LogFactory.getLog(this)

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
      persisted = new CrdProcdef(actid: entity.id, rev: entity.version, state: state)
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

    if (log.debugEnabled) log.debug "createFullProcdef >> ${procdef}"
    return procdef
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
   * Find all process definitions from a given deployment id
   * Activity definitions are not populated.
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
  
}
