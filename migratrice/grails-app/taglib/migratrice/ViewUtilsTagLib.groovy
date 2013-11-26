package migratrice

/**
 * Various view utility tags
 */ 
class ViewUtilsTagLib {
  static final FMT = 'yyyy-MM-dd HH:mm'

  /**
   * Tag for displaying an abbreviated string
   */
  def abbr = {attrs, body ->
    def str = attrs.text
    out << ((str.length() > 18)? "${str[0..4]}...${str[-5..-1]}" : str)
  }

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
    out << date.format(FMT)
  }

}
