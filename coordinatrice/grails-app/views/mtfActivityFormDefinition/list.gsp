
<%@ page import="org.motrice.coordinatrice.MtfActivityFormDefinition" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'mtfActivityFormDefinition.label', default: 'MtfActivityFormDefinition')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-mtfActivityFormDefinition" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-mtfActivityFormDefinition" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="activityDefinitionUuid" title="${message(code: 'mtfActivityFormDefinition.activityDefinitionUuid.label', default: 'Activity Definition Uuid')}" />
					
						<g:sortableColumn property="formPath" title="${message(code: 'mtfActivityFormDefinition.formPath.label', default: 'Form Path')}" />
					
						<th><g:message code="mtfActivityFormDefinition.startForm.label" default="Start Form" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${mtfActivityFormDefinitionInstList}" status="i" var="mtfActivityFormDefinitionInst">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${mtfActivityFormDefinitionInst.id}">${fieldValue(bean: mtfActivityFormDefinitionInst, field: "activityDefinitionUuid")}</g:link></td>
					
						<td>${fieldValue(bean: mtfActivityFormDefinitionInst, field: "formPath")}</td>
					
						<td>${fieldValue(bean: mtfActivityFormDefinitionInst, field: "startForm")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${mtfActivityFormDefinitionInstTotal}" />
			</div>
		</div>
	</body>
</html>
