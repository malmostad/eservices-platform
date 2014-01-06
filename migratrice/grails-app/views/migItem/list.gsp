
<%@ page import="org.motrice.migratrice.MigItem" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'migItem.label', default: 'MigItem')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-migItem" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-migItem" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="formref" title="${message(code: 'migItem.formref.label', default: 'Formref')}" />
					
						<g:sortableColumn property="path" title="${message(code: 'migItem.path.label', default: 'Path')}" />
					
						<g:sortableColumn property="uuid" title="${message(code: 'migItem.uuid.label', default: 'Uuid')}" />
					
						<g:sortableColumn property="formDef" title="${message(code: 'migItem.formDef.label', default: 'Form Def')}" />
					
						<g:sortableColumn property="format" title="${message(code: 'migItem.format.label', default: 'Format')}" />
					
						<g:sortableColumn property="size" title="${message(code: 'migItem.size.label', default: 'Size')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${migItemInstList}" status="i" var="migItemInst">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${migItemInst.id}">${fieldValue(bean: migItemInst, field: "formref")}</g:link></td>
					
						<td>${fieldValue(bean: migItemInst, field: "path")}</td>
					
						<td>${fieldValue(bean: migItemInst, field: "uuid")}</td>
					
						<td>${fieldValue(bean: migItemInst, field: "formDef")}</td>
					
						<td>${fieldValue(bean: migItemInst, field: "format")}</td>
					
						<td>${fieldValue(bean: migItemInst, field: "size")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${migItemInstTotal}" />
			</div>
		</div>
	</body>
</html>
