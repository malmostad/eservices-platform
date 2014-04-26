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
<%@ page import="org.motrice.coordinatrice.Procdef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'procdef.label', default: 'Procdef')}" />
      <title><g:message code="procdef.upload.version.label"/></title>
  </head>
  <body>
    <a href="#show-procdef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="create-crdProcCategory" class="content scaffold-create" role="main">
      <h1><g:message code="procdef.label"/>: <g:message code="procdef.upload.version.label"/></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <g:uploadForm action="xmlUpload">
	<g:hiddenField name="id" value="${procdefInst?.uuid}" />
	<fieldset class="form">
	  <div class="fieldcontain">
	    <label for="name">
	      <g:message code="procdef.name.label" default="Name" />
	    </label>
	    <g:fieldValue bean="${procdefInst}" field="nameOrKey"/>
	  </div>
	  <div class="fieldcontain">
	    <label for="vno">
	      <g:message code="procdef.vno.label" default="Version" />
	    </label>
	    <g:fieldValue bean="${procdefInst}" field="vno"/>
	  </div>
	  <div class="fieldcontain">
	    <label for="state">
	      <g:message code="procdef.state.label" default="State" />
	    </label>
	    <g:pdefstate state="${procdefInst?.state}"/>
	  </div>
	  <div class="fieldcontain">
	    <label for="category">
	      <g:message code="procdef.category.label" default="Category" />
	    </label>
	    <g:link controller="crdProcCategory" action="show" id="${procdefInst?.category?.id}"><g:fieldValue bean="${procdefInst}" field="category"/></g:link>
	  </div>
	  <div class="fieldcontain required">
	    <label for="file">
	      <g:message code="procdef.upload.file.label"/>
	      <span class="required-indicator">*</span>
	    </label>
	    <input type="file" name="bpmnDef"/>
	  </div>
	</fieldset>
	<fieldset class="buttons">
	  <g:submitButton name="create" class="save" value="${message(code: 'procdef.upload.bpmn.label')}"/>
	  <g:link class="show" action="show" id="${procdefInst?.uuid}">${message(code: 'procdef.show.label')}</g:link>
	</fieldset>
      </g:uploadForm>
    </div>
  </body>
</html>
