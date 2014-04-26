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
<%@ page import="org.motrice.migratrice.MigItem" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'migItem.label', default: 'MigItem')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-migItem" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="show-migItem" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list migItem">
	<g:if test="${migItemInst?.path}">
	  <li class="fieldcontain">
	    <span id="path-label" class="property-label"><g:message code="migItem.path.label" default="Path" /></span>
	    <span class="property-value" aria-labelledby="path-label"><g:fieldValue bean="${migItemInst}" field="path"/></span>
	  </li>
	</g:if>
	<g:if test="${migItemInst?.ref}">
	  <li class="fieldcontain">
	    <span id="ref-label" class="property-label"><g:message code="migItem.ref.label" default="Ref" /></span>
	    <span class="property-value" aria-labelledby="ref-label"><g:fieldValue bean="${migItemInst}" field="ref"/></span>
	  </li>
	</g:if>
	<g:if test="${migItemInst?.uuid}">
	  <li class="fieldcontain">
	    <span id="uuid-label" class="property-label"><g:message code="migItem.uuid.label" default="Uuid" /></span>
	    <span class="property-value" aria-labelledby="uuid-label"><g:fieldValue bean="${migItemInst}" field="uuid"/></span>
	  </li>
	</g:if>
	<g:if test="${migItemInst?.formpath}">
	  <li class="fieldcontain">
	    <span id="formpath-label" class="property-label"><g:message code="migItem.formpath.label" default="Form Path" /></span>
	    <span class="property-value" aria-labelledby="formpath-label"><g:fieldValue bean="${migItemInst}" field="formpath"/></span>
	  </li>
	</g:if>
	<g:if test="${migItemInst?.format}">
	  <li class="fieldcontain">
	    <span id="format-label" class="property-label"><g:message code="migItem.format.label" default="Format" /></span>
	    <span class="property-value" aria-labelledby="format-label"><g:fieldValue bean="${migItemInst}" field="format"/></span>
	  </li>
	</g:if>
	<g:if test="${migItemInst?.size}">
	  <li class="fieldcontain">
	    <span id="size-label" class="property-label"><g:message code="migItem.size.label" default="Size" /></span>
	    <span class="property-value" aria-labelledby="size-label"><g:fieldValue bean="${migItemInst}" field="size"/></span>
	  </li>
	</g:if>
	<g:if test="${migItemInst?.sha1}">
	  <li class="fieldcontain">
	    <span id="sha1-label" class="property-label"><g:message code="migItem.sha1.label" default="Sha1" /></span>
	    <span class="property-value" aria-labelledby="sha1-label"><g:fieldValue bean="${migItemInst}" field="sha1"/></span>
	  </li>
	</g:if>
	<g:if test="${migItemInst?.created}">
	  <li class="fieldcontain">
	    <span id="created-label" class="property-label"><g:message code="migItem.created.label" default="Created" /></span>
	    <span class="property-value" aria-labelledby="created-label"><g:tstamp date="${migItemInst?.created}" /></span>
	  </li>
	</g:if>
	<g:if test="${migItemInst?.formdef}">
	  <li class="fieldcontain">
	    <span id="formdef-label" class="property-label"><g:message code="migItem.formdef.label" default="Formdef" /></span>
	    <span class="property-value" aria-labelledby="formdef-label"><g:link controller="migFormdef" action="show" id="${migItemInst?.formdef?.id}">${migItemInst?.formdef?.display()?.encodeAsHTML()}</g:link></span>
	  </li>
	</g:if>
	<g:if test="${migItemInst?.pack}">
	  <li class="fieldcontain">
	    <span id="pack-label" class="property-label"><g:message code="migItem.pack.label" default="Pack" /></span>
	    <span class="property-value" aria-labelledby="pack-label"><g:link controller="migPackage" action="show" id="${migItemInst?.pack?.id}">${migItemInst?.pack?.display()?.encodeAsHTML()}</g:link></span>
	  </li>
	</g:if>
      </ol>
    </div>
  </body>
</html>
