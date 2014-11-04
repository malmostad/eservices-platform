<%@ page import="org.motrice.signatrice.audit.AudEventRecord" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'audEventRecord.label', default: 'EventRecord')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-eventRecord" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="show-eventRecord" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list eventRecord">
	<g:if test="${eventRecordInst?.dateCreated}">
	  <li class="fieldcontain">
	    <span id="dateCreated-label" class="property-label"><g:message code="audEventRecord.dateCreated.label" default="Date Created" /></span>
	    <span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate format="yyyy-MM-dd HH:mm:ss .SSS" date="${eventRecordInst?.dateCreated}" /></span>
	  </li>
	</g:if>
	<g:if test="${eventRecordInst?.eventType}">
	  <g:set var="icon"><g:failure flag="${eventRecordInst.failure}"/></g:set>
	  <li class="fieldcontain">
	    <span id="eventType-label" class="property-label"><g:message code="audEventRecord.eventType.label" default="Event Type" /></span>
	    <span class="property-value" aria-labelledby="eventType-label"><g:img dir="images/silk" file="${icon}"/>&nbsp;&nbsp;<g:fieldValue bean="${eventRecordInst}" field="eventType"/></span>
	  </li>
	</g:if>
	<g:if test="${eventRecordInst?.description}">
	  <li class="fieldcontain">
	    <span id="description-label" class="property-label"><g:message code="audEventRecord.description.label" default="Description" /></span>
	    <span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${eventRecordInst}" field="description"/></span>
	  </li>
	</g:if>
	<g:if test="${eventRecordInst?.label}">
	  <li class="fieldcontain">
	    <span id="label-label" class="property-label"><g:message code="audEventRecord.label.label" default="Label" /></span>
	    <span class="property-value" aria-labelledby="label-label"><g:fieldValue bean="${eventRecordInst}" field="label"/></span>
	  </li>
	</g:if>
	<g:if test="${eventRecordInst?.remoteAddr}">
	  <li class="fieldcontain">
	    <span id="remoteAddr-label" class="property-label"><g:message code="audEventRecord.remoteAddr.label" default="Remote Addr" /></span>
	    <span class="property-value" aria-labelledby="remoteAddr-label"><g:fieldValue bean="${eventRecordInst}" field="remoteAddr"/></span>
	  </li>
	</g:if>
	<g:if test="${eventRecordInst?.userRef}">
	  <li class="fieldcontain">
	    <span id="userRef-label" class="property-label"><g:message code="audEventRecord.userRef.label" default="User" /></span>
	    <span class="property-value" aria-labelledby="userRef-label"><g:fieldValue bean="${eventRecordInst}" field="userRef"/></span>
	  </li>
	</g:if>
	<g:if test="${eventRecordInst?.details}">
	  <li class="fieldcontain">
	    <span id="details-label" class="property-label"><g:message code="audEventRecord.details.label" default="Details" /></span>
	    <span class="property-value" aria-labelledby="details-label"><g:fieldValue bean="${eventRecordInst}" field="details"/></span>
	  </li>
	</g:if>
	<g:if test="${eventRecordInst?.stackTrace}">
	  <li class="fieldcontain">
	    <span id="stackTrace-label" class="property-label"><g:message code="audEventRecord.stackTrace.label" default="Stack Trace" /></span>
	    <span class="property-value" aria-labelledby="stackTrace-label"><g:fieldValue bean="${eventRecordInst}" field="stackTrace"/></span>
	  </li>
	</g:if>
      </ol>
    </div>
  </body>
</html>
