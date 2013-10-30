package org.motrice.coordinatrice

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
}
