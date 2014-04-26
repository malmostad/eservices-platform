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
<%@ page import="org.motrice.migratrice.MigItem" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'migItem.label', default: 'MigItem')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-migItem" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="list-migItem" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="formref" title="${message(code: 'migItem.formref.label', default: 'Formref')}" />
	    <g:sortableColumn property="path" title="${message(code: 'migItem.path.label', default: 'Path')}" />
	    <g:sortableColumn property="uuid" title="${message(code: 'migItem.uuid.label', default: 'Uuid')}" />
	    <g:sortableColumn property="formDef" title="${message(code: 'migItem.formDef.label', default: 'Form Def')}" />
	    <g:sortableColumn property="format" title="${message(code: 'migItem.format.label', default: 'Format')}" />
	    <g:sortableColumn property="size" title="${message(code: 'migItem.size.label', default: 'Size')}" />
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${migItemInstList}" status="i" var="migItemInst">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:link action="show" id="${migItemInst.id}">${fieldValue(bean: migItemInst, field: "formref")}</g:link></td>
	      <td>${fieldValue(bean: migItemInst, field: "path")}</td>
	      <td>${fieldValue(bean: migItemInst, field: "uuid")}</td>
	      <td>${fieldValue(bean: migItemInst, field: "formDef")}</td>
	      <td>${fieldValue(bean: migItemInst, field: "format")}</td>
	      <td>${fieldValue(bean: migItemInst, field: "size")}</td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${migItemInstTotal}" />
      </div>
    </div>
  </body>
</html>
