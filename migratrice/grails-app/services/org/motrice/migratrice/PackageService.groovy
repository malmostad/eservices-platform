package org.motrice.migratrice

import java.util.regex.*

// The only way to create a logger with a predictable name?
import org.apache.commons.logging.LogFactory

import org.codehaus.groovy.grails.web.metaclass.BindDynamicMethod
import org.codehaus.groovy.grails.commons.metaclass.GroovyDynamicMethodsInterceptor

class PackageService {
  def grailsApplication

  private static final log = LogFactory.getLog(this)

  // Pattern for a formdef path
  static final PATH = Pattern.compile('([^_/][^/]+)/([^/]+)')
  // Pattern for the name of a hidden field
  static final HID = Pattern.compile('ref-(.+)')

  /**
   * Magic for using the bindData method in a service
   */
  PackageService() {
    def interceptor = new GroovyDynamicMethodsInterceptor(this)
    interceptor.addDynamicMethodInvocation(new BindDynamicMethod())
  }

  /**
   * Find all formdefs in the local postxdb database.
   * Return List of MigFormdef (not sorted).
   */
  List allLocalFormdefs() {
    if (log.debugEnabled) log.debug "allLocalFormdefs <<"
    def allFormdefs = localPostxdb('formdef').text
    def map = parseXmlList(allFormdefs)
    def formdefList = []
    if (map.className == 'pxdFormdef') {
      formdefList = map.instMaps.collect {params ->
	def obj = new MigFormdef()
	bindData(obj, params)
	return obj
      }
    }
    if (log.debugEnabled) log.debug "allLocalFormdefs >> ${formdefList.size()}"
    return formdefList
  }

  /**
   * Find a formdef in the local postxdb database given its id.
   * The id is usually a string, the type is not important.
   */
  MigFormdef singleLocalFormdef(id) {
    if (log.debugEnabled) log.debug "singleLocalFormdefs << ${id}"
    // This call returns a list
    def formdefXml = localPostxdb("formdef/${id}").text
    def map = parseXmlList(formdefXml)
    def formdefList = []
    if (map.className == 'pxdFormdef') {
      formdefList = map.instMaps.collect {params ->
	def obj = new MigFormdef()
	bindData(obj, params)
	return obj
      }
    }

    def result = (formdefList.isEmpty())? null : formdefList[0]
    if (log.debugEnabled) log.debug "singleLocalFormdefs >> ${result}"
    return result
  }

  /**
   * Find all form definition versions given a form definition.
   * Return List of MigFormdefVer (not sorted).
   */
  List allLocalFormdefVers(MigFormdef formdef) {
    if (log.debugEnabled) log.debug "allLocalFormdefVers << ${formdef}"
    // This call returns a list
    def formdefXml = localPostxdb("formdefver?formdef=${formdef?.ref}").text
    def map = parseXmlList(formdefXml)
    def resultList = []
    if (map.className == 'pxdFormdefVer') {
      resultList = map.instMaps.collect {params ->
	def obj = new MigFormdefVer()
	bindData(obj, params)
	return obj
      }
    }

    if (log.debugEnabled) log.debug "allLocalFormdefVers >> ${resultList.size()}"
    return resultList
  }

  /**
   * Find all form definition items given a form definition.
   * Return List of MigItem.
   */
  List allLocalDefItems(MigFormdef formdef) {
    if (log.debugEnabled) log.debug "allLocalDefItems << ${formdef}"
    // This call returns a list
    def formdefXml = localPostxdb("defitem?formdef=${formdef?.ref}").text
    def map = parseXmlList(formdefXml)
    def resultList = []
    if (map.className == 'pxdItem') {
      resultList = map.instMaps.collect {params ->
	def obj = new MigItem()
	bindData(obj, params)
	return obj
      }
    }

    if (log.debugEnabled) log.debug "allLocalDefItems >> ${resultList.size()}"
    return resultList
  }

  /**
   * Create an export package from input containing
   */
  MigPackage createExportPackage(String packageName, params) {
    if (log.debugEnabled) log.debug "createExportPackage << ${packageName} ..."
    def siteName = grailsApplication.config.migratrice.local.site.name
    def pkg = new MigPackage(siteName: siteName, packageName: packageName,
    packageFormat: packageFormat(), siteTstamp: new Date(), originLocal: true)
    if (!pkg.save(insert: true)) log.error "MigPackage save: ${pkg.errors.allErrors.join(',')}"

    // Find the selected form definitions
    // First create a map containing id of formdefs
    def formdefMap = [:]
    params.keySet().each {key ->
      def m = HID.matcher(key)
      if (m.matches()) {
	formdefMap[m.group(1)] = params[key]
      }
    }
    def formdefList = []
    def itemMap = [:]
    params.keySet().each {key ->
      def m = PATH.matcher(key)
      if (m.matches() && params[key] == 'on') {
	String path = "${m.group(1)}/${m.group(2)}"
	def formdefId = formdefMap[path]
	if (formdefId) formdefList.add(singleLocalFormdef(formdefId))
      }
    }
    
    // Set up relationships
    formdefList.each {formdef ->
      // Add the new form definition to the package
      pkg.addToFormdefs(formdef)
      if (!formdef.save(insert: true)) log.error "MigFormdef save: ${formdef.errors.allErrors.join(',')}"
      // Find and add all form definition versions
      def fdvList = allLocalFormdefVers(formdef)
      fdvList.each {fdv ->
	formdef.addToVersions(fdv)
	pkg.addToVersions(fdv)
	if (!fdv.save(insert: true)) log.error "MigFormdefVer save: ${fdv.errors.allErrors.join(',')}"
      }

      // Find and remember all items, do not save just yet
      def itemList = allLocalDefItems(formdef)
      itemList.each {item ->
	formdef.addToItems(item)
	pkg.addToItems(item)
	if (!item.save(insert: true)) log.error "MigItem save: ${item.errors.allErrors.join(',')}"
      }
    }

    if (log.debugEnabled) log.debug "createExportPackage >> ${formdefList.size()} formdefs"
    return pkg
  }

  /**
   * Create a request url for the local postxdb server.
   */
  private URL localPostxdb(String tail) {
    def head = grailsApplication.config.migratrice.postxdb.uri
    return new URL("${head}/postxdb/${tail}")
  }

  private String packageFormat() {
    def appName = grailsApplication.metadata['app.name']
    def appVersion = grailsApplication.metadata['app.version']
    return "${appName}-${appVersion}"
  }

  /**
   * Parse an XML list of entities.
   * Return a map with the following entries:
   * className: the class name of the entities (String)
   * instMaps: a number of instances where each instance is a map (List of Map).
   * The maps are property maps where all values are strings.
   */
  private Map parseXmlList(String xml) {
    def root = new XmlSlurper().parseText(xml)
    def className = null
    def instanceList = root.'*'.collect {node ->
      if (!className) className = node.name()
      def map = [:]
      node.'*'.each {prop ->
	map[prop.name()] = prop.text()
	prop.attributes().each {attr ->
	  map[attr.key] = attr.value
	}
      }

      return map
    }

    return [className: className, instMaps: instanceList]
  }

}
