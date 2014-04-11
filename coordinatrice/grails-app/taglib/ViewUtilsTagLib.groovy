/**
 * Various view utility tags
 */ 
class ViewUtilsTagLib {
  static final FMTM = 'yyyy-MM-dd HH:mm'
  static final FMTS = 'yyyy-MM-dd HH:mm:ss'

  /**
   * Tag for displaying the instance boolean in PxdItem
   */
  def instflag = {attrs, body ->
    out << (attrs.flag? 'Inst' : 'Def')
  }

  /**
   * Tag for displaying an abbreviated string
   */
  def abbr = {attrs, body ->
    def str = attrs.text
    out << ((str.length() > 18)? "${str[0..4]}...${str[-5..-1]}" : str)
  }

  /**
   * Tag for displaying process definition state
   */
  def pdefstate = {attrs, body ->
    def state = attrs.state
    out << message(code: state?.res, default: state?.defaultMessage)
  }

  /**
   * Tag for displaying task types
   */
  def tasktype = {attrs, body ->
    def type = attrs.type
    out << message(code: type?.res, default: type?.defaultMessage)
  }

  /***** From Migratrice *****/

  /**
   * Tag for displaying MigPackage.originLocal
   */
  def formatOrigin = {attrs, body ->
    def flag = attrs.flag
    out << (flag? g.message(code: 'migPackage.originIsLocal.label') :
	    g.message(code: 'migPackage.originIsRemote.label'))
  }

  /**
   * Tag for displaying MigFormdefVer.published
   */
  def formatPublished = {attrs, body ->
    def flag = attrs.flag
    out << (flag? g.message(code: 'migFormdefVer.ispublished.label') :
	    g.message(code: 'migFormdefVer.unpublished.label'))
  }

  /**
   * Tag for displaying Date with minute precision
   */
  def tstamp = {attrs, body ->
    def date = attrs.date
    out << date.format(FMTM)
  }

  /**
   * Tag for displaying Date with second precision
   */
  def tstampsec = {attrs, body ->
    def date = attrs.date
    out << date.format(FMTS)
  }

}
