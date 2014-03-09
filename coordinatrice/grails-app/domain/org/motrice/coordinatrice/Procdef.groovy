package org.motrice.coordinatrice

import java.text.SimpleDateFormat
import org.motrice.coordinatrice.MtfStartFormDefinition
import org.activiti.engine.repository.Deployment

/**
 * A process definition.
 * This class is NOT PERSISTED, constructed read-only from the BPMN engine.
 * A subset is persisted as CrdProcdef
 */
class Procdef implements Comparable {
  def grailsApplication

  // This is not literally a uuid, but a string that uniquely identifies this
  // process definition
  // The name "id" is avoided because it has a special default meaning in Grails
  String uuid

  // Identifier common to all versions of this process definition
  // Almost as the name, but special characters removed
  String key

  // Version of this process definition
  // In Activiti the first version is 1
  // The name "version" is avoided because it has a special default meaning in
  // Grails
  Integer vno

  // State of this process definition
  // Valid values are Active and Suspended
  CrdProcdefState state

  // The name of this process definition as used in human communication
  String name

  // The "category" attribute in Activiti process definitions
  CrdProcCategory category

  // Text describing this process definition
  String description

  // Resource name: name of XML process definition (in table act_ge_bytearray).
  // Use RepositoryService.getResourceAsStream(deploymentId, resourceName)
  // to access
  String resourceName

  // Diagram resource name: name of process definition image.
  // Usage same as resourceName, but may be null.
  String diagramResourceName

  // Deployment, a concept taken directly from Activiti
  // A unit containing this process definition and possibly others
  Deployment deployment

  SortedSet activities
  SortedSet startForms

  // Not a database object, never to be persisted
  static mapWith = 'none'
  static belongsTo = [persisted: CrdProcdef]
  static hasMany = [activities: ActDef]
  static transients = ['deletable', 'deployedTime', 'deployedTimeStr', 'deploymentId',
		       'display', 'userActivities', 'startForms']
  static constraints = {
    uuid maxSize: 64
    key nullable: false, maxSize: 255
    vno min: 1
    state nullable: true
    name nullable: true, maxSize: 255
    category nullable: true
    resourceName nullable: true
    diagramResourceName nullable: true
    deployment nullable: true
  }

  /**
   * Find an activity with a given uuid
   */
  ActDef findActDef(String uuid) {
    activities.find {activity ->
      activity.uuid == uuid
    }
  }

  boolean isDeletable() {
    state.id == CrdProcdefState.STATE_EDIT_ID
  }

  /**
   * Get deployed time, if possible
   */
  Date getDeployedTime() {
    deployment?.deploymentTime
  }

  /**
   * Get formatted deployed time, if possible
   */
  String getDeployedTimeStr() {
    deployment?.deploymentTime?.format(grailsApplication.config.coordinatrice.tstamp.coarse.fmt)
  }

  /**
   * Get the deployment id, if available
   */
  String getDeploymentId() {
    deployment?.id
  }

  /**
   * Get the display string for this process definition
   */
  String getDisplay() {
    toString()
  }

  /**
   * Get all activities of type Human
   * Return SortedSet of ActDef
   */
  SortedSet getUserActivities() {
    def actSet = new TreeSet()
    activities.each {activity ->
      if (activity.type.id == TaskType.TYPE_USER_ID) actSet.add(activity)
    }

    return actSet
  }

  /**
   * Get all start forms.
   * Return SortedSet of a MtfStartFormDefinition view.
   * Big UNEXPLAINED problems with Hibernate here, this is not normal.
   */
  List getStartForms() {
    MtfStartFormDefinition.startFormList(uuid)
  }

  String toDump() {
    "[Procdef ${name}-v${vno}: depl ${deployment?.id}, act's: ${activities?.size()}]"
  }

  String toString() {
    "${name} [v${vno}]"
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    uuid.hashCode()
  }

  boolean equals(Object obj) {
    (obj instanceof Procdef) && ((Procdef)obj).uuid == uuid
  }

  int compareTo(Object obj) {
    def other = (Procdef)obj
    return uuid.compareTo(obj.uuid)
  }

}
