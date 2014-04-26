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
<%@ page import="org.motrice.coordinatrice.Procdef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'procdef.label', default: 'Procdef')}" />
      <g:set var="categEntityName" value="${message(code: 'crdProcCategory.label', default: 'CrdProcCategory')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-procdef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="list-procdef" class="content scaffold-list" role="main">
      <g:if test="${deploymentId}">
	<g:set var="deplId" value="${deploymentId}"/>
	<h1><g:message code="procdef.deployment.list.label" args="[deplId]" /></h1>
      </g:if>
      <g:else>
	<h1><g:message code="procdef.versions.list.label" args="[procdefKey]" /></h1>
      </g:else>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <g:render template="procdeftable"/>
      <div class="pagination">&nbsp;
	<g:paginate id="${procdefKey}" total="${procdefInstTotal}" />
	<g:if test="${deleteEnabled}">
	  <g:link class="menu-left delete" action="deletionconfirm" id="${deleteEnabled}">
	    <g:message code="procdef.deletion.proceed.button.label" default="Deletion"/>
	  </g:link>
	</g:if>
	<g:else>
	<g:link class="menu-left delete" action="listdeletion" id="${procdefKey}" params="[max:params.max,offset:params.offset]">
	  <g:message code="procdef.deletion.label" default="Deletion"/>
	</g:link>
	</g:else>
      </div>
    </div>
  </body>
</html>
