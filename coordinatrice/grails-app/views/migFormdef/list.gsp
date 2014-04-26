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
<%@ page import="org.motrice.migratrice.MigFormdef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'migFormdef.label', default: 'MigFormdef')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-migFormdef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="list-migFormdef" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <g:form method="post" >
	<table>
	  <thead>
	    <tr>
	      <th>Export</th>
	      <g:sortableColumn property="app" title="${message(code: 'migFormdef.app.label', default: 'App')}" />
	      <g:sortableColumn property="form" title="${message(code: 'migFormdef.form.label', default: 'Form')}" />
	      <g:sortableColumn property="currentDraft" title="${message(code: 'migFormdef.currentDraft.label', default: 'Current Draft')}" />
	      <g:sortableColumn property="created" title="${message(code: 'migFormdef.created.label', default: 'Created')}" />
	      <g:sortableColumn property="uuid" title="${message(code: 'migFormdef.uuid.label', default: 'Uuid')}" />
	    </tr>
	  </thead>
	  <tbody>
	    <g:each in="${migFormdefInstList}" status="i" var="migFormdefInst">
	      <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
		<td><g:checkBox name="chb_${migFormdefInst?.path}" value="${false}"/></td>
		<td>${fieldValue(bean: migFormdefInst, field: "app")}</td>
		<td>${fieldValue(bean: migFormdefInst, field: "form")}</td>
		<td>${fieldValue(bean: migFormdefInst, field: "currentDraft")}</td>
		<td><g:tstamp date="${migFormdefInst.created}" /></td>
		<td><g:abbr text="${migFormdefInst?.uuid}"/></td>
	      </tr>
	    </g:each>
	  </tbody>
	</table>
	<fieldset class="buttons">
	  <g:actionSubmit class="save" action="export" value="${message(code: 'default.button.update.label', default: 'Update')}"/>
	  <span class="checkall">Check all</span>
	  <span class="clearall">Clear all</span>
	  <span class="add">Add containing</span>
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
