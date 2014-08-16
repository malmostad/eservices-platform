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

import java.util.regex.Matcher
import java.util.regex.Pattern

// The only way to create a logger with a predictable name
import org.apache.commons.logging.LogFactory

import org.motrice.postxdb.PxdFormdef;
import org.motrice.postxdb.PxdFormdefVer;
import org.motrice.postxdb.PxdItem;

import org.motrice.postxdb.FormdefPath;
import org.motrice.postxdb.MetaEditor;
import org.motrice.postxdb.MetaExtractor;

/**
 * Methods for supporting the Rest controllers
 * A note on terminology: A form definition may be saved or published.
 * Saving implies storing as a draft, publishing implies storing as a published
 * version, i.e. without a draft number.
 * In either case the form definition XML is modified to insert the new
 * version number.
 */
class RestService {
  private static final log = LogFactory.getLog(this)

  /**
   * Do everything needed when saving a form definition
   * Creates an associated FormdefVer instance, a Formdef if needed.
   */
  PxdItem createDraftItem(String uuid, String resource, String xml) {
    // Note that the version number, if present, is included in the form name
    // as extracted by the meta extractor
    def meta = MetaExtractor.extract(xml)
    // There can only be one kind of xml resource: data.xml
    def path = new FormdefPath("${meta.app}/${meta.form}")

    // Make sure there is a proper Formdef
    // The currentDraft field must always be updated
    def formdef = PxdFormdef.findByPath(path.unversioned)
    if (log.debugEnabled)
      log.debug "createDraftItem findByPath(${path?.unversioned}) returned ${formdef}"

    // Establish a form definition with the new current draft version
    if (formdef) {
      path = new FormdefPath(formdef.currentDraft)
      formdef.currentDraft = path.nextDraft().toString()
      if (!formdef.save(insert: false))
	log.error "Formdef save1: ${formdef.errors.allErrors.join(',')}"
    } else {
      formdef = new PxdFormdef(uuid: uuid)
      formdef.assignPath(path.unversioned)
      formdef.currentDraft = path.nextDraft().toString()
      if (!formdef.save(insert: true))
	log.error "Formdef save2: ${formdef.errors.allErrors.join(',')}"
    }

    // Create a new FormdefVer, attach to Formdef
    // path contains the version assigned to Formdef.currentDraft
    def formdefVer = PxdFormdefVer.createInstance(path)
    formdefVer.assignMeta(meta)
    formdef.addToForms(formdefVer)
    if (!formdefVer.save())
      log.error "FormdefVer save: ${formdefVer.errors.allErrors.join(',')}"

    // Create the item
    def item = new PxdItem(path: "${path}/form.xml", uuid: uuid,
    formDef: path.toString(), instance: false, format: 'xml')
    // Modify the XML to contain the new version number
    MetaEditor editor = new MetaEditor(xml)
    editor.opAssignVersion(path.version, path.draft)
    editor.edit()
    item.assignText(editor.formdefOut)
    if (!item.save()) log.error "Item save: ${item.errors.allErrors.join(',')}"
    return item
  }

  /**
   * Do everything needed to save a form definition resource
   * NOTE: When creating a new form, resources are typically saved before
   * the form definition.
   * This means there is no information about the form definition when the
   * resource is stored, and formDef cannot be set.
   */
  PxdItem createFormdefResource(String uuid, String resource, request) {
    def item = new PxdItem(path: resource, uuid: uuid, instance: false, format: 'binary')
    item.assignStream(request.inputStream.bytes).save()
    if (log.debugEnabled) log.debug "createFormdefResource.item: ${item}"
    return item
  }

  /**
   * Find current form definition draft
   */
  PxdItem findCurrentDraft(String uuid) {
    def result = null
    def formdef = PxdFormdef.findByUuid(uuid)
    if (formdef) {
      String itemPath = "${formdef.currentDraft}/form.xml"
      result = PxdItem.findByPath(itemPath)
    }

    if (log.debugEnabled) log.debug "findCurrentDraft returns ${result}"
    return result
  }

  /**
   * Find a form definition resource
   */
  PxdItem findFormdefResource(String uuid, String resource) {
    return PxdItem.findByPath(resource)
  }

