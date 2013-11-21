
<%@ page import="org.motrice.orifice.OriItem" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'oriItem.label', default: 'OriItem')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-oriItem" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-oriItem" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="formref" title="${message(code: 'oriItem.formref.label', default: 'Formref')}" />
					
						<g:sortableColumn property="path" title="${message(code: 'oriItem.path.label', default: 'Path')}" />
					
						<g:sortableColumn property="uuid" title="${message(code: 'oriItem.uuid.label', default: 'Uuid')}" />
					
						<g:sortableColumn property="formDef" title="${message(code: 'oriItem.formDef.label', default: 'Form Def')}" />
					
						<g:sortableColumn property="format" title="${message(code: 'oriItem.format.label', default: 'Format')}" />
					
						<g:sortableColumn property="size" title="${message(code: 'oriItem.size.label', default: 'Size')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${oriItemInstList}" status="i" var="oriItemInst">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${oriItemInst.id}">${fieldValue(bean: oriItemInst, field: "formref")}</g:link></td>
					
						<td>${fieldValue(bean: oriItemInst, field: "path")}</td>
					
						<td>${fieldValue(bean: oriItemInst, field: "uuid")}</td>
					
						<td>${fieldValue(bean: oriItemInst, field: "formDef")}</td>
					
						<td>${fieldValue(bean: oriItemInst, field: "format")}</td>
					
						<td>${fieldValue(bean: oriItemInst, field: "size")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${oriItemInstTotal}" />
			</div>
		</div>
	</body>
</html>
