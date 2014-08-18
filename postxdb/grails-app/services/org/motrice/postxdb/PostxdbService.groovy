/* == Motrice Copyright Notice ==
 *
 * Motrice Service Platform
 *
 * Copyright (C) 2011-2014 Motrice AB
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * e-mail: info _at_ motrice.se
 * mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN
 * phone: +46 8 641 64 14
 */
package org.motrice.postxdb

// The only way to create a logger with a predictable name
import org.apache.commons.logging.LogFactory

/**
 * Methods for supporting the postxdb Rest methods, i.e. methods
 * not in the eXist db interface.
 * There is essentially a one-to-one correspondance between Rest methods
 * and the methods of this service.
 */
class PostxdbService {
  private static final log = LogFactory.getLog(this)

  /**
   * Pick the form definition with the given id, or return all formdefs if
   * id is null.
   * Return List of PxdFormdef or null if nothing is found.
   */
  List formdefGet(Long id) {
    if (log.debugEnabled) log.debug "formdefGet << ${id}"
    def result = null
    if (id) {
      def formdef = PxdFormdef.get(id)
      if (formdef) result = [formdef]
    } else {
      result = PxdFormdef.list()
    }

    if (log.debugEnabled) log.debug "formdefGet >> ${result?.size()}"
    return result
  }

  /**
   * Pick the form definition version with the given id, or use other
   * parameters if id is null
   * Return a map containing the following entries:
   * status. HTTP status (Integer), 200, 404 or 409.
   * message: Conflict message if nothing was found (String), null if ok.
   * list: List of PxdFormdefVer or null if nothing was found
   */
  Map formdefVerGet(Long id, String uuid, String formdefId) {
    if (log.debugEnabled) log.debug "formdefVerGet << ${id}, ${uuid}, ${formdefId}"
    def result = [:]
    if (id) {
      def obj = PxdFormdefVer.get(id)
      if (obj) {
	result.list = [obj]
	result.status = 200
      } else {
	result.message = 'Nothing found with the given id'
	result.status = 404
      }
    } else {
      if (uuid && formdefId) {
	result.message = 'Specify either uuid or formdef, not both'
	result.status = 409
      } else if (!(uuid || formdefId)) {
	result.message = 'One of uuid or formdef must be specified'
	result.status = 409
      } else {
	def formdef = null
	if (uuid) {
	  formdef = PxdFormdef.findByUuid(uuid)
	  if (!formdef) {
	    result.status = 404
	    result.message = 'Formdef not found with the given uuid'
	  }
	} else if (formdefId) {
	  try {
	    formdef = PxdFormdef.get(formdefId as Long)
	  } catch (NumberFormatException exc) {
	    // Ignore
	  }

	  if (!formdef) {
	    result.status = 404
	    result.message = 'Formdef not found with the given id'
	  }
	}

	if (formdef) {
	  result.list = PxdFormdefVer.findAllByFormdef(formdef)
	  if (result.list.isEmpty()) {
	    result.status = 404
	    result.message = 'Formdef not found with the given id'
	  } else {
	    // Sort the list on descending version (latest first)
	    result.list = result.list.sort()
	    result.status = 200
	  }
	}
      }
    }

    if (log.debugEnabled) {
      def size = result.list?.size()
      def status = result.status
      def msg = result.message
      log.debug "formdefVerGet >> list: ${size}, status: ${status}, '${msg}'"
    }

    return result
  }

  /**
   * Get form definition item metadata.
   * Return a map containing the following entries:
   * status. HTTP status (Integer), 200, 404 or 409.
   * message: Conflict message if nothing was found (String), null if ok.
   * list: List of PxdItem or null if nothing was found
   */
  Map defItemGet(String uuid, String formdefId) {
    if (log.debugEnabled) log.debug "defItemGet << ${uuid}, ${formdefId}"
    def result = [:]
    if (uuid && formdefId) {
      result.message = 'Specify either uuid or formdef, not both'
      result.status = 409
    } else if (!(uuid || formdefId)) {
      result.message = 'One of uuid or formdef must be specified'
      result.status = 409
    } else {
      def effectiveUuid = null
      def formdef = null
      if (formdefId) {
	try {
	  formdef = PxdFormdef.get(formdefId as Long)
	} catch (NumberFormatException exc) {
	  // Ignore
	}

	if (formdef) {
	  effectiveUuid = formdef.uuid
	} else {
	  result.status = 404
	  result.message = 'Formdef not found with the given id'
	}
      } else if (uuid) {
	formdef = PxdFormdef.findByUuid(uuid)

	if (formdef) {
	  effectiveUuid = uuid
	} else {
	  result.status = 404
	  result.message = 'Formdef not found with the given id'
	}
      }

      if (effectiveUuid) {
	result.formref = formdef.id
	result.list = PxdItem.findAllWhere(uuid: effectiveUuid, instance: false)
	if (result.list.isEmpty()) {
	  result.status = 404
	  result.message = 'No items found'
	} else {
	  result.status = 200
	}
      }
    }

    if (log.debugEnabled) {
      def size = result.list?.size()
      def status = result.status
      def msg = result.message
      log.debug "defItemGet >> list(${result?.formref}): ${size}, status: ${status}, '${msg}'"
    }

    return result
  }