  /**
   * Find editable forms for paged output
   * maxDisplay must be the maximum number of entries to display in a page
   * offset must be the display offset of the current page
   * RETURN a map with the following entries:
   * total (Integer): total number of editable forms
   * list (List of PxdFormdefVer): editable forms in presentation order
   */
  Map findEditableForms(Integer maxDisplay, Integer offset) {
    // Counting does not need ordering
    def qc = 'select count(v) from PxdFormdefVer v where path=formdef.currentDraft'
    // Reliable paging requires order
    def qs = 'from PxdFormdefVer v where path=formdef.currentDraft order by v.path'
    def countArray = PxdFormdefVer.executeQuery(qc)
    Integer count = countArray[0]
    def list = PxdFormdefVer.findAll(qs, [:], [max: maxDisplay, offset: offset])
    def result = [list: list, total: count]
    if (log.debugEnabled)
      log.debug "findEditableForms: total(${count}), list(${list.size()})"
    return result
  }

  static final LIB_Q = 'from PxdFormdefVer v where v.appName=? and v.formName=? ' +
    'and draft=? order by fvno desc limit 1'

  /**
   * Find a library form, i.e. the form name is the magic "library".
   * In this case find and return the latest version.
   * Return null in case there is no library.
   */
  PxdItem findLibraryForm(FormdefPath path) {
    if (log.debugEnabled) log.debug "findLibraryForm << ${path}"
    def result = null
    // Find the latest version
    def versionList = PxdFormdefVer.
    findAll(LIB_Q, [path.appName, FormdefPath.LIBRARY_NAME, FormdefPath.PUBLISHED])
    if (!versionList.isEmpty()) {
      def latestVersion = versionList[0]
      def latestPath = new FormdefPath(path.appName, FormdefPath.LIBRARY_NAME,
				       latestVersion.fvno, FormdefPath.PUBLISHED)
      String itemPath = "${latestPath}/form.xhtml"
      result = PxdItem.findByPath(itemPath)
    }

    if (log.debugEnabled) log.debug "findLibraryForm >> ${result}"
    return result
  }
  
  /**
   * Search among editable forms
   * The output is not paged, just limited to a maximum of 100 tuples
   * RETURN same type of map as findEditableForms
   */
  Map formSearch(Map searchParams) {
    def q = 'from PxdFormdefVer v where path=formdef.currentDraft ' +
      'and appName like :app and formName like :form ' +
      'and title like :title and description like :description order by v.path'
    def list = PxdFormdefVer.findAll(q, searchParams, [max: 100])
    def result = [list: list, total: list.size()]
    if (log.debugEnabled)
      log.debug "findEditableForms: list(${list.size()})"
    return result
  }

  /**
   * Publish a form definition and create a new current draft
   * NOTE: The xml for the published version is different from drafts.
   * So the published version cannot be used as the first draft of the next version.
   * It has to be copied from the latest draft, the draft from which the published
   * version is derived.
   * You may not publish a form unless there is a previous draft, i.e. there must
   * be a current draft.
   * Throws PostxdbException if there is no previous version.
   */
  PxdItem createPublishedItem(String resource, String xml) {
    // The version number, if present, is included in the form name
    // as extracted by the meta extractor
    def meta = MetaExtractor.extract(xml)
    def formdef = doCheckFormdef(meta)
    String initialCurrentDraft = formdef.currentDraft

    // The version number must not be taken from the incoming XML.
    // It must be derived from the current path and imposed on the incoming item.
    FormdefPath publishedPath = doPublishedPath(initialCurrentDraft)
    // Modify the XML to contain the new version number
    MetaEditor editor = new MetaEditor(xml)
    editor.opAssignVersion(publishedPath.version, publishedPath.draft)
    editor.edit()
    // Previous meta is replaced by meta of the published form
    meta = editor.metadata

    // Create a form definition version for the new item
    doCreateFormdefVer(formdef, publishedPath, meta)

    // Create the published item
    def publishedItem = doCreatePublishedItem(formdef, publishedPath, editor)

    //----- From here: Create a new current draft for the next version -----
    // We cannot use the incoming version because the user may have saved several times
    // making the incoming version number out of synch.
    // Instead, use the current draft, called "latest draft" here because we also
    // update the current draft.
    String latestDraftItemPath = doXmlPath(new FormdefPath(initialCurrentDraft))
    def currentDraftVersion = new FormdefPath(initialCurrentDraft).nextVersion()
    String currentDraftPath = currentDraftVersion.toString()
    String currentDraftItemPath = doXmlPath(currentDraftVersion)
    if (log.debugEnabled) {
      log.debug "createPublishedItem copy draft from ${latestDraftItemPath} to ${currentDraftItemPath}"
    }
    formdef.currentDraft = currentDraftPath
    doSave(formdef)
      
    // We must retrieve the PxdItem containing the latest draft to create the new current
    def latestDraftItem = PxdItem.findByPath(latestDraftItemPath)
    if (log.debugEnabled) log.debug "createPublishedItem copy draft ${latestDraftItem}"
    def currentDraftItem = PxdItem.almostCopy(latestDraftItem, currentDraftItemPath, currentDraftPath)
    editor = new MetaEditor(latestDraftItem.text)
    editor.opAssignVersion(currentDraftVersion.version, currentDraftVersion.draft)
    editor.edit()
    meta = editor.metadata
    currentDraftItem.assignText(editor.formdefOut)
    doSave(currentDraftItem)
    if (log.debugEnabled) log.debug "createPublishedItem new current draft ${currentDraftItem}"

    // Create a new form definition version for the new current draft
    doCreateFormdefVer(formdef, currentDraftVersion, meta)

    return publishedItem
  }

