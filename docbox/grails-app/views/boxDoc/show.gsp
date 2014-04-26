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
<%@ page import="org.motrice.docbox.doc.BoxDoc" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'boxDoc.label', default: 'BoxDoc')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-boxDoc" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
	<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="show-boxDoc" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list boxDoc">
	<g:if test="${boxDocObj?.docNo}">
	  <li class="fieldcontain">
	    <span id="docNo-label" class="property-label"><g:message code="boxDoc.docNo.label" default="Doc No" /></span>
	    <span class="property-value" aria-labelledby="docNo-label"><g:fieldValue bean="${boxDocObj}" field="docNo"/></span>
	  </li>
	</g:if>
	<g:if test="${boxDocObj?.formDataUuid}">
	  <li class="fieldcontain">
	    <span id="formDataUuid-label" class="property-label"><g:message code="boxDoc.formDataUuid.label" default="Form Data Uuid" /></span>
	    <span class="property-value" aria-labelledby="formDataUuid-label"><g:fieldValue bean="${boxDocObj}" field="formDataUuid"/></span>
	  </li>
	</g:if>
	<g:if test="${boxDocObj?.dateCreated}">
	  <li class="fieldcontain">
	    <span id="dateCreated-label" class="property-label"><g:message code="boxDoc.dateCreated.label" default="Date Created" /></span>
	    <span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${boxDocObj?.dateCreated}" /></span>
	  </li>
	</g:if>
	<g:if test="${boxDocObj?.lastUpdated}">
	  <li class="fieldcontain">
	    <span id="lastUpdated-label" class="property-label"><g:message code="boxDoc.lastUpdated.label" default="Last Updated" /></span>
	    <span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${boxDocObj?.lastUpdated}" /></span>
	  </li>
	</g:if>
	<g:if test="${boxDocObj?.steps}">
	  <li class="fieldcontain">
	    <span id="steps-label" class="property-label"><g:message code="boxDoc.steps.label" default="Steps" /></span>
	    <g:each in="${boxDocObj.steps}" var="s">
	      <span class="property-value" aria-labelledby="steps-label"><g:link controller="boxDocStep" action="show" id="${s.id}">${s?.display()?.encodeAsHTML()}</g:link></span>
	    </g:each>
	    
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${boxDocObj?.id}" />
	  <g:link class="edit" action="edit" id="${boxDocObj?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
	  <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
