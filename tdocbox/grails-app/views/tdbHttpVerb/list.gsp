
<%@ page import="org.motrice.tdocbox.TdbHttpVerb" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'tdbHttpVerb.label', default: 'TdbHttpVerb')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-tdbHttpVerb" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-tdbHttpVerb" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="verb" title="${message(code: 'tdbHttpVerb.verb.label', default: 'Verb')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${tdbHttpVerbObjList}" status="i" var="tdbHttpVerbObj">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${tdbHttpVerbObj.id}">${fieldValue(bean: tdbHttpVerbObj, field: "verb")}</g:link></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${tdbHttpVerbObjTotal}" />
			</div>
		</div>
	</body>
</html>
