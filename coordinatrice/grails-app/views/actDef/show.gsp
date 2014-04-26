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
<%@ page import="org.motrice.coordinatrice.ActDef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'actDef.label', default: 'ActDef')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-actDef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="show-actDef" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list actDef">
	<g:if test="${actDefInst?.name}">
	  <li class="fieldcontain">
	    <span id="name-label" class="property-label"><g:message code="actDef.name.label" default="Name" /></span>
	    <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${actDefInst}" field="name"/>
	      (<g:fieldValue bean="${actDefInst}" field="uuid"/>)
	    </span>
	  </li>
	</g:if>
	<g:if test="${actDefInst?.type}">
	  <li class="fieldcontain">
	    <span id="type-label" class="property-label"><g:message code="actDef.type.label" default="Type" /></span>
	    <span class="property-value" aria-labelledby="type-label"><g:tasktype type="${actDefInst?.type}"/></span>
	  </li>
	</g:if>
	<g:if test="${actDefInst?.process}">
	  <li class="fieldcontain">
	    <span id="process-label" class="property-label"><g:message code="actDef.process.label" default="Process" /></span>
	    <span class="property-value" aria-labelledby="process-label"><g:link controller="procdef" action="show" id="${actDefInst?.process?.uuid}">${actDefInst?.process?.encodeAsHTML()}</g:link></span>
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${actDefInst?.id}" />
	  <g:link class="edit" action="edit" id="${actDefInst?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
