package org.motrice.coordinatrice

/**
 * An internationalization label for a BPMN process activity.
 * Given a process definition key, an activity name and a locale you may find
 * a label for that locale.
 */
class CrdI18nActLabel {

  // Process definition key (id minus version, spaces and odd characters)
  String procdefKey

  // Process version number.
  // Makes it possible to have more than one label for a given activity.
  // The version number indicates the first version for which a new definition
  // should be used.
  // For instance, if procdefVer is 4 it means that the definition is valid for
  // process definitions version 4 and higher.
  // The first process version is 1 but the default value here is 0.
  // Just a convention for knowing if it has been set, no change in logic.
  Integer procdefVer

  // Activity definition name
  // NOTE: At this point we are not sure if we may trust the activity id
  String actdefName

  // Activity definition id
  // (See above note)
  String actdefId

  // Locale string
  String locale

  // The internationalized label.
  // It is nullable because we want to be able to generate a full set of empty labels
  // for a new locale.
  String label

  static mapping = {
    table 'crd_i18n_act_label'
    // This has no effect in Grails 2.2.4
    procdefVer defaultValue: 0
  }
  // A lot of indexes are justified because this is a read-mostly structure.
  // Simplify read at the expense of inserts and updates.
  static constraints = {
    procdefKey maxSize: 255
    procdefVer min: 0, unique: ['procdefKey', 'actdefName', 'locale']
    actdefName maxSize: 255, unique: ['procdefKey', 'procdefVer', 'locale']
    actdefId maxSize: 255, unique: ['procdefKey', 'procdefVer', 'locale']
    locale maxSize: 24, unique: ['procdefKey', 'procdefVer', 'actdefName']
    label maxSize: 255, nullable: true, unique: ['procdefKey', 'procdefVer', 'actdefName', 'locale']
  }

}
