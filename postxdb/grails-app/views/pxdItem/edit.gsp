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
<%@ page import="org.motrice.postxdb.PxdItem" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'pxdItem.label', default: 'PxdItem')}" />
      <title><g:message code="default.edit.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#edit-pxdItem" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="edit-pxdItem" class="content scaffold-edit" role="main">
      <h1><g:message code="pxdItem.edit.label"/></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <g:hasErrors bean="${pxdItemObj}">
	<ul class="errors" role="alert">
	  <g:eachError bean="${pxdItemObj}" var="error">
	    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
	  </g:eachError>
	</ul>
      </g:hasErrors>
      <g:form method="post"  enctype="multipart/form-data">
	<g:hiddenField name="id" value="${pxdItemObj?.id}" />
	<g:hiddenField name="version" value="${pxdItemObj?.version}" />
	<fieldset class="form">
	  <div class="fieldcontain">
	    <label for="path">
	      <g:message code="pxdItem.path.label" default="Path" />
	    </label>
	    <g:textField name="path" maxlength="255" size="64" readonly="true" value="${pxdItemObj?.path}"/>
	  </div>
	  <div class="fieldcontain">
	    <label for="search">
	      <g:message code="pxdItem.search.label" default="Search" />
	    </label>
	    <g:textField name="search" maxlength="80"/>
	  </div>
	  <div class="fieldcontain">
	    <label for="replace">
	      <g:message code="pxdItem.replace.label" default="Replace" />
	    </label>
	    <g:textField name="replace" maxlength="80"/>
	  </div>
	  <div class="fieldcontain">
	    <label for="globalFlag">
	      <g:message code="pxdItem.replace.all.label" default="Global" />
	    </label>
	    <g:checkBox name="globalFlag"/>
	  </div>
	</fieldset>
	<fieldset class="buttons">
	  <g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
	  <g:actionSubmit class="show" action="show" id="${pxdItemObj?.id}" value="${message(code: 'pxdItem.show.label', default: 'Show')}" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
