<%@ page import="org.motrice.coordinatrice.ActDef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'actDef.label', default: 'ActDef')}" />
      <title><g:message code="activity.edit.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#edit-actDef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
      </ul>
    </div>
    <div id="edit-actDef" class="content scaffold-edit" role="main">
      <h1><g:message code="activity.edit.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <g:hasErrors bean="${actDefInst}">
	<ul class="errors" role="alert">
	  <g:eachError bean="${actDefInst}" var="error">
	    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
	  </g:eachError>
	</ul>
      </g:hasErrors>
      <g:form method="post" action="update">
	<g:hiddenField name="id" value="${actDefInst?.fullId}" />
	<fieldset class="form">
	  <g:render template="form"/>
	</fieldset>
	<fieldset class="buttons">
	  <g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
