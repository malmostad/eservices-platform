/**
 * Various view utility tags
 */ 
class ViewUtilsTagLib {
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
}
