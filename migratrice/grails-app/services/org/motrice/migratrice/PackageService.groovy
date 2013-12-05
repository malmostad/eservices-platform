package org.motrice.migratrice

import java.text.SimpleDateFormat
import java.util.regex.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import grails.converters.*

// The only way to create a logger with a predictable name?
import org.apache.commons.logging.LogFactory

import org.codehaus.groovy.grails.web.metaclass.BindDynamicMethod
import org.codehaus.groovy.grails.commons.metaclass.GroovyDynamicMethodsInterceptor

import org.motrice.zip.ZipBuilder

class PackageService {
  def grailsApplication

  private static final log = LogFactory.getLog(this)

  // Pattern for a formdef path
  static final PATH = Pattern.compile('([^_/][^/]+)/([^/]+)')
  // Pattern for the name of a hidden field
  static final HID = Pattern.compile('ref-(.+)')
  // Second-based timestamp for file name inclusion
  static final FMT = 'yyMMdd-HHmmss'
  // Pattern for item zip entry names
  static final ITEMPAT = Pattern.compile( /item#(\d+).((?:bin)|(?:xml))/ )

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
    def allFormdefs = localPostxdbText('formdef')
    def map = parseXmlString(allFormdefs)
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
  private MigFormdef singleLocalFormdef(id) {
    if (log.debugEnabled) log.debug "singleLocalFormdefs << ${id}"
    // This call returns a list
    def formdefXml = localPostxdbText("formdef/${id}")
    def map = parseXmlString(formdefXml)
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
  private List allLocalFormdefVers(MigFormdef formdef) {
    if (log.debugEnabled) log.debug "allLocalFormdefVers << ${formdef}"
    // This call returns a list
    def formdefXml = localPostxdbText("formdefver?formdef=${formdef?.ref}")
    def map = parseXmlString(formdefXml)
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
  private List allLocalDefItems(MigFormdef formdef) {
    if (log.debugEnabled) log.debug "allLocalDefItems << ${formdef}"
    // This call returns a list
    def formdefXml = localPostxdbText("defitem?formdef=${formdef?.ref}")
    def map = parseXmlString(formdefXml)
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
    def pack = new MigPackage(siteName: siteName, packageName: packageName,
    packageFormat: packageFormat(), siteTstamp: new Date(), originLocal: true)
    if (!pack.save(insert: true)) log.error "MigPackage save: ${pack.errors.allErrors.join(',')}"

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
      pack.addToFormdefs(formdef)
      if (!formdef.save(insert: true)) log.error "MigFormdef save: ${formdef.errors.allErrors.join(',')}"
      // Find and add all form definition versions
      def fdvList = allLocalFormdefVers(formdef)
      fdvList.each {fdv ->
	formdef.addToVersions(fdv)
	pack.addToVersions(fdv)
	if (!fdv.save(insert: true)) log.error "MigFormdefVer save: ${fdv.errors.allErrors.join(',')}"
      }

      // Find and remember all items, do not save just yet
      def itemList = allLocalDefItems(formdef)
      itemList.each {item ->
	formdef.addToItems(item)
	pack.addToItems(item)
	if (!item.save(insert: true)) log.error "MigItem save: ${item.errors.allErrors.join(',')}"
      }
    }

    if (log.debugEnabled) log.debug "createExportPackage >> ${formdefList.size()} formdefs"
    return pack
  }

  /**
   * Generate a suitable filename when downloading a package
   */
  String downloadFileName(MigPackage pack) {
    "${pack.siteName}-${pack.packageName}-${new Date().format(FMT)}.pwp"
  }

  /**
   * Convert a package to zip format, send it to an output stream.
   * SIDE EFFECT: Writes to the output stream.
   */
  def toZip(MigPackage pack, OutputStream output) {
    if (log.debugEnabled) log.debug "toZip << ${pack}"
    // The first entry contains all package metadata
    // A list of lists with one class per list
    def metaData = [[pack]]
    metaData.add pack?.formdefs as List
    metaData.add pack?.versions as List
    metaData.add pack?.items as List

    def entryCount = 0
    new ZipBuilder(output).zip {
      entry("package#${pack.id}.xml") {
	it << (metaData as XML)
	entryCount++
      }
      // If the package is locally generated then item contents is not
      // duplicated, but has to be fetched from the local database.
      pack?.items?.each {item ->
	if (item.xmlFormat()) {
	  def entryName = "item#${item.id}.xml"
	  if (log.debugEnabled) log.debug "toZip ${entryName}: ${item}"
	  entry(entryName) {
	    if (pack.originLocal) {
	      it << localPostxdbText("item/${item.ref}")
	    } else {
	      it << item.text
	    }
	    entryCount++
	  }
	} else { // everything else is binary
	  def entryName = "item#${item.id}.bin"
	  if (log.debugEnabled) log.debug "toZip ${entryName}: ${item}"
	  entry(entryName) {
	    if (pack.originLocal) {
	      it << localPostxdbBytes("item/${item.ref}")
	    } else {
	      it << item.stream
	    }
	  }
	  entryCount++
	}
      }
    }

    if (log.debugEnabled) log.debug "toZip >> ${entryCount} entries"
  }

  MigPackage importPackage(InputStream inputStream) {
    if (log.debugEnabled) log.debug "importPackage << ${inputStream?.class?.name}"
    def zipInput = new ZipInputStream(inputStream)
    // The first entry is package metadata (XML)
    def entry = zipInput.nextEntry
    def ename = entry.name
    assert ename?.startsWith('package')
    def baos = ZipUtil.read(zipInput)
    def xml = baos.toString('UTF-8')
    if (log.debugEnabled) log.debug "importPackage xml ${xml.size()}"
    def metaList = parseImportedMetadata(xml)
    def pack = createImportedPackageObject(metaList)
    def objMap = createAllImportedObjects(pack, metaList)
    linkAllImportedObjects(objMap)
    readAllImportedContents(zipInput, objMap)
    // Save remaining objects
    objMap.versions.values().each {doSaveImportedObject(it)}
    objMap.items.values().each {doSaveImportedObject(it)}
    
    if (log.debugEnabled) log.debug "importPackage >> ${objMap}"
    return null
  }

  /**
   * Construct (but do not save) the imported package object.
   * Throw an exception if the imported package equals one already
   * in the database.
   */
  private MigPackage createImportedPackageObject(List metaList) {
    def map = metaList[0]
    def packageList = null
    if (map.className == 'migPackage') {
      packageList = map.instMaps.collect {params ->
	def obj = new MigPackage()
	bindData(obj, params)
      }
    }

    def pack = packageList[0]
    pack.originLocal = false
    def dbPackage = MigPackage.findBySiteNameAndPackageName(pack.siteName, pack.packageName)
    check(dbPackage != null, 'migPackage.upload.file.duplicate',
	"Package already exists, not imported: ${pack.siteName}-${pack.packageName}")
    return pack
  }

  /**
   * Construct all imported objects.
   * Return a map of maps containing all imported objects.
   */
  private Map createAllImportedObjects(MigPackage pack, List metaList) {
    def formdefList = null
    def formVerList = null
    def itemList = null
    def obj = null
    // MigPackage is intentionally omitted from the switch below because
    // it is instantiated separately.
    // The belongsTo relationships are excluded from data binding.
    // The reason is that the XML contains a reference here as a string.
    // This causes an error that remains in the object and ruins validation
    // when saving to the database.
    metaList.each {map ->
      switch(map.className) {
      case 'migFormdef':
      formdefList = map.instMaps.collect {params ->
	obj = new MigFormdef()
	bindData(obj, params, [exclude: ['pack']])
      }
      break
      case 'migFormdefVer':
      formVerList = map.instMaps.collect {params ->
	obj = new MigFormdefVer()
	bindData(obj, params, [exclude: ['formdef', 'pack']])
      }
      break
      case 'migItem':
      itemList = map.instMaps.collect {params ->
	obj = new MigItem()
	bindData(obj, params, [exclude: ['formdef', 'pack']])
      }
      }
    }

    def formdefMap = [:]
    def formVerMap = [:]
    def itemMap = [:]
    formdefList.each {inst ->
      formdefMap[inst.ref] = inst
    }
    formVerList.each {inst ->
      formVerMap[inst.ref] = inst
    }
    itemList.each {inst ->
      itemMap[inst.ref] = inst
    }

    return [pack: pack, formdefs: formdefMap, versions: formVerMap, items: itemMap]
  }

  /**
   * Re-create links between the imported objects.
   * @params objMap must be a map returned by createAllImportedObjects
   * SIDE EFFECT: Links created between the objects in the object map.
   */
  private linkAllImportedObjects(Map objMap) {
    MigPackage pack = objMap.pack
    doSaveImportedObject(pack)

    // Everything is linked to the package
    objMap.formdefs.values().each {formdef ->
      pack.addToFormdefs(formdef)
    }
    objMap.versions.values().each {formVer ->
      pack.addToVersions(formVer)
    }
    objMap.items.values().each {item ->
      pack.addToItems(item)
    }

    // Save all formdefs
    objMap.formdefs.values().each {formdef ->
      doSaveImportedObject(formdef)
    }

    // Form definitions are linked to versions and items
    objMap.versions.values().each {version ->
      def formdef = objMap.formdefs[version.formref]
      check(formdef == null, 'migPackage.upload.file.inconsistent',
	   "Cannot find formdef ref(${version.formref}) for formdefVer(${version.ref})")
      formdef.addToVersions(version)
    }
    objMap.items.values().each {item ->
      def formdef = objMap.formdefs[item.formref]
      check(formdef == null, 'migPackage.upload.file.inconsistent',
	   "Cannot find formdef ref(${item.formref}) for item(${item.ref})")
      formdef.addToItems(item)
    }
  }

  /**
   * Read imported item contents and add to database objects.
   */
  private readAllImportedContents(ZipInputStream zip, Map objMap) {
    def entry = null
    while ((entry = zip.nextEntry)) {
      def ename = entry.name
      if (log.debugEnabled) log.debug "readAllImportedContents: ${ename}"
      def m = ITEMPAT.matcher(ename)
      check(!m.matches(), 'migPackage.upload.file.inconsistent',
	   "Reading zip contents, unexpected item name: ${ename}")
      Long ref = m.group(1) as Long
      def item = objMap.items[ref]
      check(item == null, 'migPackage.upload.file.inconsistent',
	   "Reading zip contents, cannot find item(${ref})")
      String extension = m.group(2)
      def baos = ZipUtil.read(zip)
      zip.closeEntry()
      if (extension == 'xml') {
	item.assignText(baos.toString('UTF-8'))
      } else {
	item.assignStream(baos.toByteArray())
      }
    }
  }

  private doSaveImportedObject(obj) {
    // All objects are new, so always insert
    check(!obj.save(insert: true), 'migPackage.upload.file.dbconflict',
	  "Cannot save ${obj}: ${obj.errors.allErrors.join(', ')}")
  }

  private check(boolean condition, String code, String message) {
    if (condition) {
      log.warn "Exception thrown: ${message}"
      throw new MigratriceException(code, message)
    }
  }

  /**
   * Get bytes from the local postxdb instance with exception handling.
   * @param tail must be the last part of the postxdb uri
   */
  private byte[] localPostxdbBytes(String tail) {
    byte[] result = null
    try {
      result = localPostxdb(tail).bytes
    } catch (ConnectException exc) {
      log.error "Postxdb connection problem: ${exc}"
      throw new MigratriceException('postxdb.connect.problem', exc.message)
    } catch (IOException exc) {
      log.error "Postxdb reading problem: ${exc}"
      throw new MigratriceException('postxdb.read.problem', exc.message)
    }

    return result
  }

  /**
   * Get text from the local postxdb instance with exception handling.
   * @param tail must be the last part of the postxdb uri
   */
  private String localPostxdbText(String tail) {
    String result = null
    try {
      result = localPostxdb(tail).text
    } catch (ConnectException exc) {
      log.error "Postxdb connection problem: ${exc}"
      throw new MigratriceException('postxdb.connect.problem', exc.message)
    } catch (IOException exc) {
      log.error "Postxdb reading problem: ${exc}"
      throw new MigratriceException('postxdb.read.problem', exc.message)
    }

    return result
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
  private Map parseXmlString(String xml) {
    def root = new XmlSlurper().parseText(xml)
    return parseXmlList(root)
  }

  /**
   * Continue parsing an XML list from a root created by XmlSlurper.parseText
   */
  private Map parseXmlList(root) {
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

  /**
   * Parse the metadata entry of an uploaded package.
   * RETURN List of List.
   * There is one sublist per metadata class.
   * Each sublist is a List of Map.
   * The map is the one returned by parseXmlList
   */
  private parseImportedMetadata(String xml) {
    def outer = new XmlSlurper().parseText(xml)
    def mapList = outer.'*'.collect {list ->
      parseXmlList(list)
    }

    return mapList
  }

}
