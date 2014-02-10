package org.motrice.coordinatrice

import org.activiti.bpmn.model.BusinessRuleTask
import org.activiti.bpmn.model.ManualTask
import org.activiti.bpmn.model.ReceiveTask
import org.activiti.bpmn.model.ScriptTask
import org.activiti.bpmn.model.SendTask
import org.activiti.bpmn.model.ServiceTask
import org.activiti.bpmn.model.Task
import org.activiti.bpmn.model.UserTask
import org.activiti.engine.ActivitiException
import org.activiti.engine.repository.Deployment
import org.activiti.engine.repository.DeploymentBuilder
import org.activiti.engine.repository.ProcessDefinition
import org.apache.commons.logging.LogFactory

/**
 * All interaction with the process engine
 */
class ProcessEngineService {
  // We don't manage these transactions
  static transactional = false

  // Injection magic, see resources.groovy
  def activityFormdefService
  def activitiRepositoryService
  def procdefService
  private static final log = LogFactory.getLog(this)

  /**
   * Create a DeploymentBuilder and set its name to prepare for deployment
   * key if not null will be part of the deployment name
   */
  DeploymentBuilder createDeploymentBuilder(String key) {
    if (log.debugEnabled) log.debug "createDeploymentBuilder << ${key}"
    def tstamp = new Date().format('yyyyMMddHHmmss')
    if (key) tstamp = "${key}-${tstamp}"
    def deployment = activitiRepositoryService.createDeployment()
    deployment.name(tstamp)
    if (log.debugEnabled) log.debug "createDeploymentBuilder >> ${deployment}"
    return deployment
  }

  /**
   * Deploy from a resource map obtained from findProcessResource
   * Throws org.activiti.engine.ActivitiException on conflicts in the BPMN
   * xml definition.
   */
  Deployment deployFromResourceMap(Map resourceMap) {
    if (log.debugEnabled) log.debug "deployFromProcdef << ${resourceMap?.procdef}"
    def procdef = resourceMap.procdef
    def db = createDeploymentBuilder(procdef.key)
    if (procdef.category) db.category(procdef.category.toString())
    def is = new ByteArrayInputStream(resourceMap.bytes)
    db.addInputStream(procdef.resourceName, is)
    def deployment = db.deploy()
    if (log.debugEnabled) log.debug "deployFromProcdef >> id: ${deployment?.id}, name: ${deployment?.name}"
    return deployment
  }

  /**
   * Find the diagram of a process definition with the given id.
   * Return a map containing the following entries:
   * bytes (byte[]) the contents of the diagram
   * ctype (String) content type
   * procdef (Procdef) the process definition or null if the process definition
   * was not found
   */
  Map findProcessDiagram(String id) {
    if (log.debugEnabled) log.debug "findProcessDiagram << ${id}"
    assert id
    def map = [procdef: null, ctype: null, bytes: null]
    def entity = activitiRepositoryService.getProcessDefinition(id)
    if (entity) {
      map.procdef = procdefService.createProcdef(entity)
      map.bytes = activitiRepositoryService.getProcessDiagram(id).bytes
      map.ctype = 'image/png'
    }
    if (log.debugEnabled) log.debug "findProcessDiagram >> [${map?.procdef}, ${map?.bytes?.size()}]"
    return map
  }

  /**
   * Find the resource that defines this process definition.
   * Return a map containing the following entries,
   * bytes (byte[]): the contents of the resource,
   * procdef (Procdef): the process definition or null if the process definition
   * was not found
   * ctype (String): content type
   * fname (String): file name adjusted to end with '.bpmn'
   */
  Map findProcessResource(String id, boolean fullProcdef) {
    if (log.debugEnabled) log.debug "findProcessResource << ${id}"
    assert id
    def map = [procdef: null, bytes: null]
    def entity = activitiRepositoryService.getProcessDefinition(id)
    if (entity) {
      map.procdef = fullProcdef? procdefService.createFullProcdef(entity) :
      procdefService.createProcdef(entity)
      map.bytes = activitiRepositoryService.
      getResourceAsStream(entity.deploymentId, entity.resourceName).bytes
      map.ctype = 'text/xml'
      map.fname = adjustBpmnFileName(map.procdef.resourceName)
    }

    if (log.debugEnabled) log.debug "findProcessResource >> [${map?.procdef}, ${map?.bytes?.size()}]"
    return map
  }

