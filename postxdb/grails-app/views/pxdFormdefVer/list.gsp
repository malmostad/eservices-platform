<%@ page import="org.motrice.postxdb.PxdFormdefVer" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'pxdFormdefVer.label', default: 'PxdFormdefVer')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
    </meta>
  </head>
  <body>
    <a href="#list-pxdFormdefVer" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="list" controller="pxdItem" action="list"><g:message code="pxdItem.list.label"/></g:link></li>
      </ul>
    </div>
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
	    <g:sortableColumn property="dateCreated" title="${message(code: 'pxdFormdefVer.dateCreated.label', default: 'Date Created')}" />
	    <g:sortableColumn property="lastUpdated" title="${message(code: 'pxdFormdefVer.lastUpdated.label', default: 'Date Created')}" />
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${pxdFormdefVerObjList}" status="i" var="pxdFormdefVerObj">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:link action="show" id="${pxdFormdefVerObj.id}">${fieldValue(bean: pxdFormdefVerObj, field: "path")}</g:link></td>
	      <td>${fieldValue(bean: pxdFormdefVerObj, field: "title")}</td>
	      <td><g:formatDate date="${pxdFormdefVerObj.dateCreated}" /></td>
	      <td><g:formatDate date="${pxdFormdefVerObj.lastUpdated}" /></td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${pxdFormdefVerObjTotal}" />
      </div>
    </div>
  </body>
</html>