  /**
   * Check that a formdef exists with given metadata.
   * Throws PostxdbException otherwise.
   */
  private PxdFormdef doCheckFormdef(FormdefMeta meta) {
    def appForm = new FormdefPath("${meta.app}/${meta.form}").unversioned.toString()
    // There must already be a Formdef
    def formdef = PxdFormdef.findByPath(appForm)
    if (log.debugEnabled) log.debug "createPublishedItem findByPath(${appForm}) returned ${formdef}"
    if (formdef == null) {
      def msg = "Cannot publish '${appForm}', no previous draft exists"
      log.error msg
      throw new PostxdbException('pxdFormdef.required.but.missing', msg)
    }

    return formdef
  }

  /**
   * Derive a published path from a current draft path
   */
  private FormdefPath doPublishedPath(String currentDraft) {
    def publishedPath = new FormdefPath(currentDraft)
    publishedPath.publish()
    if (log.debugEnabled) log.debug "createPublishedItem initial: ${currentDraft}, published: ${publishedPath}"
    return publishedPath
  }

  /**
   * Create and save a form definition version for a new version
   */
  private PxdFormdefVer doCreateFormdefVer(PxdFormdef formdef, FormdefPath path,
					   FormdefMeta meta)
  {
    def formdefVer = PxdFormdefVer.createInstance(path)
    formdefVer.assignMeta(meta)
    formdef.addToForms(formdefVer)
    doSave(formdefVer)
    return formdefVer
  }

  /**
   * Create and save an item for a new version.
   * The editor must contain new metadata and new XML
   */
  private PxdItem doCreatePublishedItem(PxdFormdef formdef, FormdefPath path,
					MetaEditor editor)
  {
    def item = new PxdItem(path: "${path}/form.xhtml", uuid: formdef.uuid,
    formDef: path.toString(), instance: false, format: 'xml')
    item.assignText(editor.formdefOut)
    doSave(item)
    if (log.debugEnabled) log.debug "createPublishedItem will return ${item}"
    return item
  }

  /**
   * Save a domain object, log errors if any
   */
  private doSave(obj) {
    if (!obj.save()) log.error "${obj.class.name} save ${obj.errors.allErrors.join(',')}"
  }

  /**
   * The full path of an item, given a version
   */
  private String doXmlPath(FormdefPath path) {
    path.toString() + "/form.xml"
  }

  /**
   * Publish a resource for a form definition.
   * New behaviour Orbeon Forms 4, never called in Orbeon 3.
   * The url used in this op contains the draft version for which the Publish
   * action was invoked.
   * In the normal case the resource already exists and nothing needs to be done
   */
  PxdItem createPublishedResource(String appName, String formName, String resource,
				 request)
  {
    if (log.debugEnabled) {
      log.debug "createPublishedResource << ${appName}/${formName}/${resource}"
    }
    def item = null
    item = PxdItem.findByPath(resource)
    if (log.debugEnabled) log.debug "createPublishedResource.item ${item}"

    if (!item) {
      String formDef = "${appName}/${formName}"
      item = new PxdItem(path: resource, uuid: uuid, formDef: formDef, instance: false,
      format: 'binary')
      item.assignStream(request.inputStream.bytes).save()
    }

    if (log.debugEnabled) log.debug "createPublishedResource >> ${item}"
    return item
  }

