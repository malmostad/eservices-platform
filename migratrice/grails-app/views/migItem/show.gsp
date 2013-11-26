<%@ page import="org.motrice.migratrice.MigItem" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'migItem.label', default: 'MigItem')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-migItem" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
      </ul>
    </div>
    <div id="show-migItem" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list migItem">
	<g:if test="${migItemInst?.path}">
	  <li class="fieldcontain">
	    <span id="path-label" class="property-label"><g:message code="migItem.path.label" default="Path" /></span>
	    <span class="property-value" aria-labelledby="path-label"><g:fieldValue bean="${migItemInst}" field="path"/></span>
	  </li>
	</g:if>
	<g:if test="${migItemInst?.ref}">
	  <li class="fieldcontain">
	    <span id="ref-label" class="property-label"><g:message code="migItem.ref.label" default="Ref" /></span>
	    <span class="property-value" aria-labelledby="ref-label"><g:fieldValue bean="${migItemInst}" field="ref"/></span>
	  </li>
	</g:if>
	<g:if test="${migItemInst?.uuid}">
	  <li class="fieldcontain">
	    <span id="uuid-label" class="property-label"><g:message code="migItem.uuid.label" default="Uuid" /></span>
	    <span class="property-value" aria-labelledby="uuid-label"><g:fieldValue bean="${migItemInst}" field="uuid"/></span>
	  </li>
	</g:if>
	<g:if test="${migItemInst?.formpath}">
	  <li class="fieldcontain">
	    <span id="formpath-label" class="property-label"><g:message code="migItem.formpath.label" default="Form Path" /></span>
	    <span class="property-value" aria-labelledby="formpath-label"><g:fieldValue bean="${migItemInst}" field="formpath"/></span>
	  </li>
	</g:if>
	<g:if test="${migItemInst?.format}">
	  <li class="fieldcontain">
	    <span id="format-label" class="property-label"><g:message code="migItem.format.label" default="Format" /></span>
	    <span class="property-value" aria-labelledby="format-label"><g:fieldValue bean="${migItemInst}" field="format"/></span>
	  </li>
	</g:if>
	<g:if test="${migItemInst?.size}">
	  <li class="fieldcontain">
	    <span id="size-label" class="property-label"><g:message code="migItem.size.label" default="Size" /></span>
	    <span class="property-value" aria-labelledby="size-label"><g:fieldValue bean="${migItemInst}" field="size"/></span>
	  </li>
	</g:if>
	<g:if test="${migItemInst?.sha1}">
	  <li class="fieldcontain">
	    <span id="sha1-label" class="property-label"><g:message code="migItem.sha1.label" default="Sha1" /></span>
	    <span class="property-value" aria-labelledby="sha1-label"><g:fieldValue bean="${migItemInst}" field="sha1"/></span>
	  </li>
	</g:if>
	<g:if test="${migItemInst?.created}">
	  <li class="fieldcontain">
	    <span id="created-label" class="property-label"><g:message code="migItem.created.label" default="Created" /></span>
	    <span class="property-value" aria-labelledby="created-label"><g:tstamp date="${migItemInst?.created}" /></span>
	  </li>
	</g:if>
	<g:if test="${migItemInst?.formdef}">
	  <li class="fieldcontain">
	    <span id="formdef-label" class="property-label"><g:message code="migItem.formdef.label" default="Formdef" /></span>
	    <span class="property-value" aria-labelledby="formdef-label"><g:link controller="migFormdef" action="show" id="${migItemInst?.formdef?.id}">${migItemInst?.formdef?.encodeAsHTML()}</g:link></span>
	  </li>
	</g:if>
	<g:if test="${migItemInst?.pkg}">
	  <li class="fieldcontain">
	    <span id="pkg-label" class="property-label"><g:message code="migItem.pkg.label" default="Pkg" /></span>
	    <span class="property-value" aria-labelledby="pkg-label"><g:link controller="migPackage" action="show" id="${migItemInst?.pkg?.id}">${migItemInst?.pkg?.encodeAsHTML()}</g:link></span>
	  </li>
	</g:if>
      </ol>
    </div>
  </body>
</html>
