<%@ page import="org.motrice.orifice.OriItem" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'oriItem.label', default: 'OriItem')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-oriItem" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
      </ul>
    </div>
    <div id="show-oriItem" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list oriItem">
	<g:if test="${oriItemInst?.path}">
	  <li class="fieldcontain">
	    <span id="path-label" class="property-label"><g:message code="oriItem.path.label" default="Path" /></span>
	    <span class="property-value" aria-labelledby="path-label"><g:fieldValue bean="${oriItemInst}" field="path"/></span>
	  </li>
	</g:if>
	<g:if test="${oriItemInst?.ref}">
	  <li class="fieldcontain">
	    <span id="ref-label" class="property-label"><g:message code="oriItem.ref.label" default="Ref" /></span>
	    <span class="property-value" aria-labelledby="ref-label"><g:fieldValue bean="${oriItemInst}" field="ref"/></span>
	  </li>
	</g:if>
	<g:if test="${oriItemInst?.uuid}">
	  <li class="fieldcontain">
	    <span id="uuid-label" class="property-label"><g:message code="oriItem.uuid.label" default="Uuid" /></span>
	    <span class="property-value" aria-labelledby="uuid-label"><g:fieldValue bean="${oriItemInst}" field="uuid"/></span>
	  </li>
	</g:if>
	<g:if test="${oriItemInst?.formpath}">
	  <li class="fieldcontain">
	    <span id="formpath-label" class="property-label"><g:message code="oriItem.formpath.label" default="Form Path" /></span>
	    <span class="property-value" aria-labelledby="formpath-label"><g:fieldValue bean="${oriItemInst}" field="formpath"/></span>
	  </li>
	</g:if>
	<g:if test="${oriItemInst?.format}">
	  <li class="fieldcontain">
	    <span id="format-label" class="property-label"><g:message code="oriItem.format.label" default="Format" /></span>
	    <span class="property-value" aria-labelledby="format-label"><g:fieldValue bean="${oriItemInst}" field="format"/></span>
	  </li>
	</g:if>
	<g:if test="${oriItemInst?.size}">
	  <li class="fieldcontain">
	    <span id="size-label" class="property-label"><g:message code="oriItem.size.label" default="Size" /></span>
	    <span class="property-value" aria-labelledby="size-label"><g:fieldValue bean="${oriItemInst}" field="size"/></span>
	  </li>
	</g:if>
	<g:if test="${oriItemInst?.sha1}">
	  <li class="fieldcontain">
	    <span id="sha1-label" class="property-label"><g:message code="oriItem.sha1.label" default="Sha1" /></span>
	    <span class="property-value" aria-labelledby="sha1-label"><g:fieldValue bean="${oriItemInst}" field="sha1"/></span>
	  </li>
	</g:if>
	<g:if test="${oriItemInst?.created}">
	  <li class="fieldcontain">
	    <span id="created-label" class="property-label"><g:message code="oriItem.created.label" default="Created" /></span>
	    <span class="property-value" aria-labelledby="created-label"><g:tstamp date="${oriItemInst?.created}" /></span>
	  </li>
	</g:if>
	<g:if test="${oriItemInst?.formdef}">
	  <li class="fieldcontain">
	    <span id="formdef-label" class="property-label"><g:message code="oriItem.formdef.label" default="Formdef" /></span>
	    <span class="property-value" aria-labelledby="formdef-label"><g:link controller="oriFormdef" action="show" id="${oriItemInst?.formdef?.id}">${oriItemInst?.formdef?.encodeAsHTML()}</g:link></span>
	  </li>
	</g:if>
	<g:if test="${oriItemInst?.pkg}">
	  <li class="fieldcontain">
	    <span id="pkg-label" class="property-label"><g:message code="oriItem.pkg.label" default="Pkg" /></span>
	    <span class="property-value" aria-labelledby="pkg-label"><g:link controller="oriPackage" action="show" id="${oriItemInst?.pkg?.id}">${oriItemInst?.pkg?.encodeAsHTML()}</g:link></span>
	  </li>
	</g:if>
      </ol>
    </div>
  </body>
</html>
