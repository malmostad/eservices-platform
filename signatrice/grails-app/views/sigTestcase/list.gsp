
<%@ page import="org.motrice.signatrice.SigTestcase" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'sigTestcase.label', default: 'SigTestcase')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-sigTestcase" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-sigTestcase" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="name" title="${message(code: 'sigTestcase.name.label', default: 'Name')}" />
					
						<g:sortableColumn property="dateCreated" title="${message(code: 'sigTestcase.dateCreated.label', default: 'Date Created')}" />
					
						<g:sortableColumn property="personalIdNo" title="${message(code: 'sigTestcase.personalIdNo.label', default: 'Personal Id No')}" />
					
						<th><g:message code="sigTestcase.displayName.label" default="Display Name" /></th>
					
						<th><g:message code="sigTestcase.policy.label" default="Policy" /></th>
					
						<th><g:message code="sigTestcase.service.label" default="Service" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${sigTestcaseInstList}" status="i" var="sigTestcaseInst">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${sigTestcaseInst.id}">${fieldValue(bean: sigTestcaseInst, field: "name")}</g:link></td>
					
						<td><g:formatDate date="${sigTestcaseInst.dateCreated}" /></td>
					
						<td>${fieldValue(bean: sigTestcaseInst, field: "personalIdNo")}</td>
					
						<td>${fieldValue(bean: sigTestcaseInst, field: "displayName")}</td>
					
						<td>${fieldValue(bean: sigTestcaseInst, field: "policy")}</td>
					
						<td>${fieldValue(bean: sigTestcaseInst, field: "service")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${sigTestcaseInstTotal}" />
			</div>
		</div>
	</body>
</html>
