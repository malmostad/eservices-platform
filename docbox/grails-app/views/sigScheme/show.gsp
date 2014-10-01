<%@ page import="org.motrice.signatrice.SigScheme" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'sigScheme.label', default: 'SigScheme')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-sigScheme" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="show-sigScheme" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list sigScheme">
	<g:if test="${sigSchemeObj?.name}">
	  <li class="fieldcontain">
	    <span id="name-label" class="property-label"><g:message code="sigScheme.name.label" default="Name" /></span>
	    <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${sigSchemeObj}" field="name"/></span>
	  </li>
	</g:if>
	<g:if test="${sigSchemeObj?.service}">
	  <li class="fieldcontain">
	    <span id="service-label" class="property-label"><g:message code="sigScheme.service.label" default="Service" /></span>
	    <span class="property-value" aria-labelledby="service-label"><g:link controller="sigService" action="show" id="${sigSchemeObj?.service?.id}">${sigSchemeObj?.service?.encodeAsHTML()}</g:link></span>
	  </li>
	</g:if>
	<g:if test="${sigSchemeObj?.personalIdNo}">
	  <li class="fieldcontain">
	    <span id="personalIdNo-label" class="property-label"><g:message code="sigScheme.personalIdNo.label" default="Personal Id No" /></span>
	    <span class="property-value" aria-labelledby="personalIdNo-label"><g:fieldValue bean="${sigSchemeObj}" field="personalIdNo"/></span>
	  </li>
	</g:if>
	<g:if test="${sigSchemeObj?.displayName}">
	  <li class="fieldcontain">
	    <span id="displayName-label" class="property-label"><g:message code="sigScheme.displayName.label" default="Display Name" /></span>
	    <span class="property-value" aria-labelledby="displayName-label">${sigSchemeObj?.displayName?.encodeAsHTML()}</span>
	  </li>
	</g:if>
	<g:if test="${sigSchemeObj?.policy}">
	  <li class="fieldcontain">
	    <span id="policy-label" class="property-label"><g:message code="sigScheme.policy.label" default="Policy" /></span>
	    <span class="property-value" aria-labelledby="policy-label">${sigSchemeObj?.policy?.encodeAsHTML()}</span>
	  </li>
	</g:if>
	<g:if test="${sigSchemeObj?.dateCreated}">
	  <li class="fieldcontain">
	    <span id="dateCreated-label" class="property-label"><g:message code="sigScheme.dateCreated.label" default="Date Created" /></span>
	    <span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${sigSchemeObj?.dateCreated}" /></span>
	  </li>
	</g:if>
	<g:if test="${sigSchemeObj?.userVisibleText}">
	  <li class="fieldcontain">
	    <span id="userVisibleText-label" class="property-label"><g:message code="sigScheme.userVisibleText.label" default="User Visible Text" /></span>
	    <span class="property-value" aria-labelledby="userVisibleText-label"><g:fieldValue bean="${sigSchemeObj}" field="userVisibleText"/></span>
	  </li>
	</g:if>
	<g:if test="${sigSchemeObj?.results}">
	  <li class="fieldcontain">
	    <span id="results-label" class="property-label"><g:message code="sigScheme.results.label" default="Results" /></span>
	    <g:each in="${sigSchemeObj.results}" var="r">
	      <span class="property-value" aria-labelledby="results-label"><g:link controller="sigResult" action="show" id="${r.id}">${r?.encodeAsHTML()}</g:link></span>
	    </g:each>
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${sigSchemeObj?.id}" />
	  <g:link class="edit" action="edit" id="${sigSchemeObj?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
	  <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
	  <g:link class="edit" action="sign" id="${sigSchemeObj?.id}"><g:message code="sigScheme.sign.label" default="Sign" /></g:link>
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
