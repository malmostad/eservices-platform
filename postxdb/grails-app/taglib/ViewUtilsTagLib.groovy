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
}
