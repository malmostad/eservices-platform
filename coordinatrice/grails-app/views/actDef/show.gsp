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
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="show-actDef" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list actDef">
	<g:if test="${actDefInst?.uuid}">
	  <li class="fieldcontain">
	    <span id="uuid-label" class="property-label"><g:message code="actDef.uuid.label" default="Uuid" /></span>
	    <span class="property-value" aria-labelledby="uuid-label"><g:fieldValue bean="${actDefInst}" field="uuid"/></span>
	  </li>
	</g:if>
	<g:if test="${actDefInst?.label}">
	  <li class="fieldcontain">
	    <span id="label-label" class="property-label"><g:message code="actDef.label.label" default="Label" /></span>
	    <span class="property-value" aria-labelledby="label-label"><g:fieldValue bean="${actDefInst}" field="label"/></span>
	  </li>
	</g:if>
	<g:if test="${actDefInst?.name}">
	  <li class="fieldcontain">
	    <span id="name-label" class="property-label"><g:message code="actDef.name.label" default="Name" /></span>
	    <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${actDefInst}" field="name"/></span>
	  </li>
	</g:if>
	<g:if test="${actDefInst?.type}">
	  <li class="fieldcontain">
	    <span id="type-label" class="property-label"><g:message code="actDef.type.label" default="Type" /></span>
	    <span class="property-value" aria-labelledby="type-label"><g:fieldValue bean="${actDefInst}" field="type"/></span>
	  </li>
	</g:if>
	<g:if test="${actDefInst?.process}">
	  <li class="fieldcontain">
	    <span id="process-label" class="property-label"><g:message code="actDef.process.label" default="Process" /></span>
	    <span class="property-value" aria-labelledby="process-label"><g:link controller="procDef" action="show" id="${actDefInst?.process?.id}">${actDefInst?.process?.encodeAsHTML()}</g:link></span>
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
