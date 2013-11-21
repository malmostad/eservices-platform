
<%@ page import="org.motrice.orifice.OriFormdefVer" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'oriFormdefVer.label', default: 'OriFormdefVer')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-oriFormdefVer" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-oriFormdefVer" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="app" title="${message(code: 'oriFormdefVer.app.label', default: 'App')}" />
					
						<g:sortableColumn property="form" title="${message(code: 'oriFormdefVer.form.label', default: 'Form')}" />
					
						<g:sortableColumn property="path" title="${message(code: 'oriFormdefVer.path.label', default: 'Path')}" />
					
						<g:sortableColumn property="draft" title="${message(code: 'oriFormdefVer.draft.label', default: 'Draft')}" />
					
						<g:sortableColumn property="published" title="${message(code: 'oriFormdefVer.published.label', default: 'Published')}" />
					
						<g:sortableColumn property="title" title="${message(code: 'oriFormdefVer.title.label', default: 'Title')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${oriFormdefVerInstList}" status="i" var="oriFormdefVerInst">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${oriFormdefVerInst.id}">${fieldValue(bean: oriFormdefVerInst, field: "app")}</g:link></td>
					
						<td>${fieldValue(bean: oriFormdefVerInst, field: "form")}</td>
					
						<td>${fieldValue(bean: oriFormdefVerInst, field: "path")}</td>
					
						<td>${fieldValue(bean: oriFormdefVerInst, field: "draft")}</td>
					
						<td><g:formatBoolean boolean="${oriFormdefVerInst.published}" /></td>
					
						<td>${fieldValue(bean: oriFormdefVerInst, field: "title")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${oriFormdefVerInstTotal}" />
			</div>
		</div>
	</body>
</html>
