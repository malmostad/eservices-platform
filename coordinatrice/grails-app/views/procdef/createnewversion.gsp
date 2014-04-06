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
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
      </ul>
    </div>
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
