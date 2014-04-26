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
<%@ page import="org.motrice.coordinatrice.pxd.PxdFormdefVer" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'pxdFormdefVer.label', default: 'PxdFormdefVer')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-pxdFormdefVer" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="show-pxdFormdefVer" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list pxdFormdefVer">
	<g:if test="${pxdFormdefVerInst?.path}">
	  <li class="fieldcontain">
	    <span id="path-label" class="property-label"><g:message code="pxdFormdefVer.path.label" default="Path" /></span>
	    <span class="property-value" aria-labelledby="path-label"><g:fieldValue bean="${pxdFormdefVerInst}" field="path"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefVerInst?.title}">
	  <li class="fieldcontain">
	    <span id="title-label" class="property-label"><g:message code="pxdFormdefVer.title.label" default="Title" /></span>
	    <span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${pxdFormdefVerInst}" field="title"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefVerInst?.appName}">
	  <li class="fieldcontain">
	    <span id="appName-label" class="property-label"><g:message code="pxdFormdefVer.appName.label" default="App Name" /></span>
	    <span class="property-value" aria-labelledby="appName-label"><g:fieldValue bean="${pxdFormdefVerInst}" field="appName"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefVerInst?.formName}">
	  <li class="fieldcontain">
	    <span id="formName-label" class="property-label"><g:message code="pxdFormdefVer.formName.label" default="Form Name" /></span>
	    <span class="property-value" aria-labelledby="formName-label"><g:fieldValue bean="${pxdFormdefVerInst}" field="formName"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefVerInst?.fvno}">
	  <li class="fieldcontain">
	    <span id="fvno-label" class="property-label"><g:message code="pxdFormdefVer.fvno.label" default="Fvno" /></span>
	    <span class="property-value" aria-labelledby="fvno-label"><g:fieldValue bean="${pxdFormdefVerInst}" field="fvno"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefVerInst?.draft && pxdFormdefVerInst.draft < 9999}">
	  <li class="fieldcontain">
	    <span id="draft-label" class="property-label"><g:message code="pxdFormdefVer.draft.label" default="Draft" /></span>
	    <span class="property-value" aria-labelledby="draft-label"><g:fieldValue bean="${pxdFormdefVerInst}" field="draft"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefVerInst?.description}">
	  <li class="fieldcontain">
	    <span id="description-label" class="property-label"><g:message code="pxdFormdefVer.description.label" default="Description" /></span>
	    <span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${pxdFormdefVerInst}" field="description"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefVerInst?.language}">
	  <li class="fieldcontain">
	    <span id="language-label" class="property-label"><g:message code="pxdFormdefVer.language.label" default="Language" /></span>
	    <span class="property-value" aria-labelledby="language-label"><g:fieldValue bean="${pxdFormdefVerInst}" field="language"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefVerInst?.formdef}">
	  <li class="fieldcontain">
	    <span id="formdef-label" class="property-label"><g:message code="pxdFormdefVer.formdef.label" default="Formdef" /></span>
	    <span class="property-value" aria-labelledby="formdef-label"><g:link controller="pxdFormdef" action="show" id="${pxdFormdefVerInst?.formdef?.id}">${pxdFormdefVerInst?.formdef?.encodeAsHTML()}</g:link></span>
	  </li>
	</g:if>
	
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${pxdFormdefVerInst?.id}" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
