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
