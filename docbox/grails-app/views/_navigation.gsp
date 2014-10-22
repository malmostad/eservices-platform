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
    <li><a href="${createLink(uri: '/')}"><g:message code="boxDoc.menu"/></a>
      <ul class="submenu">
	<li><g:link controller="boxDoc" action="list"><g:message code="boxDoc.list.menu"/></g:link></li>
      </ul>
    </li>
    <li><g:link controller="sigResult" action="list"><g:message code="signatures.menu"/></g:link>
	<ul class="submenu">
	  <li><g:link controller="sigService" action="list"><g:message code="sigService.list.menu"/></g:link></li>
	  <li><g:link controller="sigService" action="create"><g:message code="sigService.create.menu"/></g:link></li>
	  <li><g:link controller="sigScheme" action="list"><g:message code="sigScheme.list.menu"/></g:link></li>
	  <li><g:link controller="sigScheme" action="create"><g:message code="sigScheme.create.menu"/></g:link></li>
	  <li><g:link controller="sigDefaultScheme" action="show" id="1"><g:message code="sigDefaultScheme.show.menu"/></g:link></li>
	  <li><g:link controller="sigTestcase" action="list"><g:message code="sigTestcase.list.menu"/></g:link></li>
	  <li><g:link controller="sigTestcase" action="create"><g:message code="sigTestcase.create.menu"/></g:link></li>
	  <li><g:link controller="sigResult" action="list"><g:message code="sigResult.list.menu"/></g:link></li>
	</ul>
    </li>
    <li><g:link controller="auditRecord" action="list"><g:message code="auditRecord.main.menu"/></g:link>
	<ul class="submenu">
	  <li><g:link controller="auditRecord" action="list"><g:message code="auditRecord.list.menu"/></g:link></li>
	</ul>
    </li>
    <li><g:link controller="env" action="showconfig"><g:message code="config.menu"/></g:link>
	<ul class="submenu">
	  <li><g:link controller="env" action="showconfig"><g:message code="config.show.menu"/></g:link></li>
	</ul>
    </li>
  </ul>
</div>
