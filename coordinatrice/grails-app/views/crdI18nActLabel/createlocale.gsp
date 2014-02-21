<%@ page import="org.motrice.coordinatrice.CrdI18nActLabel" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'crdI18nActLabel.label', default: 'CrdI18nActLabel')}" />
      <title><g:message code="default.create.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#create-crdI18nActLabel" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="create-crdI18nActLabel" class="content scaffold-create" role="main">
      <h1><g:message code="default.create.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <g:hasErrors bean="${actLabelInst}">
	<ul class="errors" role="alert">
	  <g:eachError bean="${actLabelInst}" var="error">
	    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
	  </g:eachError>
	</ul>
      </g:hasErrors>
      <ol class="property-list crdI18nActLabel">
	<li class="fieldcontain">
	  <span id="procdefKey-label" class="property-label"><g:message code="crdI18nActLabel.procdefKey.label" default="Procdef Key" /></span>
	  <span class="property-value" aria-labelledby="procdefKey-label"><g:fieldValue bean="${actLabelInst}" field="procdefKey"/></span>
	</li>
      </ol>
      <g:form action="savelocale" >
	<fieldset class="form">
	  <div class="fieldcontain ${hasErrors(bean: actLabelInst, field: 'locale', 'error')} ">
	    <label for="locale">
	      <g:message code="crdI18nActLabel.locale.label" default="Locale" />
	    </label>
	    <g:textField name="locale" maxlength="24" value="${actLabelInst?.locale}"/>
	  </div>
	</fieldset>
	<fieldset class="buttons">
	  <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
