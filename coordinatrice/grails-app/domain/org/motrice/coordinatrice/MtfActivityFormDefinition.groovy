package org.motrice.coordinatrice

class MtfActivityFormDefinition {

  String procdefId

  String actdefId

  MtfFormType formHandlerType

  String formConnectionKey

  // If the connection involves an activity, store the activity name here
  String formConnectionLabel

  // If the connection is a form (PxdFormdefVer), store its id here
  Long formdefId

  static mapping = {
    id column: 'activity_form_definition_id'
    version false
    procdefId column: 'procdef_id'
    actdefId column: 'actdef_id'
    formHandlerType column: 'form_type_id'
    formConnectionKey column: 'form_connection_key'
    formdefId column: 'form_connection_dbid'
    formConnectionLabel column: 'form_connection_label'
  }
  static constraints = {
    procdefId nullable: true, maxSize: 255
    actdefId nullable: true, maxSize: 255
    formConnectionKey nullable: true, maxSize: 511
    formdefId nullable: true, min: 0L
    formConnectionLabel nullable: true
  }

  static MtfActivityFormDefinition createFromActDef(ActDef actDefInst) {
    def afd = new MtfActivityFormDefinition()
    afd.procdefId = actDefInst.process.uuid
    afd.actdefId = actDefInst.uuid
    return afd
  }

  def assign(TaskFormSpec taskFormSpec) {
    formHandlerType = MtfFormType.get(taskFormSpec.state)
    formConnectionKey = taskFormSpec.connectionKey
    def formdef = taskFormSpec.findFormdef()
    if (formdef) formdefId = formdef.id
    formConnectionLabel = taskFormSpec.connectionLabel
  }

  /**
   * Copy connection data from another activity form definition.
   */
  def copyConnection(MtfActivityFormDefinition other) {
    formHandlerType = other.formHandlerType
    formConnectionKey = other.formConnectionKey
    formdefId = other.formdefId
    formConnectionLabel = other.formConnectionLabel
  }

  String toString() {
    "[ActFormDef(${id}): ${procdefId}/${actdefId} cnx=${formConnectionLabel}]"
  }
}
