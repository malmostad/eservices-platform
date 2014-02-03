<%@ page import="org.motrice.coordinatrice.Procdef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'procdef.label', default: 'Procdef')}" />
    <g:set var="formEntityName" value="${message(code: 'pxdFormdef.label', default: 'PxdFormdef')}" />
    <g:set var="categEntityName" value="${message(code: 'crdProcCategory.label', default: 'CrdProcCategory')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-procdef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="list" controller="PxdFormdef" action="list"><g:message code="default.list.label" args="[formEntityName]" /></g:link></li>
	<li><g:link class="list" controller="crdProcCategory" action="list"><g:message code="default.list.label" args="[categEntityName]" /></g:link></li>
      </ul>
    </div>
    <div id="list-procdef" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="name" title="${message(code: 'procdef.name.label', default: 'Name')}" />
	    <g:sortableColumn property="versions" title="${message(code: 'procdef.versions.label', default: 'Versions')}" />
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${procdefList}" status="i" var="procdefInfo">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:link action="listname" id="${procdefInfo.key}">${procdefInfo.name}</g:link></td>
	      <td>${procdefInfo.versions}</td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${procdefTotal}" />
      </div>
    </div>
  </body>
</html>
