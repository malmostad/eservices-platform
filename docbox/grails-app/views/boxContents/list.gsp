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

<%@ page import="org.motrice.docbox.doc.BoxContents" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'boxContents.label', default: 'BoxContents')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-boxContents" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="list-boxContents" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    
	    <g:sortableColumn property="name" title="${message(code: 'boxContents.name.label', default: 'Name')}" />
	    
	    <g:sortableColumn property="dateCreated" title="${message(code: 'boxContents.dateCreated.label', default: 'Date Created')}" />
	    
	    <g:sortableColumn property="lastUpdated" title="${message(code: 'boxContents.lastUpdated.label', default: 'Last Updated')}" />
	    
	    <g:sortableColumn property="format" title="${message(code: 'boxContents.format.label', default: 'Format')}" />
	    
	    <g:sortableColumn property="size" title="${message(code: 'boxContents.size.label', default: 'Size')}" />
	    
	    <g:sortableColumn property="text" title="${message(code: 'boxContents.text.label', default: 'Text')}" />
	    
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${boxContentsObjList}" status="i" var="boxContentsObj">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      
	      <td><g:link action="show" id="${boxContentsObj.id}">${fieldValue(bean: boxContentsObj, field: "name")}</g:link></td>
	      
	      <td><g:formatDate date="${boxContentsObj.dateCreated}" /></td>
	      
	      <td><g:formatDate date="${boxContentsObj.lastUpdated}" /></td>
	      
	      <td>${fieldValue(bean: boxContentsObj, field: "format")}</td>
	      
	      <td>${fieldValue(bean: boxContentsObj, field: "size")}</td>
	      
	      <td>${fieldValue(bean: boxContentsObj, field: "text")}</td>
	      
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${boxContentsObjTotal}" />
      </div>
    </div>
  </body>
</html>
