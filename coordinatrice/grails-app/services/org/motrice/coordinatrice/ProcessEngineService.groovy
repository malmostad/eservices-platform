package org.motrice.coordinatrice

import org.activiti.bpmn.model.BusinessRuleTask
import org.activiti.bpmn.model.ManualTask
import org.activiti.bpmn.model.ReceiveTask
import org.activiti.bpmn.model.ScriptTask
import org.activiti.bpmn.model.SendTask
import org.activiti.bpmn.model.ServiceTask
import org.activiti.bpmn.model.Task
import org.activiti.bpmn.model.UserTask
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
   * bytes (byte[]) the contents of the resource
   * procdef (Procdef) the process definition or null if the process definition
   * was not found
   */
  Map findProcessResource(String id) {
    if (log.debugEnabled) log.debug "findProcessResource << ${id}"
    assert id
    def map = [procdef: null, bytes: null]
    def entity = activitiRepositoryService.getProcessDefinition(id)
    if (entity) {
      map.procdef = procdefService.createProcdef(entity)
      map.bytes = activitiRepositoryService.
      getResourceAsStream(entity.deploymentId, entity.resourceName).bytes
    }
    if (log.debugEnabled) log.debug "findProcessResource >> [${map?.procdef}, ${map?.bytes?.size()}]"
    return map
  }

  /**
   * Find an activity definition given its full id.
   */
  ActDef findActivityDefinition(String fullId) {
    if (log.debugEnabled) log.debug "findActivityDefinition << ${fullId}"
    def id = new ActDefId(fullId)
    def procdef = procdefService.findProcessDefinition(id.procId)
    def actDef = procdef.findActDef(id.actId)
    if (log.debugEnabled) log.debug "findActivityDefinition >> ${actDef}"
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
  List duplicateProcdef(String id) {
    if (log.debugEnabled) log.debug "duplicateProcdef << ${id}"
    def resourceMap = findProcessResource(id)
    def procdef = resourceMap.procdef
    if (!procdef) throw new ServiceException("Process definition ${id} not found",
					     'default.not.found.message', [id])

    def deployment = deployFromResourceMap(resourceMap)
    def state = CrdProcdefState.get(CrdProcdefState.STATE_EDIT_ID)
    def procdefList = procdefService.findProcessDefinitionsFromDeployment(deployment.id, state)
    if (!procdefList) throw new ServiceException("Duplicated process definition not found",
						 'procdef.deployed.not.found')
    
    if (log.debugEnabled) log.debug "duplicateProcdef >> ${procdefList}"
    return procdefList
  }

}
