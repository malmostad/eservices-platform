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
<%@ page import="org.motrice.postxdb.PxdFormdefVer" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'pxdFormdefVer.label', default: 'PxdFormdefVer')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
    </meta>
  </head>
  <body>
    <a href="#show-pxdFormdefVer" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="show-pxdFormdefVer" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list pxdFormdefVer">
	<g:if test="${pxdFormdefVerObj?.path}">
	  <li class="fieldcontain">
	    <span id="path-label" class="property-label"><g:message code="pxdFormdefVer.path.label" default="Path" /></span>
	    <span class="property-value" aria-labelledby="path-label"><g:fieldValue bean="${pxdFormdefVerObj}" field="path"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefVerObj?.title}">
	  <li class="fieldcontain">
	    <span id="title-label" class="property-label"><g:message code="pxdFormdefVer.title.label" default="Title" /></span>
	    <span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${pxdFormdefVerObj}" field="title"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefVerObj?.description}">
	  <li class="fieldcontain">
	    <span id="description-label" class="property-label"><g:message code="pxdFormdefVer.description.label" default="Description" /></span>
	    <span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${pxdFormdefVerObj}" field="description"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefVerObj?.language}">
	  <li class="fieldcontain">
	    <span id="language-label" class="property-label"><g:message code="pxdFormdefVer.language.label" default="Language" /></span>
	    <span class="property-value" aria-labelledby="language-label"><g:fieldValue bean="${pxdFormdefVerObj}" field="language"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefVerObj?.appName}">
	  <li class="fieldcontain">
	    <span id="appName-label" class="property-label"><g:message code="pxdFormdefVer.appName.label" default="App Name" /></span>
	    <span class="property-value" aria-labelledby="appName-label"><g:fieldValue bean="${pxdFormdefVerObj}" field="appName"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefVerObj?.formName}">
	  <li class="fieldcontain">
	    <span id="formName-label" class="property-label"><g:message code="pxdFormdefVer.formName.label" default="Form Name" /></span>
	    <span class="property-value" aria-labelledby="formName-label"><g:fieldValue bean="${pxdFormdefVerObj}" field="formName"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefVerObj?.dateCreated}">
	  <li class="fieldcontain">
	    <span id="dateCreated-label" class="property-label"><g:message code="pxdFormdefVer.createdUpdated.label" default="Date Created" /></span>
	    <span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${pxdFormdefVerObj?.dateCreated}"/>/
	    <g:formatDate date="${pxdFormdefVerObj?.lastUpdated}"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefVerObj?.author}">
	  <li class="fieldcontain">
	    <span id="author-label" class="property-label"><g:message code="pxdFormdefVer.author.label" default="Author" /></span>
	    <span class="property-value" aria-labelledby="author-label"><g:fieldValue bean="${pxdFormdefVerObj}" field="author"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefVerObj?.logoRef}">
	  <li class="fieldcontain">
	    <span id="logoRef-label" class="property-label"><g:message code="pxdFormdefVer.logoRef.label" default="Logo Ref" /></span>
	    <span class="property-value" aria-labelledby="logoRef-label"><g:fieldValue bean="${pxdFormdefVerObj}" field="logoRef"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefVerObj?.formdef}">
	  <li class="fieldcontain">
	    <span id="formdef-label" class="property-label"><g:message code="pxdFormdefVer.formdef.label" default="Formdef" /></span>
	    <span class="property-value" aria-labelledby="formdef-label"><g:link controller="pxdFormdef" action="show" id="${pxdFormdefVerObj?.formdef?.id}">${pxdFormdefVerObj?.formdef.display()?.encodeAsHTML()}</g:link></span>
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${pxdFormdefVerObj?.id}" />
	  <g:link class="edit" action="edit" id="${pxdFormdefVerObj?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
	  <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
	  <g:link class="list" controller="pxdItem" action="listVersion" id="${pxdFormdefVerObj?.id}"><g:message code="pxdItem.list.label"/></g:link>
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
