<%@ page import="org.motrice.signatrice.SigCustomElement" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'sigCustomElement.label', default: 'SigCustomElement')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-sigCustomElement" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="show-sigCustomElement" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list sigCustomElement">
	<g:if test="${sigCustomElementObj?.elementName}">
	  <li class="fieldcontain">
	    <span id="elementName-label" class="property-label"><g:message code="sigCustomElement.elementName.label" default="Xml Id" /></span>
	    <span class="property-value" aria-labelledby="elementName-label"><g:fieldValue bean="${sigCustomElementObj}" field="elementName"/></span>
	  </li>
	</g:if>
	<g:if test="${sigCustomElementObj?.idAttrName}">
	  <li class="fieldcontain">
	    <span id="idAttrName-label" class="property-label"><g:message code="sigCustomElement.idAttrName.label" default="IdAttrName" /></span>
	    <span class="property-value" aria-labelledby="idAttrName-label"><g:fieldValue bean="${sigCustomElementObj}" field="idAttrName"/></span>
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${sigCustomElementObj?.id}" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
