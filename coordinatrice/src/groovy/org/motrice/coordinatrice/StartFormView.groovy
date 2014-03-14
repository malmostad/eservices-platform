package org.motrice.coordinatrice

import org.motrice.coordinatrice.pxd.PxdFormdefVer

/**
 * This helper class is introduced because of Hibernate problems.
 * It just cannot manage MtfStartFormDefinition.
 * This is a workaround.
 */
class StartFormView {
  // Id of this start form definition
  Long id

  // Id of a MtfFormType
  Long formHandlerId

  // A form definition path
  String formConnectionKey

  // If there is a form definition path this is the id
  Long formdefId

  // BPMN process definition id
  String procdefId

  String toString() {
    "[StartFormView(${id}): type=${formHandlerId}, form=(${formConnectionKey}|${formdefId}, process=${procdefId}]"
  }
}