  /**
   * Create form instance xml, or save form instance xml after editing
   * NOTE: Unlike normal Grails we cannot check for optimistic lock failure.
   */
  PxdItem createInstanceItem(String appName, String formName, String uuid,
			     String resource, String xml)
  {
    String formDef = "${appName}/${formName}"
    String itemPath = "${uuid}/data.xml"
    if (log.debugEnabled) log.debug "createInstanceItem << ${formDef}/${uuid}/${resource}"

    def item = PxdItem.findByPath(itemPath)
    if (item) {
      if (log.debugEnabled) log.debug "createInstanceItem EXISTING (${item?.id})"
      doCheckReadOnly(item)
    } else {
      item = new PxdItem(path: itemPath, uuid: uuid, formDef: formDef, instance: true,
      format: 'xml')
      if (log.debugEnabled) log.debug "createInstanceItem NEW (${resource})"
    }

    // If the item was created now or existed previously, update the xml
    item.assignText(xml)
    if (!item.save()) log.error "createInstanceItem save: ${item.errors.allErrors.join(',')}"
    if (log.debugEnabled) log.debug "createInstanceItem >> ${item}"
    return item
  }

  /**
   * Create a resource for a form instance, typically an attachment
   * Very similar to createPublishedResource
   */
  PxdItem createInstanceResource(String appName, String formName, String uuid,
				 String resource, request)
  {
    String formDef = "${appName}/${formName}"
    if (log.debugEnabled) log.debug "createInstanceResource << ${formDef}/${uuid}/${resource}"
    def item = PxdItem.findByPath(resource)

    if (item) {
      doCheckReadOnly(item)
      if (log.debugEnabled) log.debug "createInstanceResource EXISTING item (${item?.id})"
    } else {
      item = new PxdItem(path: resource, uuid: uuid, formDef: formDef, instance: true,
      format: 'binary')
    }

    item.assignStream(request.inputStream.bytes)
    if (!item.save()) log.error "createInstanceResource save: ${item.errors.allErrors.join(',')}"
    if (log.debugEnabled) log.debug "createInstanceResource >> ${item}"
    return item
  }

  /**
   * Checks if an item is read-only. Throws exception in such case.
   */
  private doCheckReadOnly(PxdItem item) {
    if (item.readOnly) {
      def exc = new PostxdbException('pxdItem.update.readonly', 'Read-only item')
      // HTTP Forbidden
      exc.http = 403
      throw exc
    }
  }

  /**
   * Find instance xml
   */
  PxdItem findInstanceItem(String uuid) {
    String itemPath = "${uuid}/data.xml"
    def item = PxdItem.findByPath(itemPath)
    if (log.debugEnabled) log.debug "findInstanceItem returns: ${item}"
    return item
  }

  /**
   * Find a resource for a form instance, typically an attachment
   */
  PxdItem findInstanceResource(String uuid, String resource) {
    return PxdItem.findByPath(resource)
  }

  /**
   * Extract search parameters from an Orbeon form search
   * Orbeon sends XPath patterns, ugh
   * There may be up to four search conditions
   * searchStrings and xpaths contain a list of strings if there are more than
   * one condition, otherwise they contain a string
   */
  Map extractSearchParameters(searchStrings, xpaths) {
    def map = ['application-name':'%', 'form-name':'%', 'title':'%', 'description':'%']
    if (searchStrings instanceof String) searchStrings = [searchStrings]
    if (xpaths instanceof String) xpaths = [xpaths]
    if (log.debugEnabled) {
      log.debug "extractSearchParameters:"
      log.debug "  strings: ${searchStrings}"
      log.debug "  xpaths:  ${xpaths}"
    }

    // The xpath for a keyword contains the pattern /keyword
    def keyMap = [:]
    map.keySet().each {key ->
      keyMap['/' + key] = key
    }

    // For each xpath, test for all keywords
    xpaths.eachWithIndex {expr, idx ->
      def key = keyMap.keySet().find {expr.indexOf(it) >= 0}
      if (key) {
	String term = keyMap[key]
	map[term] = sqlGlob(searchStrings[idx])
      }
    }

    // Post-process
    map['app'] = map['application-name']
    map.remove('application-name')
    map['form'] = map['form-name']
    map.remove('form-name')

    if (log.debugEnabled) log.debug "extractSearchParameters: ${map}"
    return map
  }

  /**
   * Globbing in search strings, SQL style
   */
  private String sqlGlob(String input) {
    String output = input.replace("'", '')
    output = output.replace('*', '%')
    output = output.replace('?', '_')
    return output
  }

}
