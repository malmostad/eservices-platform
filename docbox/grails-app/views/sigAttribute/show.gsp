<%@ page import="org.motrice.signatrice.SigAttribute" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'sigAttribute.label', default: 'SigAttribute')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-sigAttribute" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="show-sigAttribute" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list sigAttribute">
	<g:if test="${sigAttributeInst?.name}">
	  <li class="fieldcontain">
	    <span id="name-label" class="property-label"><g:message code="sigAttribute.name.label" default="Name" /></span>
	    <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${sigAttributeInst}" field="name"/></span>
	  </li>
	</g:if>
	<g:if test="${sigAttributeInst?.value}">
	  <li class="fieldcontain">
	    <span id="value-size-label" class="property-label"><g:message code="sigAttribute.value.size.label" default="Value-Size" /></span>
	    <span class="property-value" aria-labelledby="value-size-label"><g:fieldValue bean="${sigAttributeInst}" field="valueSize"/></span>
	  </li>
	  <li class="fieldcontain">
	    <span id="value-label" class="property-label"><g:message code="sigAttribute.value.label" default="Value" /></span>
	    <span class="property-value" aria-labelledby="value-label">
	      <g:if test="${decodedValue}">
		<g:textArea name="sigAttribute-value" value="${decodedValue}"/>
	      </g:if>
	      <g:else>
		<g:textArea name="sigAttribute-value" value="${sigAttributeInst?.value?.encodeAsHTML()}"/>
	      </g:else>
	    </span>
	  </li>
	</g:if>
	<g:if test="${sigAttributeInst?.result}">
	  <li class="fieldcontain">
	    <span id="result-label" class="property-label"><g:message code="sigAttribute.result.label" default="Result" /></span>
	    <span class="property-value" aria-labelledby="result-label"><g:link controller="sigResult" action="show" id="${sigAttributeInst?.result?.id}">${sigAttributeInst?.result?.encodeAsHTML()}</g:link></span>
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${sigAttributeInst?.id}" />
	  <g:link class="edit" action="edit" id="${sigAttributeInst?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
	  <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
	  
	  <g:if test="${decodedValue}">
	    <g:link class="edit" action="show" id="${sigAttributeInst?.id}"><g:message code="default.button.plain.label" default="Undecoded" /></g:link>
	  </g:if>
	  <g:else>
	    <g:link class="edit" action="base64Decode" id="${sigAttributeInst?.id}"><g:message code="default.button.b64decode.label" default="Base64 Decode" /></g:link>
	  </g:else>
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
