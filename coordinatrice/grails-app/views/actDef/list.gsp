<%@ page import="org.motrice.coordinatrice.ActDef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'actDef.label', default: 'ActDef')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-actDef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
      </ul>
    </div>
    <div id="list-actDef" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="uuid" title="${message(code: 'actDef.uuid.label', default: 'Uuid')}" />
	    <g:sortableColumn property="label" title="${message(code: 'actDef.label.label', default: 'Label')}" />
	    <g:sortableColumn property="type" title="${message(code: 'actDef.type.label', default: 'Type')}" />
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${actDefInstList}" status="i" var="actDefInst">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:link action="show" id="${actDefInst.id}">${fieldValue(bean: actDefInst, field: "uuid")}</g:link></td>
	      <td>${fieldValue(bean: actDefInst, field: "label")}</td>
	      <td>${fieldValue(bean: actDefInst, field: "type")}</td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${actDefInstTotal}" />
      </div>
    </div>
  </body>
</html>
