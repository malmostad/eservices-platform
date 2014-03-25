package org.motrice.coordinatrice

import org.motrice.coordinatrice.pxd.PxdFormdefVer

class MtfStartFormDefinition implements Comparable {

  String authTypeReq

  Long formHandlerId

  String formConnectionKey

  // If the connection is a form (PxdFormdefVer), store its id here
  Long formdefId

  String procdefId

  String userDataXpath

  static mapping = {
    id column: 'start_form_definition_id'
    version false
    formHandlerId column: 'form_type_id'
    formdefId column: 'form_connection_dbid'
  }
  static transients = ['formdef', 'formHandlerType']
  static constraints = {
    authTypeReq nullable: false, maxSize: 255
    procdefId nullable: true, maxSize: 255
    formConnectionKey nullable: true, maxSize: 255
    formdefId nullable: true, min: 0L
    userDataXpath nullable: true, maxSize: 255
  }

  static final DEFAULT_FORM_HANDLER_TYPE = MtfFormType.ORBEON_TYPE_ID

  static create(Procdef procdef, PxdFormdefVer pfv) {
    new MtfStartFormDefinition(procdefId: procdef.uuid, authTypeReq: 'USERSESSION',
    formHandlerId: DEFAULT_FORM_HANDLER_TYPE, formdefId: pfv.id, formConnectionKey: pfv.path)
  }

  static final START_FORM_Q = 'select s.id, s.formHandlerId, s.formConnectionKey, ' +
    's.formdefId, s.procdefId ' +
    'from MtfStartFormDefinition s where s.procdefId=? order by s.formConnectionKey'

  /**
   * Get start form data for a process version.
   * Return a List of StartFormView.
   * NOTE: Big UNEXPLAINED problems with Hibernate here.
   */
  static List startFormList(String procdefId) {
    MtfStartFormDefinition.executeQuery(START_FORM_Q, [procdefId]).collect {tup ->
      new StartFormView(id: tup[0], formHandlerId: tup[1], formConnectionKey: tup[2],
      formdefId: tup[3], procdefId: tup[4])
    }
  }

  PxdFormdefVer getFormdef() {
    def result = PxdFormdefVer.get(formdefId)
    if (log.debugEnabled) log.debug "getFormdef >> ${result}"
    return result
  }

  MtfFormType getFormHandlerType() {
    MtfFormType.get(formHandlerId)
  }

  String toString() {
    formdef?.path
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    (procdefId + formConnectionKey).hashCode()
  }

  boolean equals(Object obj) {
    def result = false
    if (obj instanceof MtfStartFormDefinition) {
      def other = obj as MtfStartFormDefinition
      result = procdefId == other.procdefId
      if (result) result = (!formConnectionKey && !other.formConnectionKey) ||
      (formConnectionKey == other.formConnectionKey)
    }

    return result
  }

  int compareTo(Object obj) {
    def other = (MtfStartFormDefinition)obj
    def result = procdefId.compareTo(other.procdefId)
    if (result == 0 && formConnectionKey) {
      result = formConnectionKey.compareTo(other.formConnectionKey)
    }

    return result
  }

}
