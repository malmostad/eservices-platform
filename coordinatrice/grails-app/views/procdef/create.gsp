<%@ page import="org.motrice.coordinatrice.Procdef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'procdef.label', default: 'Procdef')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-procdef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
      </ul>
    </div>
    <div id="create-crdProcCategory" class="content scaffold-create" role="main">
      <h1><g:message code="procdef.upload.bpmn.title"/></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <g:uploadForm action="xmlUploadFromScratch">
	<fieldset class="form">
	  <div class="fieldcontain">
	    <label for="deploymentName">
	      <g:message code="procdef.upload.deployment.label" default="Name" />
	    </label>
	    <g:textField name="deploymentName"/>
	  </div>
	  <div class="fieldcontain required">
	    <label for="category">
	      <g:message code="crdProcCategory.label" default="Category" />
	      <span class="required-indicator">*</span>
	    </label>
	      <g:select id="crdProcCategory" name="crdProcCategory.id" from="${categoryList}" optionKey="id" value="${defaultCategory?.id}"
			noSelection="['-1': message(code: 'crdProcCategory.select.label')]" style="width: 360px"/>
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
	</fieldset>
      </g:uploadForm>
    </div>
  </body>
</html>
