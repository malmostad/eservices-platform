
<%@ page import="org.motrice.docbox.doc.BoxDocStep" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'boxDocStep.label', default: 'BoxDocStep')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-boxDocStep" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-boxDocStep" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="step" title="${message(code: 'boxDocStep.step.label', default: 'Step')}" />
					
						<g:sortableColumn property="docNo" title="${message(code: 'boxDocStep.docNo.label', default: 'Doc No')}" />
					
						<g:sortableColumn property="signCount" title="${message(code: 'boxDocStep.signCount.label', default: 'Sign Count')}" />
					
						<g:sortableColumn property="dateCreated" title="${message(code: 'boxDocStep.dateCreated.label', default: 'Date Created')}" />
					
						<g:sortableColumn property="lastUpdated" title="${message(code: 'boxDocStep.lastUpdated.label', default: 'Last Updated')}" />
					
						<th><g:message code="boxDocStep.doc.label" default="Doc" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${boxDocStepObjList}" status="i" var="boxDocStepObj">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${boxDocStepObj.id}">${fieldValue(bean: boxDocStepObj, field: "step")}</g:link></td>
					
						<td>${fieldValue(bean: boxDocStepObj, field: "docNo")}</td>
					
						<td>${fieldValue(bean: boxDocStepObj, field: "signCount")}</td>
					
						<td><g:formatDate date="${boxDocStepObj.dateCreated}" /></td>
					
						<td><g:formatDate date="${boxDocStepObj.lastUpdated}" /></td>
					
						<td>${fieldValue(bean: boxDocStepObj, field: "doc")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${boxDocStepObjTotal}" />
			</div>
		</div>
	</body>
</html>
