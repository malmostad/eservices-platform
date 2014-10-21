package org.motrice.tdocbox

/**
 * The outcome of running a drill.
 */
class TdbCase implements Comparable {
  // Creation timestamp
  Date timeStamp

  // Originating suite
  TdbSuite suite

  // Exception message if the case crashed.
  String exception

  // Display url, if any
  String displayUrl

  SortedSet items
  static hasMany = [items: TdbItem]
  static constraints = {
    timeStamp unique: true
    exception nullable: true, maxSize: 400
    displayUrl nullable: true
  }
  static mapping = {
    displayUrl type: 'text'
  }

  static createCase(TdbSuite suite) {
    def now = new Date()
    new TdbCase(timeStamp: now, suite: suite)
  }

  TdbCase addDisplayItem(String itemName, String itemText) {
    displayUrl = itemText
    return addTextItem(itemName, itemText)
  }

  TdbCase addTextItem(String itemName, String itemText, boolean exceptionFlag) {
    def item = TdbItem.createTextItem(itemName, itemText)
    if (exceptionFlag) exception = itemText
    addToItems(item)
    return this
  }

  TdbCase addTextItem(String itemName, String itemText) {
    addTextItem(itemName, itemText, false)
  }

  TdbCase addBinaryItem(String itemName, byte[] itemBytes) {
    def item = TdbItem.createBinaryItem(itemName, itemBytes)
    addToItems(item)
    return this
  }

  /**
   * Get all items as a map.
   */
  Map getMap() {
    def map = [:]
    items.each {item ->
      map[item.name] = item.value
    }
    return map
  }

  String toString() {
    "Case ${id}: ${timeStamp.format('yyyy-MM-dd HH:mm:ss')}"
  }

  //-------------------- Comparable --------------------

  int hashCode() {
    timeStamp.hashCode()
  }

  boolean equals(Object obj) {
    def result = false
    if (obj instanceof TdbCase) {
      def other = (TdbCase)obj
      result = timeStamp == other.timeStamp
    }

    return result
  }

  int compareTo(Object obj) {
    def other = (TdbCase)obj
    -timeStamp.compareTo(other.timeStamp)
  }

}
