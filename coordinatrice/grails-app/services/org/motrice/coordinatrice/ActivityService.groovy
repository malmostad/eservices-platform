package org.motrice.coordinatrice

import org.motrice.coordinatrice.bonita.BnProcDef
import org.motrice.coordinatrice.pxd.PxdFormdefVer

class ActivityService {

  /**
   * Create a list of forms that may be associated with an activity.
   * The list contains the latest published version of all forms.
   * @param actCnx must be the current activity connection
   * @return a map with the following entries:
   * formList: list of published forms (List of PxdFormdefVer)
   * selectedFormId: id of the form currently select in actCnx (Integer)
   */
  Map activityFormSelection(ActivityConnection actCnx) {
    def formList = PxdFormdefVer.allPublishedForms()
    def selectedFormdefVer = null

    if (actCnx?.formState) {
      selectedFormdefVer = formList.find {it.path == actCnx.path}
    }

    return [formList: formList, selectedFormId: selectedFormdefVer?.id]
  }

  List startFormSelection(BnProcDef process) {
    def selection = []
    def formsInUse = new TreeSet()
    MtfStartFormDefinition.list().each {
      formsInUse.add(it.formPath)
    }
    PxdFormdefVer.allPublishedForms().each {
      if (!formsInUse.contains(it.path)) selection.add(it)
    }

    return selection
  }

  /**
   * Check if a form definition is used as a start form
   */
  Boolean checkStartFormInUse(PxdFormdefVer pfv) {
    MtfStartFormDefinition.findByFormPath(pfv.path)
  }

}
