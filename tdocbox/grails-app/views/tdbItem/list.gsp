
<%@ page import="org.motrice.tdocbox.TdbItem" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'tdbItem.label', default: 'TdbItem')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-tdbItem" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-tdbItem" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="binaryFlag" title="${message(code: 'tdbItem.binaryFlag.label', default: 'Binary Flag')}" />
					
						<g:sortableColumn property="text" title="${message(code: 'tdbItem.text.label', default: 'Text')}" />
					
						<g:sortableColumn property="bytes" title="${message(code: 'tdbItem.bytes.label', default: 'Bytes')}" />
					
						<th><g:message code="tdbItem.case.label" default="Case" /></th>
					
						<g:sortableColumn property="name" title="${message(code: 'tdbItem.name.label', default: 'Name')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${tdbItemObjList}" status="i" var="tdbItemObj">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${tdbItemObj.id}">${fieldValue(bean: tdbItemObj, field: "binaryFlag")}</g:link></td>
					
						<td>${fieldValue(bean: tdbItemObj, field: "text")}</td>
					
						<td>${fieldValue(bean: tdbItemObj, field: "bytes")}</td>
					
						<td>${fieldValue(bean: tdbItemObj, field: "case")}</td>
					
						<td>${fieldValue(bean: tdbItemObj, field: "name")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${tdbItemObjTotal}" />
			</div>
		</div>
	</body>
</html>
