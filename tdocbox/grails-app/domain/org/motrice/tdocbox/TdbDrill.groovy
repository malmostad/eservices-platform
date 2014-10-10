package org.motrice.tdocbox

class TdbDrill {
  // Name of this drill
  String name

  // HTTP verb
  TdbHttpVerb verb

  // The method to invoke
  TdbMethod method

  TreeSet parameters
  static hasMany = [parameters: TdbParameter]
  static constraints = {
    name nullable: false, maxSize: 40, unique: true
    verb nullable: false
    method nullable: false
  }

  Map getMap() {
    def map = [:]
    parameters.each {param ->
      map[param.name] = param.value
    }
    return map
  }

  String toString() {
    "[Drill ${name}: ${map}]"
  }

}
