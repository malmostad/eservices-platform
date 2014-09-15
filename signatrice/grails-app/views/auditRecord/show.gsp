
<%@ page import="org.motrice.signatrice.audit.AuditRecord" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'auditRecord.label', default: 'AuditRecord')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-auditRecord" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-auditRecord" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list auditRecord">
			
				<g:if test="${auditRecordInst?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="auditRecord.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${auditRecordInst?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${auditRecordInst?.eventType}">
				<li class="fieldcontain">
					<span id="eventType-label" class="property-label"><g:message code="auditRecord.eventType.label" default="Event Type" /></span>
					
						<span class="property-value" aria-labelledby="eventType-label"><g:fieldValue bean="${auditRecordInst}" field="eventType"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${auditRecordInst?.failure}">
				<li class="fieldcontain">
					<span id="failure-label" class="property-label"><g:message code="auditRecord.failure.label" default="Failure" /></span>
					
						<span class="property-value" aria-labelledby="failure-label"><g:formatBoolean boolean="${auditRecordInst?.failure}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${auditRecordInst?.description}">
				<li class="fieldcontain">
					<span id="description-label" class="property-label"><g:message code="auditRecord.description.label" default="Description" /></span>
					
						<span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${auditRecordInst}" field="description"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${auditRecordInst?.remoteAddr}">
				<li class="fieldcontain">
					<span id="remoteAddr-label" class="property-label"><g:message code="auditRecord.remoteAddr.label" default="Remote Addr" /></span>
					
						<span class="property-value" aria-labelledby="remoteAddr-label"><g:fieldValue bean="${auditRecordInst}" field="remoteAddr"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${auditRecordInst?.details}">
				<li class="fieldcontain">
					<span id="details-label" class="property-label"><g:message code="auditRecord.details.label" default="Details" /></span>
					
						<span class="property-value" aria-labelledby="details-label"><g:fieldValue bean="${auditRecordInst}" field="details"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${auditRecordInst?.stackTrace}">
				<li class="fieldcontain">
					<span id="stackTrace-label" class="property-label"><g:message code="auditRecord.stackTrace.label" default="Stack Trace" /></span>
					
						<span class="property-value" aria-labelledby="stackTrace-label"><g:fieldValue bean="${auditRecordInst}" field="stackTrace"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${auditRecordInst?.id}" />
					<g:link class="edit" action="edit" id="${auditRecordInst?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
