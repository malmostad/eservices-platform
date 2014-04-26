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
<%@ page import="org.motrice.coordinatrice.CrdI18nGuideUrl" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'crdI18nGuideUrl.label', default: 'CrdI18nGuideUrl')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-crdI18nGuideUrl" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="show-crdI18nGuideUrl" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list crdI18nGuideUrl">
	<g:if test="${crdI18nGuideUrlInst?.procdefKey}">
	  <li class="fieldcontain">
	    <span id="procdefKey-label" class="property-label"><g:message code="crdI18nGuideUrl.procdefKey.label" default="Procdef Key" /></span>
	    <span class="property-value" aria-labelledby="procdefKey-label"><g:fieldValue bean="${crdI18nGuideUrlInst}" field="procdefKey"/></span>
	  </li>
	</g:if>
	<g:if test="${crdI18nGuideUrlInst?.procdefVer}">
	  <li class="fieldcontain">
	    <span id="procdefVer-label" class="property-label"><g:message code="crdI18nGuideUrl.procdefVer.label" default="Procdef Ver" /></span>
	    <span class="property-value" aria-labelledby="procdefVer-label"><g:fieldValue bean="${crdI18nGuideUrlInst}" field="procdefVer"/></span>
	  </li>
	</g:if>
	<g:if test="${crdI18nGuideUrlInst?.pattern}">
	  <li class="fieldcontain">
	    <span id="pattern-label" class="property-label"><g:message code="crdI18nGuideUrl.pattern.label" default="Pattern" /></span>
	    <span class="property-value" aria-labelledby="pattern-label"><g:fieldValue bean="${crdI18nGuideUrlInst}" field="pattern"/></span>
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${crdI18nGuideUrlInst?.id}" />
	  <g:link class="edit" action="edit" id="${crdI18nGuideUrlInst?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
	  <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
