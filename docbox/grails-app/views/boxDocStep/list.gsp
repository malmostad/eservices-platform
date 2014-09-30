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

<%@ page import="org.motrice.docbox.doc.BoxDocStep" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'boxDocStep.label', default: 'BoxDocStep')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-boxDocStep" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="list-boxDocStep" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    
	    <g:sortableColumn property="step" title="${message(code: 'boxDocStep.step.label', default: 'Step')}" />
	    
	    <g:sortableColumn property="docNo" title="${message(code: 'boxDocStep.docNo.label', default: 'Doc No')}" />
	    
	    <g:sortableColumn property="signCount" title="${message(code: 'boxDocStep.signCount.label', default: 'Sign Count')}" />
	    
	    <g:sortableColumn property="dateCreated" title="${message(code: 'boxDocStep.dateCreated.label', default: 'Date Created')}" />
	    
	    <g:sortableColumn property="lastUpdated" title="${message(code: 'boxDocStep.lastUpdated.label', default: 'Last Updated')}" />
	    
	    <th><g:message code="boxDocStep.doc.label" default="Doc" /></th>
	    
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${boxDocStepObjList}" status="i" var="boxDocStepObj">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      
	      <td><g:link action="show" id="${boxDocStepObj.id}">${fieldValue(bean: boxDocStepObj, field: "step")}</g:link></td>
	      
	      <td>${fieldValue(bean: boxDocStepObj, field: "docNo")}</td>
	      
	      <td>${fieldValue(bean: boxDocStepObj, field: "signCount")}</td>
	      
	      <td><g:formatDate date="${boxDocStepObj.dateCreated}" /></td>
	      
	      <td><g:formatDate date="${boxDocStepObj.lastUpdated}" /></td>
	      
	      <td>${fieldValue(bean: boxDocStepObj, field: "doc")}</td>
	      
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${boxDocStepObjTotal}" />
      </div>
    </div>
  </body>
</html>