  private static ITEMQ = 'from PxdItem where uuid in (:uuidList) order by uuid'
  /**
   * Get form instance item metadata.
   * Return List of PxdItem or null if nothing was found
   */
  List instItemGet(String formdefVerId) {
    if (log.debugEnabled) log.debug "instItemGet << ${formdefVerId}"
    def result = null
    def uuid = null
    def formdefVer = null
    if (formdefVerId) {
      try {
	formdefVer = PxdFormdefVer.get(formdefVerId as Long)
      } catch (NumberFormatException exc) {
	// Ignore
      }

      if (formdefVer) {
	def xmlList = PxdItem.findAllWhere(formDef: formdefVer.path, format: 'xml',
	instance: true)
	if (xmlList) {
	  def uuidList = xmlList.collect {item -> item.uuid}
	  if (log.debugEnabled) log.debug "uuids(${uuidList.size()}): ${uuidList}"
	  result = PxdItem.findAll(ITEMQ, [uuidList: uuidList])
	}
      }
    }

    if (log.debugEnabled) log.debug "instItemGet >> ${result?.size()}"
    return result
  }

  /**
   * Get the item containing XML form data for a given instance uuid.
   */
  PxdItem instItemGetXml(String uuid) {
    if (log.debugEnabled) log.debug "instItemGetXml << ${uuid}"
    def item = PxdItem.findByPath(uuid + '/data.xml')
    if (log.debugEnabled) log.debug "instItemGetXml >> ${item}"
    return item
  }

  /**
   * Get all items belonging to a form instance.
   */
  List instItemGetAll(String uuid) {
    if (log.debugEnabled) log.debug "instItemGetAll << ${uuid}"
    def items = PxdItem.findAllByUuid(uuid)
    if (log.debugEnabled) log.debug "instItemGetAll >> ${items?.size()}"
    return items
  }

  /**
   * Get an item given its database id.
   */
  PxdItem itemGet(Long id) {
    if (log.debugEnabled) log.debug "itemGet << ${id}"
    def item = PxdItem.get(id)
    if (log.debugEnabled) log.debug "itemGet >> ${item}"
    return item
  }

  /**
   * Duplicate a form instance identified by its XML form data item.
   */
  PxdItem duplicateInstance(PxdItem srcItem, String tgtuuid, boolean readOnlyFlag) {
    if (log.debugEnabled) log.debug "duplicateInstance << ${srcItem?.id}, ${tgtuuid}, ${readOnlyFlag}"
    def tgtItem = PxdItem.formDataCopy(srcItem, tgtuuid)
    if (!tgtItem.save(failOnError: true)) log.error "duplicateInstance tgtItem: ${tgtItem.errors.allErrors.join(',')}"
    srcItem.readOnly = readOnlyFlag
    if (!srcItem.save()) {
      srcItem.errors.allErrors.each {err ->
	log.error "WOW: field[${err.field}], value[${err.rejectedValue}], d-msg[${err.defaultMessage}]"
      }
    }
    if (log.debugEnabled) log.debug "duplicateInstance >> ${tgtItem}"
    return tgtItem
  }

  /**
   * Set the read-only state of all items belonging to a form instance.
   * itemList must be a list of all items of the instance.
   * RETURN the number of processed items
   */
  int makeInstanceReadOnly(List itemList, boolean readOnlyFlag) {
    int count = itemList.size()
    if (log.debugEnabled) log.debug "makeInstanceReadOnly << ${count}"
    itemList.each {item ->
      item.readOnly = readOnlyFlag
      if (!item.save()) {
	def msg = item.errors.allErrors.join(',')
	throw new PostxdbException('pxdItem.update.problem', msg)
      }
    }

    if (log.debugEnabled) log.debug "makeInstanceReadOnly >> ${count}"
    return count
  }

}
