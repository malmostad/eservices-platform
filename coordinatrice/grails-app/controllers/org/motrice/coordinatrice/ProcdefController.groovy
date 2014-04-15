package org.motrice.coordinatrice

import org.springframework.dao.DataIntegrityViolationException
import org.motrice.coordinatrice.pxd.PxdFormdefVer
import org.motrice.coordinatrice.MtfStartFormDefinition

/**
 * Process definition controller
 */
class ProcdefController {

  def formService
  def procdefService
  def processEngineService

  static allowedMethods = [update: 'POST', delete: 'POST']

  private static final DELETION_PREFIX = 'deletion@'
  private static final DELETION_PREFIX_LEN = DELETION_PREFIX.length()

  def index() {
    redirect(action: "list", params: params)
  }

  /**
   * Create a process definition from scratch by uploading XML.
   * This is different from the upload in procdef/show where we assume the uploaded
   * process is a new version and try to reconnect activity forms.
   * In this action we do not try to reconnect.
   */
  def create() {
    if (log.debugEnabled) log.debug "CREATE FROM SCRATCH ${params}"
    def categoryList = CrdProcCategory.listOrderByName()
    [categoryList: categoryList, defaultCategory: CrdProcCategory.defaultEntry()]
  }

  /**
   * Copy the process definition and make it a new version.
   */
  def createcopy() {
    if (log.debugEnabled) log.debug "NEW VERSION ${params}"
    def uuid = params.id
    def procdefList = null

    try {
      procdefList = processEngineService.createNewProcdefVersionByDuplication(uuid)
    } catch (ServiceException exc) {
      handleServiceException('newversion', exc)
      redirect(action: "list")
      return
    }

    flash.message = message(code: 'procdef.newversion.count', args: [procdefList.size()])
    render(view: 'listname', model: [procdefInstList: procdefList,
	   procdefInstTotal: procdefList.size(), procdefKey: 'unknown'])
  }

