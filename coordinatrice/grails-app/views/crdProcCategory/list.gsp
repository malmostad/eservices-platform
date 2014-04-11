<%@ page import="org.motrice.coordinatrice.CrdProcCategory" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'crdProcCategory.label', default: 'CrdProcCategory')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-crdProcCategory" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="list-crdProcCategory" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="name" title="${message(code: 'crdProcCategory.name.label', default: 'Name')}" />
	    <g:sortableColumn property="description" title="${message(code: 'crdProcCategory.description.label', default: 'Description')}" />
	    <g:sortableColumn property="dateCreated" title="${message(code: 'crdProcCategory.dateCreated.label', default: 'Date Created')}" />
	    <g:sortableColumn property="lastUpdated" title="${message(code: 'crdProcCategory.lastUpdated.label', default: 'Last Updated')}" />
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${crdProcCategoryInstList}" status="i" var="crdProcCategoryInst">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:link action="show" id="${crdProcCategoryInst.id}">${fieldValue(bean: crdProcCategoryInst, field: "name")}</g:link></td>
	      <td>${fieldValue(bean: crdProcCategoryInst, field: "description")}</td>
	      <td><g:formatDate date="${crdProcCategoryInst.dateCreated}" /></td>
	      <td><g:formatDate date="${crdProcCategoryInst.lastUpdated}" /></td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${crdProcCategoryInstTotal}" />
      </div>
    </div>
  </body>
</html>
