<%-- == Motrice Copyright Notice ==

  Motrice Service Platform

  Copyright (C) 2011-2014 Motrice AB

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU Affero General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU Affero General Public License for more details.

  You should have received a copy of the GNU Affero General Public License
  along with this program. If not, see <http://www.gnu.org/licenses/>.

  e-mail: info _at_ motrice.se
  mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN
  phone: +46 8 641 64 14

--%>
<div class="mainmenu" role="navigation">
  <ul id="menu">
    <li><a href="${createLink(uri: '/')}"><g:message code="tdbSuite.list.menu"/></a>
      <ul class="submenu">
	<li><g:link controller="tdbHttpVerb" action="list"><g:message code="tdbHttpVerb.list.menu"/></g:link></li>
	<li><g:link controller="tdbMethod" action="list"><g:message code="tdbMethod.list.menu"/></g:link></li>
	<li><g:link controller="tdbMode" action="list"><g:message code="tdbMode.list.menu"/></g:link></li>
	<li><g:link controller="tdbProperty" action="list"><g:message code="tdbProperty.list.menu"/></g:link></li>
      </ul>
    </li>
  </ul>
</div>
