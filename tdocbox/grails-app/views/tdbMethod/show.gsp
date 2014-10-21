
<%@ page import="org.motrice.tdocbox.TdbMethod" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'tdbMethod.label', default: 'TdbMethod')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-tdbMethod" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-tdbMethod" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list tdbMethod">
			
				<g:if test="${tdbMethodObj?.urlPattern}">
				<li class="fieldcontain">
					<span id="urlPattern-label" class="property-label"><g:message code="tdbMethod.urlPattern.label" default="Url Pattern" /></span>
					
						<span class="property-value" aria-labelledby="urlPattern-label"><g:fieldValue bean="${tdbMethodObj}" field="urlPattern"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${tdbMethodObj?.description}">
				<li class="fieldcontain">
					<span id="description-label" class="property-label"><g:message code="tdbMethod.description.label" default="Description" /></span>
					
						<span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${tdbMethodObj}" field="description"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${tdbMethodObj?.id}" />
					<g:link class="edit" action="edit" id="${tdbMethodObj?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
