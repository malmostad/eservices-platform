<%@ page import="org.motrice.coordinatrice.ProcDef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'procDef.label', default: 'ProcDef')}" />
      <g:set var="formEntityName" value="${message(code: 'pxdFormdef.label', default: 'PxdFormdef')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-procDef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="list" controller="PxdFormdef" action="list"><g:message code="default.list.label" args="[formEntityName]" /></g:link></li>
      </ul>
    </div>
    <div id="list-procDef" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="name" title="${message(code: 'procDef.name.label', default: 'Name')}" />
	    <g:sortableColumn property="vno" title="${message(code: 'procDef.vno.label', default: 'Version')}" />
	    <g:sortableColumn property="type" title="${message(code: 'procDef.type.label', default: 'Type')}" />
	    <g:sortableColumn property="deployedTime" title="${message(code: 'procDef.deployedTime.label', default: 'Deployed')}" />
	    <g:sortableColumn property="state" title="${message(code: 'procDef.state.label', default: 'State')}" />
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${procDefInstList}" status="i" var="procDefInst">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:link action="show" id="${procDefInst.uuid}">${fieldValue(bean: procDefInst, field: "name")}</g:link></td>
	      <td>${fieldValue(bean: procDefInst, field: "vno")}</td>
	      <td>${fieldValue(bean: procDefInst, field: "type")}</td>
	      <td>${fieldValue(bean: procDefInst, field: "deployedTimeStr")}</td>
	      <td><g:pdefstate state="${procDefInst?.state}"/></td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${procDefInstTotal}" />
      </div>
    </div>
  </body>
</html>
