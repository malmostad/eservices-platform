
<%@ page import="org.motrice.docbox.doc.BoxDoc" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'boxDoc.label', default: 'BoxDoc')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-boxDoc" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-boxDoc" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="docNo" title="${message(code: 'boxDoc.docNo.label', default: 'Doc No')}" />
					
						<g:sortableColumn property="formDataUuid" title="${message(code: 'boxDoc.formDataUuid.label', default: 'Form Data Uuid')}" />
					
						<g:sortableColumn property="dateCreated" title="${message(code: 'boxDoc.dateCreated.label', default: 'Date Created')}" />
					
						<g:sortableColumn property="lastUpdated" title="${message(code: 'boxDoc.lastUpdated.label', default: 'Last Updated')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${boxDocObjList}" status="i" var="boxDocObj">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${boxDocObj.id}">${fieldValue(bean: boxDocObj, field: "docNo")}</g:link></td>
					
						<td>${fieldValue(bean: boxDocObj, field: "formDataUuid")}</td>
					
						<td><g:formatDate date="${boxDocObj.dateCreated}" /></td>
					
						<td><g:formatDate date="${boxDocObj.lastUpdated}" /></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${boxDocObjTotal}" />
			</div>
		</div>
	</body>
</html>
