<%@ page import="org.motrice.coordinatrice.pxd.PxdFormdefVer" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'pxdFormdefVer.label', default: 'PxdFormdefVer')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-pxdFormdefVer" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="list-pxdFormdefVer" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="path" title="${message(code: 'pxdFormdefVer.path.label', default: 'Path')}" />
	    <g:sortableColumn property="title" title="${message(code: 'pxdFormdefVer.title.label', default: 'Title')}" />
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${pxdFormdefVerInstList}" status="i" var="pxdFormdefVerInst">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:link action="show" id="${pxdFormdefVerInst.id}">${fieldValue(bean: pxdFormdefVerInst, field: "path")}</g:link></td>
	      <td>${fieldValue(bean: pxdFormdefVerInst, field: "title")}</td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${pxdFormdefVerInstTotal}" />
      </div>
    </div>
  </body>
</html>
