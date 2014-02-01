package org.motrice.coordinatrice

/**
 * The "category" attribute in Activiti process definitions.
 * Coordinatrice does not assume any meaning in category names.
 */
class CrdProcCategory {
  // Category name
  String name

  // Free text description of the category
  String description

  // Automatic timestamping
  Date dateCreated
  Date lastUpdated

  static mapping = {
    description type: 'text'
  }
  static constraints = {
    name blank: false, maxSize: 200, unique: true
    description nullable: true
    dateCreated nullable: true
    lastUpdated nullable: true
  }

  static final String DEFAULT_CATEGORY_NAME = 'http://www.activiti.org/test'
  static final String DEFAULT_CATEGORY_DESCRIPTION = 'Activiti built-in category'

  static CrdProcCategory createCategory(String name, String description) {
    if (!CrdProcCategory.findByName(name)) {
      def categ = new CrdProcCategory(name: name, description: description)
      categ.save(failOnError: true)
    }
  }

  String toString() {
    name
  }

}
