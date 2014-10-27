package org.motrice.tdocbox

import com.budjb.requestbuilder.RequestBuilder
import com.budjb.requestbuilder.ResponseStatusException

/**
 * Service for invoking the REST methods of DocBox.
 */
class TdbDrillService {
  // All methods are transactional (actually the default)
  static transactional = true

  /**
   * Execute a drill.
   */
  TdbCase perform(TdbSuite suite, TdbDrill drill, TdbCase cs) {
    if (log.debugEnabled) log.debug "perform << ${drill?.debug}"
    // Sleep for a while if the drill has delay.
    // The delay is not included in the timing.
    if (drill.delay) {
      try {
	// Seconds (Float) to millis (Long).
	Thread.sleep(drill.delay * 1000.0f as Long)
      } catch (InterruptedException exc) {
	Thread.currentThread().interrupt()
      }
    }

    return drill.method.displayMethod? performDisplayMethod(suite, drill, cs) :
    performRestMethod(suite, drill, cs)
  }

  TdbCase performDisplayMethod(TdbSuite suite, TdbDrill drill, TdbCase cs) {
    if (log.debugEnabled) log.debug "performDisplayMethod << Case: ${cs}"
    def urlString = null
    try {
      urlString = TdbMethod.createAbsoluteUrl(drill, cs)
    } catch (MissingPropertyException exc) {
      throw new ServiceException("Value missing for variable '${exc.property}'")
    }

    cs = suite.createCase(cs)
    String itemName = "${drill.name}_DISPLAY"
    cs.addDisplayItem(itemName, urlString)
    if (log.debugEnabled) log.debug "performDisplayMethod: ${itemName} = ${urlString}"
    return cs
  }

  TdbCase performRestMethod(TdbSuite suite, TdbDrill drill, TdbCase cs) {
    if (log.debugEnabled) log.debug "performRestMethod << ${cs? cs : 'Case(null)'}"
    def urlString = null
    try {
      urlString = TdbMethod.createUrl(drill, cs)
    } catch (MissingPropertyException exc) {
      throw new ServiceException("Value missing for variable '${exc.property}'")
    }

    if (log.debugEnabled) log.debug "HTTP ${drill?.verb} [[ ${urlString}"
    // There is also a pretty DSL format that we don't use...
    def builder = new RequestBuilder()
    builder.uri = urlString
    // Configure relative the invocation mode
    builder = modeConfig(builder, drill)

    try {
      cs = doPerform(builder, suite, drill, cs)
    } catch (ResponseStatusException exc) {
      if (log.debugEnabled) log.debug "HTTP ${exc.status} ]] ${exc.content}"
      cs = suite.createCase(cs)
      cs.addTextItem("${drill.name}_HTTP_STATUS", String.valueOf(exc.status))
      cs.addTextItem("${drill.name}_EXCEPTION", exc.message, true)
      def content = exc.content
      if (content instanceof Map) {
	cs = storeTextResultMap(suite, drill, cs, content)
      } else {
	cs.addTextItem("${drill.name}_RESULT", String.valueOf(exc.content))
      }
      cs.save()
    } catch (Exception exc) {
      if (log.debugEnabled) log.debug "HTTP EXC ]] ${exc}"
      cs = suite.createCase(cs)
      cs.addTextItem("${drill.name}_EXCEPTION", exc.message, true)
      cs.save()
    }

    if (log.debugEnabled) log.debug "perform >> ${cs}"
    return cs
  }

  private RequestBuilder modeConfig(RequestBuilder builder, TdbDrill drill) {
    switch (drill.mode.id) {
    case TdbMode.PARSE_MODE_ID:
    // Default configuration
    break
    case TdbMode.TEXT_MODE_ID:
      // Do not parse JSON or XML
      builder.convertJson = false
      builder.convertXML = false
    break
    case TdbMode.BINARY_MODE_ID:
      // This option overrides the parsing options
      builder.binaryResponse = true
    break
    default:
      def msg = "Drill contains invalid mode ${drill?.mode?.id}"
      throw new ServiceException(msg)
    }

    return builder
  }

  private TdbCase doPerform(RequestBuilder builder,
			    TdbSuite suite, TdbDrill drill, TdbCase cs)
  {
    def result = null
    def nanos = System.nanoTime()
    // Perform the invocation
    switch (drill.verb.id) {
    case TdbHttpVerb.GET_ID:
      result = builder.get()
    break
    case TdbHttpVerb.PUT_ID:
      result = builder.put()
    break
    case TdbHttpVerb.POST_ID:
      builder.body = drill.body
      result = builder.post()
    break
    default:
      def msg = "Drill contains invalid verb ${drill?.verb?.id}"
      throw new ServiceException(msg)
    }

    nanos = System.nanoTime() - nanos
    // Pick up the result
    String msg = null
    switch (drill.mode.id) {
    case TdbMode.PARSE_MODE_ID:
      msg = "${result?.size()} entries"
      cs = storeTextResultMap(suite, drill, cs, result)
    break
    case TdbMode.TEXT_MODE_ID:
      cs = suite.createCase(cs)
      msg = "${result.size()} characters"
      cs.addTextItem("${drill.name}_result", result)
      cs.save()
    break
    case TdbMode.BINARY_MODE_ID:
      cs = suite.createCase(cs)
      msg = "${result.size()} bytes"
      cs.addBinaryItem("${drill.name}_RESULT", result)
      cs.save()
    break
    default:
      msg = "Drill contains invalid mode ${drill?.mode?.id}"
      throw new ServiceException(msg)
    }

    // Record elapsed milliseconds
    if (cs) {
      cs.addTextItem("${drill.name}_MILLIS", String.format('%01.3g', nanos/1000000.0d))
    }

    if (log.debugEnabled) log.debug "perform ]] ${msg}"
    return cs
  }

  private storeTextResultMap(TdbSuite suite, TdbDrill drill, TdbCase cs, result) {
    cs = suite.createCase(cs)
    result.each {entry ->
      String name = "${drill.name}_${entry.key}"
      cs.addTextItem(name, entry.value as String)
    }

    cs.save()
  }

}
