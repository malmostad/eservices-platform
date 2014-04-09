<%@ page import="org.motrice.migratrice.MigFormdefVer" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'migFormdefVer.label', default: 'MigFormdefVer')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-migFormdefVer" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="list-migFormdefVer" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="app" title="${message(code: 'migFormdefVer.app.label', default: 'App')}" />
	    <g:sortableColumn property="form" title="${message(code: 'migFormdefVer.form.label', default: 'Form')}" />
	    <g:sortableColumn property="path" title="${message(code: 'migFormdefVer.path.label', default: 'Path')}" />
	    <g:sortableColumn property="draft" title="${message(code: 'migFormdefVer.draft.label', default: 'Draft')}" />
	    <g:sortableColumn property="published" title="${message(code: 'migFormdefVer.published.label', default: 'Published')}" />
	    <g:sortableColumn property="title" title="${message(code: 'migFormdefVer.title.label', default: 'Title')}" />
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${migFormdefVerInstList}" status="i" var="migFormdefVerInst">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:link action="show" id="${migFormdefVerInst.id}">${fieldValue(bean: migFormdefVerInst, field: "app")}</g:link></td>
	      <td>${fieldValue(bean: migFormdefVerInst, field: "form")}</td>
	      <td>${fieldValue(bean: migFormdefVerInst, field: "path")}</td>
	      <td>${fieldValue(bean: migFormdefVerInst, field: "draft")}</td>
	      <td><g:formatBoolean boolean="${migFormdefVerInst.published}" /></td>
	      <td>${fieldValue(bean: migFormdefVerInst, field: "title")}</td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${migFormdefVerInstTotal}" />
      </div>
    </div>
  </body>
</html>
