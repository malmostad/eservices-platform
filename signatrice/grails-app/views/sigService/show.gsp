
<%@ page import="org.motrice.signatrice.SigService" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'sigService.label', default: 'SigService')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-sigService" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-sigService" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list sigService">
			
				<g:if test="${sigServiceInst?.wsdlLocation}">
				<li class="fieldcontain">
					<span id="wsdlLocation-label" class="property-label"><g:message code="sigService.wsdlLocation.label" default="Service Name" /></span>
					
						<span class="property-value" aria-labelledby="wsdlLocation-label"><g:fieldValue bean="${sigServiceInst}" field="wsdlLocation"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${sigServiceInst?.qNameUri}">
				<li class="fieldcontain">
					<span id="qNameUri-label" class="property-label"><g:message code="sigService.qNameUri.label" default="QN ame Uri" /></span>
					
						<span class="property-value" aria-labelledby="qNameUri-label"><g:fieldValue bean="${sigServiceInst}" field="qNameUri"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${sigServiceInst?.qNameLocalPart}">
				<li class="fieldcontain">
					<span id="qNameLocalPart-label" class="property-label"><g:message code="sigService.qNameLocalPart.label" default="QN ame Local Part" /></span>
					
						<span class="property-value" aria-labelledby="qNameLocalPart-label"><g:fieldValue bean="${sigServiceInst}" field="qNameLocalPart"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${sigServiceInst?.cases}">
				<li class="fieldcontain">
					<span id="cases-label" class="property-label"><g:message code="sigService.cases.label" default="Cases" /></span>
					
						<g:each in="${sigServiceInst.cases}" var="c">
						<span class="property-value" aria-labelledby="cases-label"><g:link controller="sigTestcase" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${sigServiceInst?.defaultDisplayName}">
				<li class="fieldcontain">
					<span id="defaultDisplayName-label" class="property-label"><g:message code="sigService.defaultDisplayName.label" default="Default Display Name" /></span>
					
						<span class="property-value" aria-labelledby="defaultDisplayName-label"><g:link controller="sigDisplayname" action="show" id="${sigServiceInst?.defaultDisplayName?.id}">${sigServiceInst?.defaultDisplayName?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${sigServiceInst?.defaultPolicy}">
				<li class="fieldcontain">
					<span id="defaultPolicy-label" class="property-label"><g:message code="sigService.defaultPolicy.label" default="Default Policy" /></span>
					
						<span class="property-value" aria-labelledby="defaultPolicy-label"><g:link controller="sigPolicy" action="show" id="${sigServiceInst?.defaultPolicy?.id}">${sigServiceInst?.defaultPolicy?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${sigServiceInst?.id}" />
					<g:link class="edit" action="edit" id="${sigServiceInst?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
