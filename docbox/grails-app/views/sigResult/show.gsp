<%@ page import="org.motrice.signatrice.SigResult" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'sigResult.label', default: 'SigResult')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-sigResult" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="show-sigResult" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list sigResult">
	<g:if test="${sigResultInst?.dateCreated}">
	  <li class="fieldcontain">
	    <span id="dateCreated-label" class="property-label"><g:message code="sigResult.dateCreated.label" default="Date Created" /></span>
	    <span class="property-value" aria-labelledby="dateCreated-label"><g:tstampsec date="${sigResultInst?.dateCreated}" /></span>
	  </li>
	</g:if>
	<g:if test="${sigResultInst?.tcase}">
	  <li class="fieldcontain">
	    <span id="tcase-label" class="property-label"><g:message code="sigResult.tcase.label" default="Tcase" /></span>
	    <span class="property-value" aria-labelledby="tcase-label"><g:link controller="sigScheme" action="show" id="${sigResultInst?.tcase?.id}">${sigResultInst?.tcase?.encodeAsHTML()}</g:link></span>
	  </li>
	</g:if>
	<g:if test="${sigResultInst?.progressStatus}">
	  <li class="fieldcontain">
	    <span id="progressStatus-label" class="property-label"><g:message code="sigResult.progressStatus.label" default="Progress Status" /></span>
	    <span class="property-value" aria-labelledby="progressStatus-label">${sigResultInst?.progressStatus?.encodeAsHTML()}</span>
	  </li>
	</g:if>
	<g:if test="${sigResultInst?.transactionId}">
	  <li class="fieldcontain">
	    <span id="transactionId-label" class="property-label"><g:message code="sigResult.transactionId.label" default="Transaction Id" /></span>
	    <span class="property-value" aria-labelledby="transactionId-label"><g:fieldValue bean="${sigResultInst}" field="transactionId"/></span>
	  </li>
	</g:if>
	<g:if test="${sigResultInst?.orderRef}">
	  <li class="fieldcontain">
	    <span id="orderRef-label" class="property-label"><g:message code="sigResult.orderRef.label" default="Order Ref" /></span>
	    <span class="property-value" aria-labelledby="orderRef-label"><g:fieldValue bean="${sigResultInst}" field="orderRef"/></span>
	  </li>
	</g:if>
	<g:if test="${sigResultInst?.autoStartToken}">
	  <li class="fieldcontain">
	    <span id="autoStartToken-label" class="property-label"><g:message code="sigResult.autoStartToken.label" default="Auto Start Token" /></span>
	    <span class="property-value" aria-labelledby="autoStartToken-label"><g:fieldValue bean="${sigResultInst}" field="autoStartToken"/></span>
	  </li>
	</g:if>
	<g:if test="${sigResultInst?.personalIdNo}">
	  <li class="fieldcontain">
	    <span id="personalIdNo-label" class="property-label"><g:message code="sigResult.personalIdNo.label" default="Personal Id No" /></span>
	    <span class="property-value" aria-labelledby="personalIdNo-label"><g:fieldValue bean="${sigResultInst}" field="personalIdNo"/></span>
	  </li>
	</g:if>
	<g:if test="${sigResultInst?.faultStatus}">
	  <li class="fieldcontain">
	    <span id="faultStatus-label" class="property-label"><g:message code="sigResult.faultStatus.label" default="Fault Status" /></span>
	    <span class="property-value" aria-labelledby="faultStatus-label">${sigResultInst?.faultStatus?.encodeAsHTML()}</span>
	  </li>
	</g:if>
	<g:if test="${sigResultInst?.signature}">
	  <li class="fieldcontain">
	    <span id="signature-label" class="property-label"><g:message code="sigResult.signature.label" default="Signature" /></span>
	    <span class="property-value" aria-labelledby="signature-label"><g:fieldValue bean="${sigResultInst}" field="sigSize"/></span>
	  </li>
	</g:if>
	<g:if test="${sigResultInst?.attrs}">
	  <li class="fieldcontain">
	    <span id="attrs-label" class="property-label"><g:message code="sigResult.attrs.label" default="Attrs" /></span>
	    <g:each in="${sigResultInst.attrs}" var="a">
	      <span class="property-value" aria-labelledby="attrs-label"><g:link controller="sigAttribute" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></span>
	    </g:each>
	  </li>
	</g:if>
	<g:if test="${sigResultInst?.displayName}">
	  <li class="fieldcontain">
	    <span id="displayName-label" class="property-label"><g:message code="sigResult.displayName.label" default="Display Name" /></span>
	    <span class="property-value" aria-labelledby="displayName-label">${sigResultInst?.displayName?.encodeAsHTML()}</span>
	  </li>
	</g:if>
	<g:if test="${sigResultInst?.policy}">
	  <li class="fieldcontain">
	    <span id="policy-label" class="property-label"><g:message code="sigResult.policy.label" default="Policy" /></span>
	    <span class="property-value" aria-labelledby="policy-label">${sigResultInst?.policy?.encodeAsHTML()}</span>
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${sigResultInst?.id}" />
	  <g:link class="edit" action="edit" id="${sigResultInst?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
	  <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
	  <g:link class="edit" action="sign" id="${sigResultInst?.id}"><g:message code="sigResult.sign.label" default="Collect" /></g:link>
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
