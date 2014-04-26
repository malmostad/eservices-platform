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
<%@ page import="org.motrice.coordinatrice.MtfStartFormDefinition" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'mtfStartFormDefinition.label', default: 'MtfStartFormDefinition')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-mtfStartFormDefinition" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="show-mtfStartFormDefinition" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list mtfStartFormDefinition">
	
	<g:if test="${startFormdefInst?.authTypeReq}">
	  <li class="fieldcontain">
	    <span id="authTypeReq-label" class="property-label"><g:message code="mtfStartFormDefinition.authTypeReq.label" default="Auth Type Req" /></span>
	    <span class="property-value" aria-labelledby="authTypeReq-label"><g:fieldValue bean="${startFormdefInst}" field="authTypeReq"/></span>
	  </li>
	</g:if>
	<g:if test="${startFormdefInst?.formConnectionKey}">
	  <li class="fieldcontain">
	    <span id="formConnectionKey-label" class="property-label"><g:message code="mtfStartFormDefinition.formConnectionKey.label" default="Form Path" /></span>
	    <span class="property-value" aria-labelledby="formConnectionKey-label"><g:fieldValue bean="${startFormdefInst}" field="formConnectionKey"/></span>
	  </li>
	</g:if>
	<g:if test="${startFormdefInst?.procdefId}">
	  <li class="fieldcontain">
	    <span id="procdefId-label" class="property-label"><g:message code="mtfStartFormDefinition.procdefId.label" default="Process Definition Uuid" /></span>
	    <span class="property-value" aria-labelledby="procdefId-label"><g:fieldValue bean="${startFormdefInst}" field="procdefId"/></span>
	  </li>
	</g:if>
	<g:if test="${startFormdefInst?.userDataXpath}">
	  <li class="fieldcontain">
	    <span id="userDataXpath-label" class="property-label"><g:message code="mtfStartFormDefinition.userDataXpath.label" default="User Data Xpath" /></span>
	    <span class="property-value" aria-labelledby="userDataXpath-label"><g:fieldValue bean="${startFormdefInst}" field="userDataXpath"/></span>
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${startFormdefInst?.id}" />
	  <g:link class="edit" action="edit" id="${startFormdefInst?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
	  <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
