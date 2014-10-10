
<%@ page import="org.motrice.tdocbox.TdbDrill" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'tdbDrill.label', default: 'TdbDrill')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-tdbDrill" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-tdbDrill" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="name" title="${message(code: 'tdbDrill.name.label', default: 'Name')}" />
					
						<th><g:message code="tdbDrill.verb.label" default="Verb" /></th>
					
						<th><g:message code="tdbDrill.method.label" default="Method" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${tdbDrillObjList}" status="i" var="tdbDrillObj">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${tdbDrillObj.id}">${fieldValue(bean: tdbDrillObj, field: "name")}</g:link></td>
					
						<td>${fieldValue(bean: tdbDrillObj, field: "verb")}</td>
					
						<td>${fieldValue(bean: tdbDrillObj, field: "method")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${tdbDrillObjTotal}" />
			</div>
		</div>
	</body>
</html>
