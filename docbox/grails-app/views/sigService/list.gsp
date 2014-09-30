<%@ page import="org.motrice.signatrice.SigService" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'sigService.label', default: 'SigService')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-sigService" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="list-sigService" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="alias" title="${message(code: 'sigService.alias.label', default: 'Alias')}" />
	    <th><g:message code="sigService.defaultDisplayName.label" default="Default Display Name" /></th>
	    <th><g:message code="sigService.defaultPolicy.label" default="Default Policy" /></th>
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${sigServiceInstList}" status="i" var="sigServiceInst">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:link action="show" id="${sigServiceInst.id}">${fieldValue(bean: sigServiceInst, field: "alias")}</g:link></td>
	      <td>${fieldValue(bean: sigServiceInst, field: "defaultDisplayName")}</td>
	      <td>${fieldValue(bean: sigServiceInst, field: "defaultPolicy")}</td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${sigServiceInstTotal}" />
      </div>
    </div>
  </body>
</html>
