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
<%@ page import="org.motrice.migratrice.MigPackage" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'migPackage.label', default: 'MigPackage')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-migPackage" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="list-migPackage" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="siteName" title="${message(code: 'migPackage.siteName.label', default: 'Site Name')}" />
	    <g:sortableColumn property="packageName" title="${message(code: 'migPackage.packageName.label', default: 'Package Name')}" />
	    <g:sortableColumn property="originLocal" title="${message(code: 'migPackage.originLocal.label', default: 'Origin Local')}" />
	    <g:sortableColumn property="siteTstamp" title="${message(code: 'migPackage.siteTstamp.label', default: 'Site Tstamp')}" />
	    <g:sortableColumn property="packageFormat" title="${message(code: 'migPackage.packageFormat.label', default: 'Package Format')}" />
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${migPackageInstList}" status="i" var="migPackageInst">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td>${fieldValue(bean: migPackageInst, field: "siteName")}</td>
	      <td><g:link action="show" id="${migPackageInst.id}">${fieldValue(bean: migPackageInst, field: "packageName")}</g:link></td>
	      <td><g:formatOrigin flag="${migPackageInst.originLocal}" /></td>
	      <td><g:tstamp date="${migPackageInst.siteTstamp}"/></td>
	      <td>${fieldValue(bean: migPackageInst, field: "packageFormat")}</td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${migPackageInstTotal}" />
      </div>
    </div>
  </body>
</html>
