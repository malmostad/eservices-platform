
<%@ page import="org.motrice.coordinatrice.MtfProcessActivityFormInstance" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'mtfProcessActivityFormInstance.label', default: 'MtfProcessActivityFormInstance')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-mtfProcessActivityFormInstance" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-mtfProcessActivityFormInstance" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="activityInstanceUuid" title="${message(code: 'mtfProcessActivityFormInstance.activityInstanceUuid.label', default: 'Activity Instance Uuid')}" />
					
						<g:sortableColumn property="formDocId" title="${message(code: 'mtfProcessActivityFormInstance.formDocId.label', default: 'Form Doc Id')}" />
					
						<g:sortableColumn property="formPath" title="${message(code: 'mtfProcessActivityFormInstance.formPath.label', default: 'Form Path')}" />
					
						<g:sortableColumn property="processInstanceUuid" title="${message(code: 'mtfProcessActivityFormInstance.processInstanceUuid.label', default: 'Process Instance Uuid')}" />
					
						<g:sortableColumn property="submitted" title="${message(code: 'mtfProcessActivityFormInstance.submitted.label', default: 'Submitted')}" />
					
						<g:sortableColumn property="userid" title="${message(code: 'mtfProcessActivityFormInstance.userid.label', default: 'Userid')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${mtfProcessActivityFormInstanceInstList}" status="i" var="mtfProcessActivityFormInstanceInst">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${mtfProcessActivityFormInstanceInst.id}">${fieldValue(bean: mtfProcessActivityFormInstanceInst, field: "activityInstanceUuid")}</g:link></td>
					
						<td>${fieldValue(bean: mtfProcessActivityFormInstanceInst, field: "formDocId")}</td>
					
						<td>${fieldValue(bean: mtfProcessActivityFormInstanceInst, field: "formPath")}</td>
					
						<td>${fieldValue(bean: mtfProcessActivityFormInstanceInst, field: "processInstanceUuid")}</td>
					
						<td><g:formatDate date="${mtfProcessActivityFormInstanceInst.submitted}" /></td>
					
						<td>${fieldValue(bean: mtfProcessActivityFormInstanceInst, field: "userid")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${mtfProcessActivityFormInstanceInstTotal}" />
			</div>
		</div>
	</body>
</html>
