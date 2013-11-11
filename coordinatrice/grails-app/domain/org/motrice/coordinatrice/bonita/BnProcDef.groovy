package org.motrice.coordinatrice.bonita

import java.text.SimpleDateFormat
import org.motrice.coordinatrice.MtfStartFormDefinition

class BnProcDef implements Comparable {
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
  static hasMany = [activities: BnActDef]
  static mapping = {
    datasource 'bonita'
    id column: 'dbid_'
    version false
    uuid column: 'proc_uuid_'
    state column: 'state_'
    name column: 'name_'
    label column: 'label_'
    vno column: 'version_'
    type column: 'type_'
    deployedMillis column: 'deployed_date_'
    undeployedMillis column: 'undeployed_date_'
  }
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
   * Return SortedSet of BnActDef
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
    (obj instanceof BnProcDef) && ((BnProcDef)obj).uuid == uuid
  }

  int compareTo(Object obj) {
    def other = (BnProcDef)obj
    return uuid.compareTo(obj.uuid)
  }

}
