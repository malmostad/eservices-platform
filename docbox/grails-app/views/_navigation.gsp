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
    <li><a href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a>
      <ul class="submenu">
	<li><g:link controller="procdef" action="showconfig"><g:message code="config.show.menu"/></g:link></li>
      </ul>
    </li>
    <li><g:link controller="procdef" action="list"><g:message code="procdef.menu"/></g:link>
	<ul class="submenu">
	  <li><g:link controller="procdef" action="list"><g:message code="procdef.list.menu"/></g:link></li>
	  <li><g:link controller="procdef" action="create"><g:message code="procdef.upload.bpmn.label"/></g:link></li>
	  <li><g:link controller="crdProcCategory" action="list"><g:message code="crdProcCategory.list.menu"/></g:link></li>
	  <li><g:link class="create" action="create"><g:message code="crdProcCategory.create.menu"/></g:link></li>
	</ul>
    </li>
    <li><g:link controller="mtfStartFormDefinition" action="list"><g:message code="pxdFormdef.menu"/></g:link>
	<ul class="submenu">
	  <li><g:link controller="mtfStartFormDefinition" action="list"><g:message code="startform.list.label"/></g:link></li>
	  <li><g:link controller="pxdFormdef" action="list"><g:message code="pxdFormdef.list.label"/></g:link></li>
	  <li><a class="create" href="${orbeonUri}/new" target="_"><g:message code="pxdFormdef.create.menu"/></a></li>
	</ul>
    </li>
    <li><g:link controller="migPackage" action="list"><g:message code="migration.menu"/></g:link>
	<ul class="submenu">
	  <li><g:link controller="migPackage" action="list"><g:message code="migPackage.list.menu"/></g:link></li>
	  <li><g:link controller="migPackage" action="uploadprepare"><g:message code="migPackage.upload.menu"/></g:link></li>
	  <li><g:link controller="migPackage" action="listexp"><g:message code="migPackage.create.menu"/></g:link></li>
	</ul>
    </li>
  </ul>
</div>
