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
  static transients = ['deployedTime', 'undeployedTime', 'startFormdef']
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

  MtfStartFormDefinition getStartFormdef() {
    MtfStartFormDefinition.findByProcessDefinitionUuid(uuid)
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

  // This comparison is different from comparing paths.
  // Highest version and highest draft number comes first.
  int compareTo(Object obj) {
    def other = (BnProcDef)obj
    return uuid.compareTo(obj.uuid)
  }

}
