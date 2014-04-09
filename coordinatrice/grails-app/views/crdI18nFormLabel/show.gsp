<%@ page import="org.motrice.coordinatrice.CrdI18nFormLabel" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'crdI18nFormLabel.label', default: 'CrdI18nFormLabel')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-crdI18nFormLabel" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="show-crdI18nFormLabel" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list crdI18nFormLabel">
	<g:if test="${crdI18nFormLabelInst?.formdefPath}">
	  <li class="fieldcontain">
	    <span id="formdefPath-label" class="property-label"><g:message code="crdI18nFormLabel.formdefPath.label" default="Formdef Path" /></span>
	    <span class="property-value" aria-labelledby="formdefPath-label"><g:fieldValue bean="${crdI18nFormLabelInst}" field="formdefPath"/></span>
	  </li>
	</g:if>
	<g:if test="${crdI18nFormLabelInst?.formdefVer}">
	  <li class="fieldcontain">
	    <span id="formdefVer-label" class="property-label"><g:message code="crdI18nFormLabel.formdefVer.label" default="Formdef Ver" /></span>
	    <span class="property-value" aria-labelledby="formdefVer-label"><g:fieldValue bean="${crdI18nFormLabelInst}" field="formdefVer"/></span>
	  </li>
	</g:if>
	<g:if test="${crdI18nFormLabelInst?.label}">
	  <li class="fieldcontain">
	    <span id="label-label" class="property-label"><g:message code="crdI18nFormLabel.label.label" default="Label" /></span>
	    <span class="property-value" aria-labelledby="label-label"><g:fieldValue bean="${crdI18nFormLabelInst}" field="label"/></span>
	  </li>
	</g:if>
	<g:if test="${crdI18nFormLabelInst?.locale}">
	  <li class="fieldcontain">
	    <span id="locale-label" class="property-label"><g:message code="crdI18nFormLabel.locale.label" default="Locale" /></span>
	    <span class="property-value" aria-labelledby="locale-label"><g:fieldValue bean="${crdI18nFormLabelInst}" field="locale"/></span>
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${crdI18nFormLabelInst?.id}" />
	  <g:link class="edit" action="edit" id="${crdI18nFormLabelInst?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
	  <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
