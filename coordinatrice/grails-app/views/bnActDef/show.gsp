<%@ page import="org.motrice.coordinatrice.bonita.BnActDef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'bnActDef.label', default: 'BnActDef')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-bnActDef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="show-bnActDef" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list bnActDef">
	<g:if test="${bnActDefInst?.uuid}">
	  <li class="fieldcontain">
	    <span id="uuid-label" class="property-label"><g:message code="bnActDef.uuid.label" default="Uuid" /></span>
	    <span class="property-value" aria-labelledby="uuid-label"><g:fieldValue bean="${bnActDefInst}" field="uuid"/></span>
	  </li>
	</g:if>
	<g:if test="${bnActDefInst?.label}">
	  <li class="fieldcontain">
	    <span id="label-label" class="property-label"><g:message code="bnActDef.label.label" default="Label" /></span>
	    <span class="property-value" aria-labelledby="label-label"><g:fieldValue bean="${bnActDefInst}" field="label"/></span>
	  </li>
	</g:if>
	<g:if test="${bnActDefInst?.name}">
	  <li class="fieldcontain">
	    <span id="name-label" class="property-label"><g:message code="bnActDef.name.label" default="Name" /></span>
	    <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${bnActDefInst}" field="name"/></span>
	  </li>
	</g:if>
	<g:if test="${bnActDefInst?.type}">
	  <li class="fieldcontain">
	    <span id="type-label" class="property-label"><g:message code="bnActDef.type.label" default="Type" /></span>
	    <span class="property-value" aria-labelledby="type-label"><g:fieldValue bean="${bnActDefInst}" field="type"/></span>
	  </li>
	</g:if>
	<g:if test="${bnActDefInst?.process}">
	  <li class="fieldcontain">
	    <span id="process-label" class="property-label"><g:message code="bnActDef.process.label" default="Process" /></span>
	    <span class="property-value" aria-labelledby="process-label"><g:link controller="bnProcDef" action="show" id="${bnActDefInst?.process?.id}">${bnActDefInst?.process?.encodeAsHTML()}</g:link></span>
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${bnActDefInst?.id}" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