  /**
   * Upload BPMN defining what we assume is a new version of this process definition.
   */
  def createnewversion() {
    if (log.debugEnabled) log.debug "CREATE NEW VERSION ${params}"
    def uuid = params.id
    def procdefInst = procdefService.findProcessDefinition(uuid)
    if (!procdefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'procdef.label', default: 'Procdef'), uuid])
      redirect(action: "list")
      return
    }

    [procdefInst: procdefInst]
  }

  /**
   * List process definition keys (no versions, no full names)
   */
  def list(Integer max) {
    if (log.debugEnabled) log.debug "LIST ${params}"
    params.max = Math.min(max ?: 15, 100)
    params.offset = params.offset as Integer ?: 0
    // Sorting is disabled
    def procdefList = procdefService.allProcessDefinitionsGroupByName()
    def length = procdefList.size()
    def maxIndex = Math.min(params.offset  + params.max, length)
    def procdefView = procdefList.subList(params.offset, maxIndex)
    [procdefList: procdefView, procdefTotal: length]
  }

  /**
   * List process definitions from a deployment
   */
  def listdepl(Integer max) {
    if (log.debugEnabled) log.debug "LISTDEPL ${params}"
    // key is the deployment id
    def key = params.id
    params.max = Math.min(max ?: 15, 100)
    params.offset = params.offset as Integer ?: 0
    def procdefInstList = procdefService.allProcessDefinitionsByDeployment(key)
    def length = procdefInstList.size()
    def maxIndex = Math.min(params.offset + params.max, length)
    def procdefView = procdefInstList.subList(params.offset, maxIndex)
    render(view: 'listname',
    model: [procdefInstList: procdefView, procdefInstTotal: length, procdefKey: key,
	   deploymentId: key, deleteEnabled: "${DELETION_PREFIX}${key}"])
  }

  /**
   * List process definitions having a given key
   * Pass the key as the "id" parameter
   */
  def listname(Integer max) {
    if (log.debugEnabled) log.debug "LISTNAME ${params}"
    // key is the process definition key (id without version etc)
    def key = params.id
    params.max = Math.min(max ?: 15, 100)
    params.offset = params.offset as Integer ?: 0
    def procdefInstList = procdefService.allProcessDefinitionsByKey(key)
    def length = procdefInstList.size()
    def maxIndex = Math.min(params.offset + params.max, length)
    def procdefView = procdefInstList.subList(params.offset, maxIndex)
    [procdefInstList: procdefView, procdefInstTotal: length, procdefKey: key]
  }

  /**
   * List process definitions from a key, show delete checkboxes.
   */
  def listdeletion(Integer max) {
    if (log.debugEnabled) log.debug "LISTDELETION ${params}"
    // key is the process definition key (id without version etc)
    def key = params.id
    params.max = Math.min(max ?: 15, 100)
    params.offset = params.offset as Integer ?: 0
    // TODO: This might not be the exact condition for deletion
    // Other parts of the code use the 'deletable' property.
    def procdefInstList = procdefService.allProcdefsByKeyAndState(key, CrdProcdefState.STATE_EDIT_ID)
    def length = procdefInstList.size()
    def maxIndex = Math.min(params.offset + params.max, length)
    def minIndex = params.offset
    def procdefView = (minIndex > maxIndex)? [] : procdefInstList.subList(params.offset, maxIndex)
    [procdefInstList: procdefView, procdefInstTotal: length, procdefKey: key,
    delprefix: DELETION_PREFIX]
  }

  def show() {
    if (log.debugEnabled) log.debug "SHOW ${params}"
    def uuid = params.id
    def procdefInst = procdefService.findProcessDefinition(uuid)
    if (!procdefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'procdef.label', default: 'Procdef'), uuid])
      redirect(action: "list")
      return
    }

    def state = procdefInst.state
    def startForms = procdefInst.startForms
    if (log.debugEnabled) log.debug "SHOW >> ${procdefInst}, ${startForms}"
    [procdefInst: procdefInst, startForms: startForms, procState: state]
  }

  def diagramDownload() {
    if (log.debugEnabled) log.debug "DIAGRAM ${params}"
    def uuid = params.id
    def diagramMap = processEngineService.findProcessDiagram(uuid)
    if (!diagramMap.procdef) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'procdef.label', default: 'Procdef'), uuid])
      redirect(action: "show", id: uuid)
      return
    }

    def procdef = diagramMap.procdef
    response.contentType = diagramMap.ctype
    response.setHeader('Content-Disposition', "filename=${procdef.diagramResourceName}")
    response.outputStream << diagramMap.bytes
  }

  def xmlDownload() {
    if (log.debugEnabled) log.debug "XML ${params}"
    def uuid = params.id
    def resourceMap = processEngineService.findProcessResource(uuid)
    if (!resourceMap.procdef) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'procdef.label', default: 'Procdef'), uuid])
      redirect(action: "show", id: uuid)
      return
    }

    def procdef = resourceMap.procdef
    response.contentType = resourceMap.ctype
    response.setHeader('Content-Disposition', "filename=${resourceMap.fname}")
    response.outputStream << resourceMap.bytes
  }

  /**
   * Create a new version of a process definition by uploading BPMN.
   * We have no control over what's uploaded.
   * We act as if it is a new version and try to connect activity forms.
   */
  def xmlUpload() {
    if (log.debugEnabled) log.debug "XMLUPLOAD ${params}"
    def uuid = params.id
    def procdefInst = procdefService.findProcessDefinition(uuid)
    if (!procdefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'procdef.label', default: 'Procdef'), uuid])
      redirect(action: "list")
      return
    }

    def procList = []
    def file = request.getFile('bpmnDef')
    if (file.empty) {
      flash.message = message(code: 'procdef.upload.bpmn.empty')
      redirect(action: "show", id: uuid)
      return
    } else {
      try {
	// If you want to save the file there is the transferTo(File) method
	procList = processEngineService.createNewProcdefVersionByUpdate(procdefInst, file.inputStream)
      } catch (ServiceException exc) {
	handleServiceException('xmlUpload', exc)
	redirect(action: "list")
	return
      }
    }

    flash.message = message(code: 'procdef.newversion.count', args: [procList.size()])
    render(view: 'listname', model: [procdefInstList: procList,
	   procdefInstTotal: procList.size(), procdefKey: 'unknown'])
  }

  def xmlUploadFromScratch(UploadBpmnCommand cmd) {
    if (log.debugEnabled) {
      log.debug "UPLOAD FROM SCRATCH ${params}"
      log.debug "cmd: ${cmd}"
    }
    def procList = []
    cmd.file = request.getFile('bpmnDef')
    if (cmd.file.empty) {
      flash.message = message(code: 'procdef.upload.bpmn.empty')
      redirect(action: "list")
      return
    } else {
      try {
	// If you want to save the file there is the transferTo(File) method
	procList = processEngineService.createProcdefFromScratch(cmd)
      } catch (ServiceException exc) {
	handleServiceException('xmlUpload', exc)
	redirect(action: "list")
	return
      }
    }

    def procCount = procList?.size() ?: 0
    flash.message = message(code: 'procdef.newversion.count', args: [procCount])
    render(view: 'listname', model: [procdefInstList: procList,
	   procdefInstTotal: procCount, procdefKey: 'unknown'])
  }

  /**
   * Edit the start form connection (nothing else may be changed)
   */
  def edit() {
    if (log.debugEnabled) log.debug "EDIT ${params}"
    def uuid = params.id
    def procdefInst = procdefService.findProcessDefinition(uuid)
    if (!procdefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'procdef.label', default: 'Procdef'), uuid])
      redirect(action: "list")
      return
    } else if (!procdefInst.state.startFormChangeAllowed) {
      flash.message = message(code: 'procdef.state.not.editable', args: [message(code: 'procdef.label', default: 'Procdef'), uuid])
      redirect(action: "show", id: uuid)
      return
    }

    def selection = null
    try {
      selection = formService.startFormSelection()
    } catch (ServiceException exc) {
      handleServiceException('edit', exc)
      redirect(action: "list")
      return
    }

    def startForms = procdefInst.startForms
    if (log.debugEnabled) log.debug "EDIT >> ${procdefInst}, ${startForms}"
    [procdefInst: procdefInst, startForms: startForms, formList: selection]
  }

  /**
   * Add a form to the start forms
   */
  def update(StartFormSelectionCommand sfsc) {
    if (log.debugEnabled) {
      log.debug "UPDATE ${params}"
      log.debug "SFSC ${sfsc}"
    }

    def procdefInst = procdefService.findProcessDefinition(sfsc.id)
    if (!procdefInst) {
      redirect(action: "edit", id: procdefInst.uuid)
      return
    } else if (!procdefInst.state.startFormChangeAllowed) {
      flash.message = message(code: 'procdef.state.not.editable', args: [message(code: 'procdef.label', default: 'Procdef'), id])
      redirect(action: "list")
      return
    }

    def formId = params.form.id as Integer
    
    if (formId < 0) {
      flash.message = message(code: 'startform.selection.no.form.selected')
      redirect(action: "edit", id: procdefInst.uuid)
      return
    }

    if (log.debugEnabled) log.debug "update.procdefInst: ${procdefInst?.uuid}"

    // We have taken precautions to remove existing start forms from the selection.
    // It is not possible to define a database constraint that would preclude multiple
    // use of a start form. So we have to check.
    def inUse = formService.findAsStartForm(sfsc.form)
    if (inUse) {
      flash.message = message(code: 'startform.selection.form.in.use', args: [sfsc.formConnectionKey])
      redirect(action: "edit", id: procdefInst.uuid)
      return
    }

    def startForm = MtfStartFormDefinition.create(procdefInst, sfsc.form)
    if (log.debugEnabled) log.debug "update.startForm: ${startForm}"

    if (!startForm.save(flush: true)) {
      redirect(action: "edit", id: procdefInst.uuid)
      return
    }

    flash.message = message(code: 'startform.updated.label', args: [startForm])
    redirect(action: "edit", id: procdefInst.uuid)
  }

  def editstate(String id) {
    if (log.debugEnabled) log.debug "EDIT STATE: ${params}"
    def procdefInst = procdefService.findProcessDefinition(id)
    if (!procdefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'procdef.label', default: 'Procdef'), id])
      redirect(action: 'list')
      return
    }

    def stateList = procdefInst.state.nextStates().collect {CrdProcdefState.get(it)}
    [procdefInst: procdefInst, stateList: stateList]
  }

  def updatestate(String id) {
    if (log.debugEnabled) log.debug "UPDATE STATE: ${params}"
    def procdefInst = procdefService.findProcessDefinition(id)
    def updatedState = CrdProcdefState.get(params.state.id)
    if (!(procdefInst && updatedState)) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'procdef.label', default: 'Procdef'), id])
      redirect(action: 'list')
      return
    }

    procdefService.updateProcdefState(procdefInst, updatedState)
    def procdefKey = procdefInst.key
    flash.message = message(code: 'default.updated.message', args: [message(code: 'procdef.label', default: 'Procdef'), id])
    redirect(action: 'listname', id: procdefKey)
  }

  /**
   * Withdraw the process from use, set it in state Retired
   */
  def retire() {
    if (log.debugEnabled) log.debug "RETIRE ${params}"
    def procdefInst = procdefService.findProcessDefinition(params.id)
    if (!procdefInst) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'procdef.label', default: 'Procdef'), id])
      redirect(action: "list")
      return
    } else if (!procdefInst.state.editable) {
      flash.message = message(code: 'procdef.state.not.editable', args: [message(code: 'procdef.label', default: 'Procdef'), id])
      redirect(action: "list")
      return
    }

    def selection = formService.startFormSelection()
    [procdefInst: procdefInst, formList: selection]
  }

  /**
   * Confirm deletion of a number of process definitions
   */
  def deletionconfirm(String id) {
    if (log.debugEnabled) log.debug "DELETIONCONFIRM ${params}"
    // Delete a bunch of deployments: from listdeletion
    def deletionIdList = params.inject([]) {list, entry ->
      if (entry.key.startsWith(DELETION_PREFIX) && entry.value == 'on') {
	list << entry.key.substring(DELETION_PREFIX_LEN)
      }
      return list
    }

    // This case if we come from listdepl and a single deployment
    def deploymentId = null
    if (!deletionIdList && id?.startsWith(DELETION_PREFIX)) {
      deploymentId = id.substring(DELETION_PREFIX_LEN)
    }

    // Get all process definitions from all deployments involved
    def procdefList = null
    try {
      if (deploymentId) {
	procdefList = procdefService.allProcessDefinitionsByDeployment(deploymentId)
      } else {
	procdefList = procdefService.allProcessDefinitionsByDeployment(deletionIdList)
      }
    } catch (ServiceException exc) {
      handleServiceException('deletionconfirm', exc)
      redirect(action: "list")
      return
    }

    if (procdefList.isEmpty()) {
      flash.message = message(code: 'procdef.delete.nothing')
      redirect(action: "list")
      return
    } 

    def deploymentSet = new TreeSet()
    procdefList.each {deploymentSet << it.deploymentId}
    def deploymentIdStr = deploymentSet.join('|').bytes.encodeBase64()
    [procdefInstList: procdefList, deletionKey: deploymentIdStr]
  }

  /**
   * Delete a number of deployments.
   * The parameter 'key' contains a Base64-encoded list of deployment ids
   * separated by vertical bar.
   */
  def deleteall() {
    if (log.debugEnabled) log.debug "DELETEALL ${params}"
    def deploymentIdStr = new String(params?.key?.decodeBase64())
    def deploymentIdList = deploymentIdStr?.split('\\|')?.toList()
    def deleteCount = 0
    try {
      deleteCount = procdefService.deleteDeployments(deploymentIdList)
    } catch (ServiceException exc) {
      handleServiceException('deletionconfirm', exc)
    }

    flash.message = message(code: 'procdef.deployments.deleted.message', args: [deleteCount])
    redirect(action: "list")
  }

  /**
   * Delete a number of process versions.
   */
  def delete() {
    if (log.debugEnabled) log.debug "DELETE ${params}"
    def deletionIdList = params.inject([]) {list, entry ->
      if (entry.key.startsWith(DELETION_PREFIX) && entry.value == 'on') {
	list << entry.key.substring(DELETION_PREFIX_LEN)
      }

      return list
    }

    // Get and check the process definitions
    def procdefList = deletionIdList.inject([]) {list, id ->
      def procdef = procdefService.findProcessDefinition(id)
      if (procdef.deletable) list << procdef
      return list
    }

    if (procdefList.isEmpty()) {
      flash.message = message(code: 'procdef.delete.nothing')
    } else {
      def deleteCount = procdefList.inject(0) {count, procdef ->
	procdefService.deleteProcessDefinition(procdef)
	return count + 1
      }
      flash.message = message(code: 'procdef.deleted.count', args: [deleteCount])
    }

    def selection = formService.startFormSelection()
    [procdefInst: procdefInst, formList: selection]
  }

  private handleServiceException(String op, ServiceException exc) {
    log.error "${op} ${exc?.message}"
    if (exc.key) {
      flash.message = message(code: exc.key, args: exc.args ?: [])
    } else {
      flash.message = exc.message
    }
  }

}

class StartFormSelectionCommand {
  String id
  PxdFormdefVer form

  String toString() {
    "[StartFormCmd(process ${id}): ${form}]"
  }
}

class UploadBpmnCommand {
  String deploymentName
  CrdProcCategory crdProcCategory
  // org.springframework.web.multipart.commons.CommonsMultipartFile
  def file

  String getFileName() {
    file.originalFilename
  }

  InputStream getInputStream() {
    file.inputStream
  }

  String toString() {
    "[UploadBpmnCmd: ${deploymentName}, ${crdProcCategory}, ${file}]"
  }
}
