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
<%@ page import="org.motrice.coordinatrice.CrdProcCategory" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'crdProcCategory.label', default: 'CrdProcCategory')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-crdProcCategory" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="show-crdProcCategory" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list crdProcCategory">
	<g:if test="${crdProcCategoryInst?.name}">
	  <li class="fieldcontain">
	    <span id="name-label" class="property-label"><g:message code="crdProcCategory.name.label" default="Name" /></span>
	    <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${crdProcCategoryInst}" field="name"/></span>
	  </li>
	</g:if>
	<g:if test="${crdProcCategoryInst?.description}">
	  <li class="fieldcontain">
	    <span id="description-label" class="property-label"><g:message code="crdProcCategory.description.label" default="Description" /></span>
	    <span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${crdProcCategoryInst}" field="description"/></span>
	  </li>
	</g:if>
	<g:if test="${crdProcCategoryInst?.dateCreated}">
	  <li class="fieldcontain">
	    <span id="dateCreated-label" class="property-label"><g:message code="crdProcCategory.dateCreated.label" default="Date Created" /></span>
	    <span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${crdProcCategoryInst?.dateCreated}" /></span>
	  </li>
	</g:if>
	<g:if test="${crdProcCategoryInst?.lastUpdated}">
	  <li class="fieldcontain">
	    <span id="lastUpdated-label" class="property-label"><g:message code="crdProcCategory.lastUpdated.label" default="Last Updated" /></span>
	    <span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${crdProcCategoryInst?.lastUpdated}" /></span>
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${crdProcCategoryInst?.id}" />
	  <g:link class="edit" action="edit" id="${crdProcCategoryInst?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
	  <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
