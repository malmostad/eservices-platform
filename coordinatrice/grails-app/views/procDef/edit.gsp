<%@ page import="org.motrice.coordinatrice.ProcDef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'procDef.label', default: 'ProcDef')}" />
      <title><g:message code="startform.edit.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#edit-procDef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
      </ul>
    </div>
    <div id="edit-procDef" class="content scaffold-edit" role="main">
      <h1><g:message code="startform.edit.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <g:hasErrors bean="${procDefInst}">
	<ul class="errors" role="alert">
	  <g:eachError bean="${procDefInst}" var="error">
	    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
	  </g:eachError>
	</ul>
      </g:hasErrors>
      <g:form method="post" action="update">
	<g:hiddenField name="id" value="${procDefInst?.uuid}" />
	<fieldset class="form">
	  <g:render template="form"/>
	</fieldset>
	<fieldset class="buttons">
	  <g:actionSubmit class="save" action="update" value="${message(code: 'startform.add.label', default: 'Save')}" />
	  <g:actionSubmit class="show" action="show" value="${message(code: 'procDef.show.label', default: 'Show')}" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
