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
 * Storing form definition XML always implies modifying it in order to set the
 * version number.
 */
class RestService {
  private static final log = LogFactory.getLog(this)
  // Pattern for distilling meaning from an XPath search expression sent by Orbeon
  private static final Pattern SPAT = Pattern.compile(/.*\]\/\*\/([a-z][a-z-]*).*/)

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
    // A draft must use English, otherwise the title and the description are
    // hidden in the editor
    editor.opLangEdit('en')
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
  
  /**
   * Search among editable forms
   * The output is not paged, just limited to a maximum of 100 tuples
   * RETURN same type of map as findEditableForms
   */
  Map doSearch(Map searchParams) {
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
   * Get the path of a form definition version using the path map
   * generated by the above method
   */
  String pathOfEditableForm(Map pathMap, String baseName) {
    def result = pathMap[baseName]
    if (log.debugEnabled) log.debug "pathOfEditableForm(${baseName}): ${result}"
    return result
  }

  /**
   * Publish a form definition and create a new current draft
   * NOTE: The xml for the published version is very different from drafts.
   * So the published version cannot be used as the first draft of the next version.
   * It has to be copied from the latest draft, the draft from which the published
   * version is derived.
   */
  PxdItem createPublishedItem(String resource, String xml) {
    // Note that the version number, if present, is included in the form name
    // as extracted by the meta extractor
    def meta = MetaExtractor.extract(xml)
    // This is the draft that must be copied to a new current draft
    def path = new FormdefPath("${meta.app}/${meta.form}")
    def latestDraft = new FormdefPath(path)
    if (log.debugEnabled) log.debug "createPublishedItem initial path: ${path}"

    // There should already be a Formdef
    def formdef = PxdFormdef.findByPath(path.unversioned)
    if (log.debugEnabled) log.debug "createPublishedItem findByPath(${path?.unversioned}) returned ${formdef}"
    assert formdef != null

    // Update the version number, update xml with new metadata
    path.publish()
    if (log.debugEnabled) log.debug "createPublishedItem published path: ${path}"
    // Modify the XML to contain the new version number
    MetaEditor editor = new MetaEditor(xml)
    editor.opAssignVersion(path.version, path.draft)
    // Set language Swedish in the published version
    editor.opLangEdit('sv')
    editor.edit()
    meta = editor.metadata

    def formdefVer = PxdFormdefVer.createInstance(path)
    formdefVer.assignMeta(meta)
    formdef.addToForms(formdefVer)
    if (!formdefVer.save()) log.error "FormdefVer save: ${formdefVer.errors.allErrors.join(',')}"

    // Create the item
    def item = new PxdItem(path: "${path}/form.xhtml", uuid: formdef.uuid,
    formDef: path.toString(), instance: false, format: 'xml')
    item.assignText(editor.formdefOut)
    if (!item.save()) log.error "Item save: ${item.errors.allErrors.join(',')}"
    if (log.debugEnabled) log.debug "createPublishedItem returns ${item}"
    def publishedItem = item

    //----- From here: Create a new current draft for the next version -----
    path = new FormdefPath(latestDraft)
    formdef.currentDraft = path.nextVersion().toString()
    if (!formdef.save()) log.error "Formdef next draft save: ${formdef.errors.allErrors.join(',')}"
      
    // We must retrieve the PxdItem containing the latest draft to create the new current
    String latestDraftPath = "${latestDraft}/form.xml"
    String currentDraftPath = "${path}/form.xml"
    def latestDraftItem = PxdItem.findByPath(latestDraftPath)
    if (log.debugEnabled) log.debug "createPublishedItem latest draft ${latestDraftItem}"
    def currentDraftItem = PxdItem.almostCopy(latestDraftItem, currentDraftPath, path.toString())
    editor = new MetaEditor(latestDraftItem.text)
    editor.opAssignVersion(path.version, path.draft)
    editor.edit()
    meta = editor.metadata
    currentDraftItem.assignText(editor.formdefOut)
    if (!currentDraftItem.save()) log.error "Item save: ${item.errors.allErrors.join(',')}"
    if (log.debugEnabled) log.debug "createPublishedItem new current draft ${currentDraftItem}"

    // Create a new formdefVer
    formdefVer = PxdFormdefVer.createInstance(path)
    formdefVer.assignMeta(meta)
    formdef.addToForms(formdefVer)
    if (!formdefVer.save()) log.error "FormdefVer save: ${formdefVer.errors.allErrors.join(',')}"

    return publishedItem
  }

  /**
   * Publish a resource for a form definition.
   * New behaviour Orbeon Forms 4, never called in Orbeon 3.
   * The url used in this op contains the draft version for which the Publish
   * action was invoked.
   */
  PxdItem createPublishedResource(String resource, String xml) {
  }

  /**
   * Create form instance xml
   */
  PxdItem createInstanceItem(String appName, String formName, String uuid,
			     String resource, String xml)
  {
    String itemPath = "${uuid}/data.xml"
    String formDef = "${appName}/${formName}"
    def item = new PxdItem(path: itemPath, uuid: uuid, formDef: formDef, instance: true,
    format: 'xml')
    item.assignText(xml)
    if (!item.save()) log.error "createInstanceItem save: ${item.errors.allErrors.join(',')}"
    return item
  }

  /**
   * Create a resource for a form instance, typically an attachment
   */
  PxdItem createInstanceResource(String appName, String formName, String uuid,
				 String resource, request)
  {
    String formDef = "${appName}/${formName}"
    def item = new PxdItem(path: resource, uuid: uuid, formDef: formDef, instance: true,
    format: 'binary')
    item.assignStream(request.inputStream.bytes).save()
    if (log.debugEnabled) log.debug "createInstanceResource.item: ${item}"
    return item
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

    xpaths.eachWithIndex {expr, idx ->
      def m = SPAT.matcher(expr)
      if (m.matches()) {
	String term = m.group(1)
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
