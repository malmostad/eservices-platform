<%@ page import="org.motrice.coordinatrice.ActDef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'actDef.label', default: 'ActDef')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-actDef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="show-actDef" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list actDef">
	<g:if test="${actDefInst?.name}">
	  <li class="fieldcontain">
	    <span id="name-label" class="property-label"><g:message code="actDef.name.label" default="Name" /></span>
	    <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${actDefInst}" field="name"/>
	      (<g:fieldValue bean="${actDefInst}" field="uuid"/>)
	    </span>
	  </li>
	</g:if>
	<g:if test="${actDefInst?.type}">
	  <li class="fieldcontain">
	    <span id="type-label" class="property-label"><g:message code="actDef.type.label" default="Type" /></span>
	    <span class="property-value" aria-labelledby="type-label"><g:tasktype type="${actDefInst?.type}"/></span>
	  </li>
	</g:if>
	<g:if test="${actDefInst?.process}">
	  <li class="fieldcontain">
	    <span id="process-label" class="property-label"><g:message code="actDef.process.label" default="Process" /></span>
	    <span class="property-value" aria-labelledby="process-label"><g:link controller="procdef" action="show" id="${actDefInst?.process?.uuid}">${actDefInst?.process?.encodeAsHTML()}</g:link></span>
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${actDefInst?.id}" />
	  <g:link class="edit" action="edit" id="${actDefInst?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
