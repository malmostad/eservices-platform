<%@ page import="org.motrice.signatrice.SigDefaultScheme" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'sigDefaultScheme.label', default: 'SigDefaultScheme')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-sigDefaultScheme" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="show-sigDefaultScheme" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list sigDefaultScheme">
	<li class="fieldcontain">
	  <span id="defaultScheme-label" class="property-label"><g:message code="sigDefaultScheme.defaultScheme.label" default="Default Scheme" /></span>
	  <span class="property-value" aria-labelledby="defaultScheme-label">
	    <g:if test="${sigDefaultSchemeObj?.defaultScheme}">
	      <g:link controller="sigScheme" action="show" id="${sigDefaultSchemeObj?.defaultScheme?.id}">${sigDefaultSchemeObj?.defaultScheme?.encodeAsHTML()}</g:link>
	    </g:if>
	    <g:else><g:message code="sigDefaultScheme.none" default="None" /></g:else>
	  </span>
	</li>
	<li class="fieldcontain">
	  <span id="lastUpdated-label" class="property-label"><g:message code="sigDefaultScheme.lastUpdated.label" default="Last Updated" /></span>
	  <span class="property-value" aria-labelledby="lastUpdated-label">
	    <g:if test="${sigDefaultSchemeObj?.lastUpdated}"><g:tstamp date="${sigDefaultSchemeObj?.lastUpdated}"/></g:if>
	    <g:else>--</g:else>
	  </span>
	</li>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${sigDefaultSchemeObj?.id}" />
	  <g:link class="edit" action="edit" id="${sigDefaultSchemeObj?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