  Map findProcessResource(String id) {
    findProcessResource(id, false)
  }

  private String adjustBpmnFileName(String origName) {
    def name = stripExtension(origName, '.xml')
    name = stripExtension(name, '.bpmn20')
    return "${name}.bpmn"
  }

  private String stripExtension(String name, String extension) {
    def result = name
    def idx = name.lastIndexOf(extension)
    if (idx > 0) result = name[0..idx-1]
    return result
  }

  /**
   * Find an activity definition given its full id.
   */
  ActDef findActivityDefinition(ActDefId id) {
    if (log.debugEnabled) log.debug "findActivityDefinition << ${id}"
    def procdef = procdefService.findProcessDefinition(id.procId)
    def actDef = procdef.findActDef(id.actId)
    if (log.debugEnabled) log.debug "findActivityDefinition >> ${actDef?.toDisplay()}"
    return actDef
  }

  /**
   * Duplicate a process definition, creating a new version.
   * The process definition resource (BPMN xml) is re-deployed.
   * If the resource contains more than one process definition, all of them
   * will be duplicated to a new version.
   * id must be the id of the process definition to duplicate
   * Returns the duplicated process definitions (List of Procdef)
   * The duplicated process definitions are in state Edit.
   */
  List createNewProcdefVersionByDuplication(String id) {
    if (log.debugEnabled) log.debug "procdefVersionByDuplication << ${id}"
    def resourceMap = findProcessResource(id, true)
    def procdef = resourceMap.procdef
    if (!resourceMap.procdef) throw new ServiceException("Process definition ${id} not found",
							 'default.not.found.message', [id])
    def procdefList = null
    procdefList = deployAndReconnect(resourceMap)
    if (log.debugEnabled) log.debug "procdefVersionByDuplication >> ${procdefList}"
    return procdefList
  }

  private List deployAndReconnect(Map resourceMap) {
    def deployment = null
    try {
      deployment = deployFromResourceMap(resourceMap)
    } catch (ActivitiException exc) {
      def msg = exc.message
      throw new ServiceException(msg, 'procdef.xml.conflict', [msg])
    }

    def state = CrdProcdefState.get(CrdProcdefState.STATE_EDIT_ID)
    def procdefList = procdefService.findProcessDefinitionsFromDeployment(deployment.id, state)
    if (!procdefList) throw new ServiceException("Duplicated process definition not found",
						 'procdef.deployed.not.found')
    
    // Copy activity form connections from previous version
    procdefList.each {reconnectActivityForms(resourceMap.procdef, it)}
    return procdefList
  }

  /**
   * Make a best effort to connect activities to forms for a new process version.
   * Find the previous version and copy the connections.
   * origProcdef should be the original process definition that was duplicated,
   * procdef should be the newly deployed copy.
   */
  private reconnectActivityForms(Procdef origProcdef, Procdef procdef) {
    def procdefList = procdefService.findProcessDefinitionsFromKey(procdef.key)
    if (log.debugEnabled) log.debug "reconnectActivityForms: ${procdefList.size()}"
    // The two first elements should be the new and previous versions.
    // No guarantee though.
    if (procdefList.size() >= 2) {
      activityFormdefService.connectActivityForms(origProcdef, procdefList[1], procdefList[0])
    }
  }

  /**
   * Update a process definition by uploading a new BPMN model.
   */
  List createNewProcdefVersionByUpdate(Procdef origProc, InputStream is) {
    if (log.debugEnabled) log.debug "procdefVersionByUpdate << ${origProc?.uuid}"
    def resourceMap = [procdef: origProc, bytes: is.bytes, ctype: 'text/xml']
    def procdefList = deployAndReconnect(resourceMap)
    if (log.debugEnabled) log.debug "procdefVersionByUpdate >> ${procdefList}"
    return procdefList
  }

}
