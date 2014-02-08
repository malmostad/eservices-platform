package org.motrice.coordinatrice

/**
 * Comparator for sorting process definitions first by descending
 * deployment date, then by ascending id.
 */
class ProcdefByDateAndIdComparator implements Comparator {
  /**
   * Construct
   */
  ProcdefByDateAndIdComparator() {
  }

  int compare(o1, o2) {
    def obj1 = o1 as Procdef
    def obj2 = o2 as Procdef
    def result = obj2.deployedTime.compareTo(obj1.deployedTime)
    if (result == 0) result = obj1.uuid.compareTo(obj2.uuid)
    return result
  }

  boolean equals(Object obj) {
    obj instanceof ProcdefByDateAndIdComparator
  }
  
}

