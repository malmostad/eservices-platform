
<%@ page import="org.motrice.coordinatrice.MtfProcessActivityFormInstance" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'mtfProcessActivityFormInstance.label', default: 'MtfProcessActivityFormInstance')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-mtfProcessActivityFormInstance" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-mtfProcessActivityFormInstance" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list mtfProcessActivityFormInstance">
			
				<g:if test="${mtfProcessActivityFormInstanceInst?.activityInstanceUuid}">
				<li class="fieldcontain">
					<span id="activityInstanceUuid-label" class="property-label"><g:message code="mtfProcessActivityFormInstance.activityInstanceUuid.label" default="Activity Instance Uuid" /></span>
					
						<span class="property-value" aria-labelledby="activityInstanceUuid-label"><g:fieldValue bean="${mtfProcessActivityFormInstanceInst}" field="activityInstanceUuid"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${mtfProcessActivityFormInstanceInst?.formDocId}">
				<li class="fieldcontain">
					<span id="formDocId-label" class="property-label"><g:message code="mtfProcessActivityFormInstance.formDocId.label" default="Form Doc Id" /></span>
					
						<span class="property-value" aria-labelledby="formDocId-label"><g:fieldValue bean="${mtfProcessActivityFormInstanceInst}" field="formDocId"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${mtfProcessActivityFormInstanceInst?.formPath}">
				<li class="fieldcontain">
					<span id="formPath-label" class="property-label"><g:message code="mtfProcessActivityFormInstance.formPath.label" default="Form Path" /></span>
					
						<span class="property-value" aria-labelledby="formPath-label"><g:fieldValue bean="${mtfProcessActivityFormInstanceInst}" field="formPath"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${mtfProcessActivityFormInstanceInst?.processInstanceUuid}">
				<li class="fieldcontain">
					<span id="processInstanceUuid-label" class="property-label"><g:message code="mtfProcessActivityFormInstance.processInstanceUuid.label" default="Process Instance Uuid" /></span>
					
						<span class="property-value" aria-labelledby="processInstanceUuid-label"><g:fieldValue bean="${mtfProcessActivityFormInstanceInst}" field="processInstanceUuid"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${mtfProcessActivityFormInstanceInst?.submitted}">
				<li class="fieldcontain">
					<span id="submitted-label" class="property-label"><g:message code="mtfProcessActivityFormInstance.submitted.label" default="Submitted" /></span>
					
						<span class="property-value" aria-labelledby="submitted-label"><g:formatDate date="${mtfProcessActivityFormInstanceInst?.submitted}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${mtfProcessActivityFormInstanceInst?.userid}">
				<li class="fieldcontain">
					<span id="userid-label" class="property-label"><g:message code="mtfProcessActivityFormInstance.userid.label" default="Userid" /></span>
					
						<span class="property-value" aria-labelledby="userid-label"><g:fieldValue bean="${mtfProcessActivityFormInstanceInst}" field="userid"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${mtfProcessActivityFormInstanceInst?.startForm}">
				<li class="fieldcontain">
					<span id="startForm-label" class="property-label"><g:message code="mtfProcessActivityFormInstance.startForm.label" default="Start Form" /></span>
					
						<span class="property-value" aria-labelledby="startForm-label"><g:link controller="mtfStartFormDefinition" action="show" id="${mtfProcessActivityFormInstanceInst?.startForm?.id}">${mtfProcessActivityFormInstanceInst?.startForm?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${mtfProcessActivityFormInstanceInst?.id}" />
					<g:link class="edit" action="edit" id="${mtfProcessActivityFormInstanceInst?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
