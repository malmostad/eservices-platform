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
    out << ((str.length() > 40)? "${str[0..4]}...${str[-5..-1]}" : str)
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
