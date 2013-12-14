package org.motrice.coordinatrice

import org.activiti.bpmn.model.BusinessRuleTask
import org.activiti.bpmn.model.ManualTask
import org.activiti.bpmn.model.ReceiveTask
import org.activiti.bpmn.model.ScriptTask
import org.activiti.bpmn.model.SendTask
import org.activiti.bpmn.model.ServiceTask
import org.activiti.bpmn.model.Task
import org.activiti.bpmn.model.UserTask
import org.activiti.engine.repository.ProcessDefinition
import org.apache.commons.logging.LogFactory

/**
 * All interaction with the process engine
 */
class ProcessEngineService {
  // We don't manage these transactions
  static transactional = false

  // Injection magic, see resources.groovy
  def processEngine
  def activitiRepositoryService
  private static final log = LogFactory.getLog(this)

  /**
   * Create a process definition from an Activiti entity
   * Does not populate activities
   */
  ProcDef createProcDef(ProcessDefinition entity) {
    if (log.debugEnabled) log.debug "createProcDef << ${entity}"
    def state = ProcDefState.get(entity.suspended?
      ProcDefState.STATE_SUSPENDED_ID : ProcDefState.STATE_ACTIVE_ID)
    def deployment = activitiRepositoryService.createDeploymentQuery().
      deploymentId(entity.deploymentId).singleResult()
    def procDef = new ProcDef(uuid: entity.id, key: entity.key, vno: entity.version,
    name: entity.name, type: entity.category, description: entity.description,
    state: state, deployment: deployment)
    if (log.debugEnabled) log.debug "createProcDef >> ${procDef}"
    return procDef
  }

  /**
   * Create a process definition and populate its activities
   */
  ProcDef createFullProcDef(ProcessDefinition entity) {
    if (log.debugEnabled) log.debug "createFullProcDef << ${entity}"
    def procDef = createProcDef(entity)
    def model = activitiRepositoryService.getBpmnModel(procDef.uuid)
    def processModel = model.processes.find {process ->
      process.name == procDef.name
    }

    processModel.findFlowElementsOfType(Task.class).each {element ->
      def taskType = findTaskType(element)
      if (taskType) {
	def actDef = new ActDef(uuid: element.id, name: element.name,
	type: taskType, documentation: element.documentation)
	procDef.addToActivities(actDef)
      }
    }

    if (log.debugEnabled) log.debug "createFullProcDef >> ${procDef}"
    return procDef
  }

  /**
   * Collect all process definitions.
   * Activity definitions are not collected.
   */
  List allProcessDefinitions() {
    if (log.debugEnabled) log.debug "allProcessDefinitions <<"
    def entityList = activitiRepositoryService.createProcessDefinitionQuery().list()
    def result = entityList.collect {entity ->
      createProcDef(entity)
    }
    if (log.debugEnabled) log.debug "allProcessDefinitions >> ${result.size()}"
    return result
  }

  /**
   * Find the process definition with the given id.
   * Return the process definition or null if not found.
   */
  ProcDef findProcessDefinition(String id) {
    if (log.debugEnabled) log.debug "findProcessDefinition << ${id}"
    assert id
    def entity = activitiRepositoryService.createProcessDefinitionQuery().
    processDefinitionId(id).singleResult()
    def procDef = createFullProcDef(entity)
    if (log.debugEnabled) log.debug "findProcessDefinition >> ${procDef}"
    return procDef
  }

  /**
   * Find an activity definition given its full id.
   */
  ActDef findActivityDefinition(String fullId) {
    if (log.debugEnabled) log.debug "findActivityDefinition << ${fullId}"
    def id = new ActDefId(fullId)
    def procDef = findProcessDefinition(id.procId)
    def actDef = procDef.findActDef(id.actId)
    if (log.debugEnabled) log.debug "findActivityDefinition >> ${actDef}"
    return actDef
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
