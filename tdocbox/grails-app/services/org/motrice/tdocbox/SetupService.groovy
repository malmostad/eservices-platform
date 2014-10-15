package org.motrice.tdocbox

/**
 * Service for database initialization
 */
class SetupService {
  // This service only runs native SQL
  static transactional = false
  // From config/spring/resources.groovy
  def groovySql

  /**
   * The way the database is configured, the Hibernate sequence is
   * reset every time DocBox starts.
   */
  def initialize() {
    if (log.debugEnabled) log.debug "initialize <<"
    def tup = groovySql.firstRow('select max(id) as max from tdb_item')
    def itemMax = tup.MAX as Long
    tup = groovySql.firstRow('select last_value as last from hibernate_sequence')
    def seqLast = tup.LAST as Long
    if (seqLast < itemMax) {
      def q = 'select setval(\'hibernate_sequence\', ?)'
      tup = groovySql.firstRow(q, [itemMax + 10L])
      seqLast = tup.SETVAL as Long
      if (log.debugEnabled) log.debug "initialize >> sequence restart ${seqLast}"
    } else {
      if (log.debugEnabled) log.debug "initialize >> (${seqLast} >= ${itemMax})"
    }
  }

}
