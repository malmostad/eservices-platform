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
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-migFormdef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="show-migFormdef" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list migFormdef">
	<g:if test="${migFormdefInst?.app}">
	  <li class="fieldcontain">
	    <span id="app-label" class="property-label"><g:message code="migFormdef.app.label" default="App" /></span>
	    <span class="property-value" aria-labelledby="app-label"><g:fieldValue bean="${migFormdefInst}" field="app"/></span>
	  </li>
	</g:if>
	<g:if test="${migFormdefInst?.form}">
	  <li class="fieldcontain">
	    <span id="form-label" class="property-label"><g:message code="migFormdef.form.label" default="Form" /></span>
	    <span class="property-value" aria-labelledby="form-label"><g:fieldValue bean="${migFormdefInst}" field="form"/></span>
	  </li>
	</g:if>
	<g:if test="${migFormdefInst?.uuid}">
	  <li class="fieldcontain">
	    <span id="uuid-label" class="property-label"><g:message code="migFormdef.uuid.label" default="Uuid" /></span>
	    <span class="property-value" aria-labelledby="uuid-label"><g:fieldValue bean="${migFormdefInst}" field="uuid"/></span>
	  </li>
	</g:if>
	<g:if test="${migFormdefInst?.ref}">
	  <li class="fieldcontain">
	    <span id="ref-label" class="property-label"><g:message code="migFormdef.ref.label" default="Ref" /></span>
	    <span class="property-value" aria-labelledby="ref-label"><g:fieldValue bean="${migFormdefInst}" field="ref"/></span>
	  </li>
	</g:if>
	<g:if test="${migFormdefInst?.currentDraft}">
	  <li class="fieldcontain">
	    <span id="currentDraft-label" class="property-label"><g:message code="migFormdef.currentDraft.label" default="Current Draft" /></span>
	    <span class="property-value" aria-labelledby="currentDraft-label"><g:fieldValue bean="${migFormdefInst}" field="currentDraft"/></span>
	  </li>
	</g:if>
	<g:if test="${migFormdefInst?.created}">
	  <li class="fieldcontain">
	    <span id="created-label" class="property-label"><g:message code="migFormdef.createdupdated.label" default="Created/Updated" /></span>
	    <span class="property-value" aria-labelledby="created-label"><g:tstamp date="${migFormdefInst?.created}"/> /
	      <g:tstamp date="${migFormdefInst?.updated}"/></span>
	  </li>
	</g:if>
	<g:if test="${migFormdefInst?.versions}">
	  <li class="fieldcontain">
	    <span id="versions-label" class="property-label"><g:message code="migFormdef.versions.label" default="Versions" /></span>
	    <g:each in="${migFormdefInst.versions}" var="f">
	      <span class="property-value" aria-labelledby="versions-label"><g:link controller="migFormdefVer" action="show" id="${f.id}">${f?.display()?.encodeAsHTML()}</g:link></span>
	    </g:each>
	  </li>
	</g:if>
	<g:if test="${migFormdefInst?.items}">
	  <li class="fieldcontain">
	    <span id="items-label" class="property-label"><g:message code="migFormdef.items.label" default="Items" /></span>
	    <g:each in="${migFormdefInst.items}" var="f">
	      <span class="property-value" aria-labelledby="items-label"><g:link controller="migItem" action="show" id="${f.id}">${f?.display()?.encodeAsHTML()}</g:link></span>
	    </g:each>
	  </li>
	</g:if>
	<g:if test="${migFormdefInst?.pack}">
	  <li class="fieldcontain">
	    <span id="pack-label" class="property-label"><g:message code="migFormdef.pack.label" default="Pack" /></span>
	    <span class="property-value" aria-labelledby="pack-label"><g:link controller="migPackage" action="show" id="${migFormdefInst?.pack?.id}">${migFormdefInst?.pack?.display()?.encodeAsHTML()}</g:link></span>
	  </li>
	</g:if>
      </ol>
    </div>
  </body>
</html>
