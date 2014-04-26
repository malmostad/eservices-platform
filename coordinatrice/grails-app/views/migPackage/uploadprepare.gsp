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
<%@ page import="org.motrice.migratrice.MigPackage" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'migPackage.label', default: 'MigPackage')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#create-migPackage" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="create-migPackage" class="content scaffold-create" role="main">
      <h1><g:message code="migPackage.upload.file.label"/></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <g:uploadForm action="upload">
	<fieldset class="form">
	  <div class="fieldcontain required">
	    <label for="file">
	      <g:message code="procdef.upload.file.label"/>
	      <span class="required-indicator">*</span>
	    </label>
	    <input type="file" name="pkgUpload"/>
	  </div>
	</fieldset>
	<fieldset class="buttons">
	  <g:submitButton name="create" class="save"
			  value="${message(code: 'migPackage.upload.file.label')}" />
	</fieldset>
      </g:uploadForm>
    </div>
  </body>
</html>
