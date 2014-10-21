<%@ page import="org.motrice.tdocbox.TdbParameter" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'tdbParameter.label', default: 'TdbParameter')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-tdbParameter" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="list-tdbParameter" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="name" title="${message(code: 'tdbParameter.name.label', default: 'Name')}" />
	    <g:sortableColumn property="value" title="${message(code: 'tdbParameter.value.label', default: 'Value')}" />
	    <g:sortableColumn property="description" title="${message(code: 'tdbParameter.description.label', default: 'Description')}" />
	    <th><g:message code="tdbParameter.drill.label" default="Drill" /></th>
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${tdbParameterObjList}" status="i" var="tdbParameterObj">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:link action="show" id="${tdbParameterObj.id}">${fieldValue(bean: tdbParameterObj, field: "name")}</g:link></td>
	      <td>${fieldValue(bean: tdbParameterObj, field: "value")}</td>
	      <td>${fieldValue(bean: tdbParameterObj, field: "description")}</td>
	      <td>${fieldValue(bean: tdbParameterObj, field: "drill")}</td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${tdbParameterObjTotal}" />
      </div>
    </div>
  </body>
</html>
