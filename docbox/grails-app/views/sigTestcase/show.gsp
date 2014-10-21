<%@ page import="org.motrice.signatrice.SigTestcase" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'sigTestcase.label', default: 'SigTestcase')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-sigTestcase" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
	<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="show-sigTestcase" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list sigTestcase">
	<g:if test="${sigTestcaseObj?.name}">
	  <li class="fieldcontain">
	    <span id="name-label" class="property-label"><g:message code="sigTestcase.name.label" default="Name" /></span>
	    <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${sigTestcaseObj}" field="name"/></span>
	  </li>
	</g:if>
	<g:if test="${sigTestcaseObj?.personalIdNo}">
	  <li class="fieldcontain">
	    <span id="personalIdNo-label" class="property-label"><g:message code="sigTestcase.personalIdNo.label" default="Personal Id No" /></span>
	    <span class="property-value" aria-labelledby="personalIdNo-label"><g:fieldValue bean="${sigTestcaseObj}" field="personalIdNo"/></span>
	  </li>
	</g:if>
	<g:if test="${sigTestcaseObj?.userVisibleText}">
	  <li class="fieldcontain">
	    <span id="userVisibleText-label" class="property-label"><g:message code="sigTestcase.userVisibleText.label" default="User Visible Text" /></span>
	    <span class="property-value" aria-labelledby="userVisibleText-label"><g:fieldValue class="wide" bean="${sigTestcaseObj}" field="userVisibleText"/></span>
	  </li>
	</g:if>
	<g:if test="${sigTestcaseObj?.scheme}">
	  <li class="fieldcontain">
	    <span id="scheme-label" class="property-label"><g:message code="sigTestcase.scheme.label" default="Scheme" /></span>
	    <span class="property-value" aria-labelledby="scheme-label"><g:link controller="sigScheme" action="show" id="${sigTestcaseObj?.scheme?.id}">${sigTestcaseObj?.scheme?.encodeAsHTML()}</g:link></span>
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${sigTestcaseObj?.id}"/>
	  <g:link class="edit" action="edit" id="${sigTestcaseObj?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
	  <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
	  <g:link class="edit" controller="restSig" action="testSigRequest" id="${sigTestcaseObj?.id}"><g:message code="sigTestcase.sign.label" default="Sign" /></g:link>
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
