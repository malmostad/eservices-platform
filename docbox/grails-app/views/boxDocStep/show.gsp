
<%@ page import="org.motrice.docbox.doc.BoxDocStep" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'boxDocStep.label', default: 'BoxDocStep')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-boxDocStep" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-boxDocStep" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list boxDocStep">
			
				<g:if test="${boxDocStepObj?.step}">
				<li class="fieldcontain">
					<span id="step-label" class="property-label"><g:message code="boxDocStep.step.label" default="Step" /></span>
					
						<span class="property-value" aria-labelledby="step-label"><g:fieldValue bean="${boxDocStepObj}" field="step"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${boxDocStepObj?.docNo}">
				<li class="fieldcontain">
					<span id="docNo-label" class="property-label"><g:message code="boxDocStep.docNo.label" default="Doc No" /></span>
					
						<span class="property-value" aria-labelledby="docNo-label"><g:fieldValue bean="${boxDocStepObj}" field="docNo"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${boxDocStepObj?.signCount}">
				<li class="fieldcontain">
					<span id="signCount-label" class="property-label"><g:message code="boxDocStep.signCount.label" default="Sign Count" /></span>
					
						<span class="property-value" aria-labelledby="signCount-label"><g:fieldValue bean="${boxDocStepObj}" field="signCount"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${boxDocStepObj?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="boxDocStep.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${boxDocStepObj?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${boxDocStepObj?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="boxDocStep.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${boxDocStepObj?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${boxDocStepObj?.contents}">
				<li class="fieldcontain">
					<span id="contents-label" class="property-label"><g:message code="boxDocStep.contents.label" default="Contents" /></span>
					
						<g:each in="${boxDocStepObj.contents}" var="c">
						<span class="property-value" aria-labelledby="contents-label"><g:link controller="boxContents" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${boxDocStepObj?.doc}">
				<li class="fieldcontain">
					<span id="doc-label" class="property-label"><g:message code="boxDocStep.doc.label" default="Doc" /></span>
					
						<span class="property-value" aria-labelledby="doc-label"><g:link controller="boxDoc" action="show" id="${boxDocStepObj?.doc?.id}">${boxDocStepObj?.doc?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${boxDocStepObj?.docboxRef}">
				<li class="fieldcontain">
					<span id="docboxRef-label" class="property-label"><g:message code="boxDocStep.docboxRef.label" default="Docbox Ref" /></span>
					
						<span class="property-value" aria-labelledby="docboxRef-label"><g:fieldValue bean="${boxDocStepObj}" field="docboxRef"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${boxDocStepObj?.id}" />
					<g:link class="edit" action="edit" id="${boxDocStepObj?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
