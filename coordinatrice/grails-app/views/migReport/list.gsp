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
<%@ page import="org.motrice.migratrice.MigReport" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'migReport.label', default: 'MigReport')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-migReport" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="list-migReport" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    
	    <g:sortableColumn property="body" title="${message(code: 'migReport.body.label', default: 'Body')}" />
	    
	    <th><g:message code="migReport.pkg.label" default="Pkg" /></th>
	    
	    <g:sortableColumn property="tstamp" title="${message(code: 'migReport.tstamp.label', default: 'Tstamp')}" />
	    
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${migReportInstList}" status="i" var="migReportInst">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      
	      <td><g:link action="show" id="${migReportInst.id}">${fieldValue(bean: migReportInst, field: "body")}</g:link></td>
	      
	      <td>${fieldValue(bean: migReportInst, field: "pkg")}</td>
	      
	      <td><g:formatDate date="${migReportInst.tstamp}" /></td>
	      
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${migReportInstTotal}" />
      </div>
    </div>
  </body>
</html>
