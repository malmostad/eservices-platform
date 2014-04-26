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
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-migReport" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="show-migReport" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list migReport">
	<g:if test="${migReportInst?.tstamp}">
	  <li class="fieldcontain">
	    <span id="tstamp-label" class="property-label"><g:message code="migReport.tstamp.label" default="Tstamp" /></span>
	    <span class="property-value" aria-labelledby="tstamp-label"><g:tstampsec date="${migReportInst?.tstamp}"/></span>
	  </li>
	</g:if>
	<g:if test="${migReportInst?.body}">
	  <li class="fieldcontain">
	    <span id="body-label" class="property-label"><g:message code="migReport.body.label" default="Body" /></span>
	    <span class="property-value" aria-labelledby="body-label">
	      <g:textArea id="report-body" name="body" wrap="hard" value="${migReportInst?.body?.encodeAsHTML()}" style="width:550px"/>
	    </span>
	  </li>
	</g:if>
	<g:if test="${migReportInst?.pkg}">
	  <li class="fieldcontain">
	    <span id="pkg-label" class="property-label"><g:message code="migReport.pkg.label" default="Pkg" /></span>
	    <span class="property-value" aria-labelledby="pkg-label"><g:link controller="migPackage" action="show" id="${migReportInst?.pkg?.id}">${migReportInst?.pkg?.display()?.encodeAsHTML()}</g:link></span>
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${migReportInst?.id}" />
	  <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
