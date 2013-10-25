<%@ page import="org.motrice.coordinatrice.bonita.BnProcDef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'bnProcDef.label', default: 'BnProcDef')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-bnProcDef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="show-bnProcDef" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list bnProcDef">
	<g:if test="${bnProcDefInst?.uuid}">
	  <li class="fieldcontain">
	    <span id="uuid-label" class="property-label"><g:message code="bnProcDef.uuid.label" default="Uuid" /></span>
	    <span class="property-value" aria-labelledby="uuid-label"><g:fieldValue bean="${bnProcDefInst}" field="uuid"/></span>
	  </li>
	</g:if>
	<g:if test="${bnProcDefInst?.label}">
	  <li class="fieldcontain">
	    <span id="label-label" class="property-label"><g:message code="bnProcDef.label.label" default="Label" /></span>
	    <span class="property-value" aria-labelledby="label-label"><g:fieldValue bean="${bnProcDefInst}" field="label"/></span>
	  </li>
	</g:if>
	<g:if test="${bnProcDefInst?.name}">
	  <li class="fieldcontain">
	    <span id="name-label" class="property-label"><g:message code="bnProcDef.name.label" default="Name" /></span>
	    <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${bnProcDefInst}" field="name"/></span>
	  </li>
	</g:if>
	<g:if test="${bnProcDefInst?.vno}">
	  <li class="fieldcontain">
	    <span id="vno-label" class="property-label"><g:message code="bnProcDef.vno.label" default="Vno" /></span>
	    <span class="property-value" aria-labelledby="vno-label"><g:fieldValue bean="${bnProcDefInst}" field="vno"/></span>
	  </li>
	</g:if>
	<g:if test="${bnProcDefInst?.type}">
	  <li class="fieldcontain">
	    <span id="type-label" class="property-label"><g:message code="bnProcDef.type.label" default="Type" /></span>
	    <span class="property-value" aria-labelledby="type-label"><g:fieldValue bean="${bnProcDefInst}" field="type"/></span>
	  </li>
	</g:if>
	<g:if test="${bnProcDefInst?.state}">
	  <li class="fieldcontain">
	    <span id="state-label" class="property-label"><g:message code="bnProcDef.state.label" default="State" /></span>
	    <span class="property-value" aria-labelledby="state-label"><g:fieldValue bean="${bnProcDefInst}" field="state"/></span>
	  </li>
	</g:if>
	<g:if test="${bnProcDefInst?.activities}">
	  <li class="fieldcontain">
	    <span id="activities-label" class="property-label"><g:message code="bnProcDef.activities.label" default="Activities" /></span>
	    <g:each in="${bnProcDefInst.activities}" var="a">
	      <span class="property-value" aria-labelledby="activities-label"><g:link controller="bnActDef" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></span>
	    </g:each>
	  </li>
	</g:if>
	<g:if test="${bnProcDefInst?.deployedMillis}">
	  <li class="fieldcontain">
	    <span id="deployedMillis-label" class="property-label"><g:message code="bnProcDef.deployedTime.label" default="Deployed" /></span>
	    <span class="property-value" aria-labelledby="deployedMillis-label"><g:fieldValue bean="${bnProcDefInst}" field="deployedTime"/></span>
	  </li>
	</g:if>
	<g:if test="${bnProcDefInst?.undeployedMillis}">
	  <li class="fieldcontain">
	    <span id="undeployedMillis-label" class="property-label"><g:message code="bnProcDef.undeployedTime.label" default="Undeployed" /></span>
	    <span class="property-value" aria-labelledby="undeployedMillis-label"><g:fieldValue bean="${bnProcDefInst}" field="undeployedTime"/></span>
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${bnProcDefInst?.id}" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
