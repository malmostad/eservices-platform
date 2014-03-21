
<%@ page import="org.motrice.coordinatrice.MtfStartFormDefinition" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'mtfStartFormDefinition.label', default: 'MtfStartFormDefinition')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-mtfStartFormDefinition" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-mtfStartFormDefinition" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="authTypeReq" title="${message(code: 'mtfStartFormDefinition.authTypeReq.label', default: 'Auth Type Req')}" />
					
						<g:sortableColumn property="formPath" title="${message(code: 'mtfStartFormDefinition.formPath.label', default: 'Form Path')}" />
					
						<g:sortableColumn property="processDefinitionUuid" title="${message(code: 'mtfStartFormDefinition.processDefinitionUuid.label', default: 'Process Definition Uuid')}" />
					
						<g:sortableColumn property="userDataXpath" title="${message(code: 'mtfStartFormDefinition.userDataXpath.label', default: 'User Data Xpath')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${mtfStartFormDefinitionInstList}" status="i" var="mtfStartFormDefinitionInst">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${mtfStartFormDefinitionInst.id}">${fieldValue(bean: mtfStartFormDefinitionInst, field: "authTypeReq")}</g:link></td>
					
						<td>${fieldValue(bean: mtfStartFormDefinitionInst, field: "formPath")}</td>
					
						<td>${fieldValue(bean: mtfStartFormDefinitionInst, field: "processDefinitionUuid")}</td>
					
						<td>${fieldValue(bean: mtfStartFormDefinitionInst, field: "userDataXpath")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${mtfStartFormDefinitionInstTotal}" />
			</div>
		</div>
	</body>
</html>
