<%@ page import="org.motrice.postxdb.PxdItem" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'pxdItem.label', default: 'PxdItem')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
    </meta>
  </head>
  <body>
    <a href="#list-pxdItem" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
      </ul>
    </div>
    <div id="list-pxdItem" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="path" title="${message(code: 'pxdItem.path.label', default: 'Path')}" />
	    <g:sortableColumn property="instance" title="${message(code: 'pxdItem.instance.label', default: 'Format')}" />
	    <g:sortableColumn property="format" title="${message(code: 'pxdItem.format.label', default: 'Format')}" />
	    <g:sortableColumn property="uuid" title="${message(code: 'pxdItem.uuid.label', default: 'Uuid')}" />
	    <g:sortableColumn property="formDef" title="${message(code: 'pxdItem.formDef.label', default: 'Form Def')}" />
	    <g:sortableColumn property="dateCreated" title="${message(code: 'pxdItem.dateCreated.label', default: 'Date Created')}" />
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${pxdItemObjList}" status="i" var="pxdItemObj">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:link action="show" id="${pxdItemObj.id}">${fieldValue(bean: pxdItemObj, field: "path")}</g:link></td>
	      <td><g:instflag flag="${pxdItemObj?.instance}"/></td>
	      <td>${fieldValue(bean: pxdItemObj, field: "format")}</td>
	      <td>${fieldValue(bean: pxdItemObj, field: "uuid")}</td>
	      <td>${fieldValue(bean: pxdItemObj, field: "formDef")}</td>
	      <td><g:formatDate date="${pxdItemObj.dateCreated}" /></td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${pxdItemObjTotal}" />
      </div>
    </div>
  </body>
</html>
