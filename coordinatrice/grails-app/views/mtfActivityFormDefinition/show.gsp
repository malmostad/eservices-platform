
<%@ page import="org.motrice.coordinatrice.MtfActivityFormDefinition" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'mtfActivityFormDefinition.label', default: 'MtfActivityFormDefinition')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-mtfActivityFormDefinition" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-mtfActivityFormDefinition" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list mtfActivityFormDefinition">
			
				<g:if test="${mtfActivityFormDefinitionInst?.activityDefinitionUuid}">
				<li class="fieldcontain">
					<span id="activityDefinitionUuid-label" class="property-label"><g:message code="mtfActivityFormDefinition.activityDefinitionUuid.label" default="Activity Definition Uuid" /></span>
					
						<span class="property-value" aria-labelledby="activityDefinitionUuid-label"><g:fieldValue bean="${mtfActivityFormDefinitionInst}" field="activityDefinitionUuid"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${mtfActivityFormDefinitionInst?.formPath}">
				<li class="fieldcontain">
					<span id="formPath-label" class="property-label"><g:message code="mtfActivityFormDefinition.formPath.label" default="Form Path" /></span>
					
						<span class="property-value" aria-labelledby="formPath-label"><g:fieldValue bean="${mtfActivityFormDefinitionInst}" field="formPath"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${mtfActivityFormDefinitionInst?.startForm}">
				<li class="fieldcontain">
					<span id="startForm-label" class="property-label"><g:message code="mtfActivityFormDefinition.startForm.label" default="Start Form" /></span>
					
						<span class="property-value" aria-labelledby="startForm-label"><g:link controller="mtfStartFormDefinition" action="show" id="${mtfActivityFormDefinitionInst?.startForm?.id}">${mtfActivityFormDefinitionInst?.startForm?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${mtfActivityFormDefinitionInst?.id}" />
					<g:link class="edit" action="edit" id="${mtfActivityFormDefinitionInst?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
