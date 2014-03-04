package org.motrice.coordinatrice

/**
 * Form types for MtfActivityFormDefinition and MtfStartFormDefinition
 */
class MtfFormType {

  // Descriptive label
  String label

  // Spring bean managing this form type. Not used by Coordinatrice.
  String formHandlerBean

  static mapping = {
    id column: 'formtypeid'
    version false
    formHandlerBean column: 'formhandlerbean'
  }
  static constraints = {
    label nullable: true, maxSize: 255
    formHandlerBean nullable: true, maxSize: 255
  }

  // Pre-defined form types...
  static final ORBEON_TYPE_ID = 1
  static final SIGN_START_FORM_ID = 2
  static final SIGN_TASK_FORM_ID = 3
  static final PAY_TASK_ID = 4
  static final NOTIFY_ACT_ID = 5
  static final NO_FORM_ID = 6

  String toString() {
    "[FormType ${id}: ${label}]"
  }

}
