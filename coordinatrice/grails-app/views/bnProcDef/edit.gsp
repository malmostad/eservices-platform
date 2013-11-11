<%@ page import="org.motrice.coordinatrice.bonita.BnProcDef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'bnProcDef.label', default: 'BnProcDef')}" />
      <title><g:message code="startform.edit.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#edit-bnProcDef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
      </ul>
    </div>
    <div id="edit-bnProcDef" class="content scaffold-edit" role="main">
      <h1><g:message code="startform.edit.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <g:hasErrors bean="${bnProcDefInst}">
	<ul class="errors" role="alert">
	  <g:eachError bean="${bnProcDefInst}" var="error">
	    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
	  </g:eachError>
	</ul>
      </g:hasErrors>
      <g:form method="post" >
	<g:hiddenField name="id" value="${bnProcDefInst?.id}" />
	<fieldset class="form">
	  <g:render template="form"/>
	</fieldset>
	<fieldset class="buttons">
	  <g:actionSubmit class="save" action="update" value="${message(code: 'startform.add.label', default: 'Save')}" />
	  <g:actionSubmit class="show" action="show" value="${message(code: 'bnProcDef.show.label', default: 'Show')}" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
