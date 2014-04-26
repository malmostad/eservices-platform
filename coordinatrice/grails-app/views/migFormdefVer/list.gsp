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
<%@ page import="org.motrice.migratrice.MigFormdefVer" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'migFormdefVer.label', default: 'MigFormdefVer')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-migFormdefVer" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="list-migFormdefVer" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="app" title="${message(code: 'migFormdefVer.app.label', default: 'App')}" />
	    <g:sortableColumn property="form" title="${message(code: 'migFormdefVer.form.label', default: 'Form')}" />
	    <g:sortableColumn property="path" title="${message(code: 'migFormdefVer.path.label', default: 'Path')}" />
	    <g:sortableColumn property="draft" title="${message(code: 'migFormdefVer.draft.label', default: 'Draft')}" />
	    <g:sortableColumn property="published" title="${message(code: 'migFormdefVer.published.label', default: 'Published')}" />
	    <g:sortableColumn property="title" title="${message(code: 'migFormdefVer.title.label', default: 'Title')}" />
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${migFormdefVerInstList}" status="i" var="migFormdefVerInst">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:link action="show" id="${migFormdefVerInst.id}">${fieldValue(bean: migFormdefVerInst, field: "app")}</g:link></td>
	      <td>${fieldValue(bean: migFormdefVerInst, field: "form")}</td>
	      <td>${fieldValue(bean: migFormdefVerInst, field: "path")}</td>
	      <td>${fieldValue(bean: migFormdefVerInst, field: "draft")}</td>
	      <td><g:formatBoolean boolean="${migFormdefVerInst.published}" /></td>
	      <td>${fieldValue(bean: migFormdefVerInst, field: "title")}</td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${migFormdefVerInstTotal}" />
      </div>
    </div>
  </body>
</html>
