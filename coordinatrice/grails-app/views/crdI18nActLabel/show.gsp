<%@ page import="org.motrice.coordinatrice.CrdI18nActLabel" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'crdI18nActLabel.label', default: 'CrdI18nActLabel')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-crdI18nActLabel" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="show-crdI18nActLabel" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list crdI18nActLabel">
	
	<g:if test="${actLabelInst?.procdefKey}">
	  <li class="fieldcontain">
	    <span id="procdefKey-label" class="property-label"><g:message code="crdI18nActLabel.procdefKey.label" default="Procdef Key" /></span>
	    
	    <span class="property-value" aria-labelledby="procdefKey-label"><g:fieldValue bean="${actLabelInst}" field="procdefKey"/></span>
	    
	  </li>
	</g:if>
	
	<g:if test="${actLabelInst?.procdefVer}">
	  <li class="fieldcontain">
	    <span id="procdefVer-label" class="property-label"><g:message code="crdI18nActLabel.procdefVer.label" default="Procdef Ver" /></span>
	    
	    <span class="property-value" aria-labelledby="procdefVer-label"><g:fieldValue bean="${actLabelInst}" field="procdefVer"/></span>
	    
	  </li>
	</g:if>
	
	<g:if test="${actLabelInst?.actdefName}">
	  <li class="fieldcontain">
	    <span id="actdefName-label" class="property-label"><g:message code="crdI18nActLabel.actdefName.label" default="Actdef Name" /></span>
	    
	    <span class="property-value" aria-labelledby="actdefName-label"><g:fieldValue bean="${actLabelInst}" field="actdefName"/></span>
	    
	  </li>
	</g:if>
	
	<g:if test="${actLabelInst?.actdefId}">
	  <li class="fieldcontain">
	    <span id="actdefId-label" class="property-label"><g:message code="crdI18nActLabel.actdefId.label" default="Actdef Id" /></span>
	    
	    <span class="property-value" aria-labelledby="actdefId-label"><g:fieldValue bean="${actLabelInst}" field="actdefId"/></span>
	    
	  </li>
	</g:if>
	
	<g:if test="${actLabelInst?.locale}">
	  <li class="fieldcontain">
	    <span id="locale-label" class="property-label"><g:message code="crdI18nActLabel.locale.label" default="Locale" /></span>
	    
	    <span class="property-value" aria-labelledby="locale-label"><g:fieldValue bean="${actLabelInst}" field="locale"/></span>
	    
	  </li>
	</g:if>
	
	<g:if test="${actLabelInst?.label}">
	  <li class="fieldcontain">
	    <span id="label-label" class="property-label"><g:message code="crdI18nActLabel.label.label" default="Label" /></span>
	    
	    <span class="property-value" aria-labelledby="label-label"><g:fieldValue bean="${actLabelInst}" field="label"/></span>
	    
	  </li>
	</g:if>
	
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${actLabelInst?.id}" />
	  <g:link class="edit" action="edit" id="${actLabelInst?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
	  <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
