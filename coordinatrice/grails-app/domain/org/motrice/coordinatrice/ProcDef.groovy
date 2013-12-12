package org.motrice.coordinatrice

import java.text.SimpleDateFormat
import org.motrice.coordinatrice.MtfStartFormDefinition

class ProcDef implements Comparable {
  def grailsApplication

  String uuid

  String state

  String name

  String label

  String vno

  String type

  Long deployedMillis

  Long undeployedMillis

  SortedSet activities
  SortedSet startForms

  // Not a database object, never to be persisted
  static mapWith = 'none'
  static hasMany = [activities: ActDef]
  static transients = ['deployedTime', 'undeployedTime', 'humanActivities', 'startForms']
  static constraints = {
    uuid nullable: true, maxSize: 255, unique: true
    state nullable: true, maxSize: 255
    name nullable: true, maxSize: 255
    label nullable: true, maxSize: 255
    vno nullable: true, maxSize: 255
    type nullable: true, maxSize: 255
  }

  // Formatted deploy time
  String getDeployedTime() {
    def fmt = new SimpleDateFormat(grailsApplication.config.coordinatrice.tstamp.coarse.fmt)
    return fmt.format(new java.util.Date(deployedMillis))
  }

  // Formatted undeploy time
  Date getUndeployedTime() {
    def fmt = new SimpleDateFormat(grailsApplication.config.coordinatrice.tstamp.coarse.fmt)
    return fmt.format(new java.util.Date(undeployedMillis))
  }

  /**
   * Get all activities of type Human
   * Return SortedSet of ActDef
   */
  SortedSet getHumanActivities() {
    def actSet = new TreeSet()
    activities.each {activity ->
      if (activity.type == 'Human') actSet.add(activity)
    }

    return actSet
  }

  /**
   * Get all start forms.
   * Return SortedSet of MtfStartFormDefinition.
   */
  SortedSet getStartForms() {
    def formList = MtfStartFormDefinition.findAllByProcessDefinitionUuid(uuid)
    return new TreeSet(formList)
  }

  String toString() {
    label
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    uuid.hashCode()
  }

  boolean equals(Object obj) {
    (obj instanceof ProcDef) && ((ProcDef)obj).uuid == uuid
  }

  int compareTo(Object obj) {
    def other = (ProcDef)obj
    return uuid.compareTo(obj.uuid)
  }

}
