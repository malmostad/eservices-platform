
<%@ page import="org.motrice.docbox.doc.BoxContents" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'boxContents.label', default: 'BoxContents')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-boxContents" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-boxContents" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="name" title="${message(code: 'boxContents.name.label', default: 'Name')}" />
					
						<g:sortableColumn property="dateCreated" title="${message(code: 'boxContents.dateCreated.label', default: 'Date Created')}" />
					
						<g:sortableColumn property="lastUpdated" title="${message(code: 'boxContents.lastUpdated.label', default: 'Last Updated')}" />
					
						<g:sortableColumn property="format" title="${message(code: 'boxContents.format.label', default: 'Format')}" />
					
						<g:sortableColumn property="size" title="${message(code: 'boxContents.size.label', default: 'Size')}" />
					
						<g:sortableColumn property="text" title="${message(code: 'boxContents.text.label', default: 'Text')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${boxContentsObjList}" status="i" var="boxContentsObj">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${boxContentsObj.id}">${fieldValue(bean: boxContentsObj, field: "name")}</g:link></td>
					
						<td><g:formatDate date="${boxContentsObj.dateCreated}" /></td>
					
						<td><g:formatDate date="${boxContentsObj.lastUpdated}" /></td>
					
						<td>${fieldValue(bean: boxContentsObj, field: "format")}</td>
					
						<td>${fieldValue(bean: boxContentsObj, field: "size")}</td>
					
						<td>${fieldValue(bean: boxContentsObj, field: "text")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${boxContentsObjTotal}" />
			</div>
		</div>
	</body>
</html>
