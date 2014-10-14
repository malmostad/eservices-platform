
<%@ page import="org.motrice.tdocbox.TdbDrill" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'tdbDrill.label', default: 'TdbDrill')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-tdbDrill" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-tdbDrill" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list tdbDrill">
			
				<g:if test="${tdbDrillObj?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="tdbDrill.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${tdbDrillObj}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${tdbDrillObj?.verb}">
				<li class="fieldcontain">
					<span id="verb-label" class="property-label"><g:message code="tdbDrill.verb.label" default="Verb" /></span>
					
						<span class="property-value" aria-labelledby="verb-label"><g:link controller="tdbHttpVerb" action="show" id="${tdbDrillObj?.verb?.id}">${tdbDrillObj?.verb?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${tdbDrillObj?.mode}">
				<li class="fieldcontain">
					<span id="mode-label" class="property-label"><g:message code="tdbDrill.mode.label" default="Mode" /></span>
					
						<span class="property-value" aria-labelledby="mode-label"><g:link controller="tdbMode" action="show" id="${tdbDrillObj?.mode?.id}">${tdbDrillObj?.mode?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${tdbDrillObj?.method}">
				<li class="fieldcontain">
					<span id="method-label" class="property-label"><g:message code="tdbDrill.method.label" default="Method" /></span>
					
						<span class="property-value" aria-labelledby="method-label"><g:link controller="tdbMethod" action="show" id="${tdbDrillObj?.method?.id}">${tdbDrillObj?.method?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${tdbDrillObj?.parameters}">
				<li class="fieldcontain">
					<span id="parameters-label" class="property-label"><g:message code="tdbDrill.parameters.label" default="Parameters" /></span>
					
						<g:each in="${tdbDrillObj.parameters}" var="p">
						<span class="property-value" aria-labelledby="parameters-label"><g:link controller="tdbParameter" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${tdbDrillObj?.suite}">
				<li class="fieldcontain">
					<span id="suite-label" class="property-label"><g:message code="tdbDrill.suite.label" default="Suite" /></span>
					
						<span class="property-value" aria-labelledby="suite-label"><g:link controller="tdbSuite" action="show" id="${tdbDrillObj?.suite?.id}">${tdbDrillObj?.suite?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${tdbDrillObj?.id}" />
					<g:link class="edit" action="edit" id="${tdbDrillObj?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
