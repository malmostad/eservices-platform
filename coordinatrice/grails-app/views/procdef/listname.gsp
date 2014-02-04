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
	    <g:sortableColumn property="vno" title="${message(code: 'procdef.vno.label', default: 'Version')}" />
	    <g:sortableColumn property="state" title="${message(code: 'procdef.state.label', default: 'State')}" />
	    <g:sortableColumn property="category" title="${message(code: 'procdef.category.label', default: 'Categ')}" />
	    <g:sortableColumn property="deployedTime" title="${message(code: 'procdef.deployedTime.label', default: 'Deployed')}" />
	    <th></th>
	    <th></th>
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${procdefInstList}" status="i" var="procdefInst">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:link action="show" id="${procdefInst.uuid}">${fieldValue(bean: procdefInst, field: "name")}</g:link></td>
	      <td>${fieldValue(bean: procdefInst, field: "vno")}</td>
	      <td><g:pdefstate state="${procdefInst?.state}"/></td>
	      <td>${fieldValue(bean: procdefInst, field: "category")}</td>
	      <td>${fieldValue(bean: procdefInst, field: "deployedTimeStr")}</td>
	      <td><g:link action="diagramDownload" id="${procdefInst.uuid}"><g:img uri="/images/silk/camera.png" title="${message(code: 'procdef.diagram.label', default: 'Diagram')}"/></g:link></td>
	      <td><g:link action="xmlDownload" id="${procdefInst.uuid}"><g:img uri="/images/silk/map_edit.png" title="${message(code: 'procdef.xml.label', default: 'BPMN')}"/></g:link></td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate id="${procdefKey}" total="${procdefInstTotal}" />
      </div>
    </div>
  </body>
</html>
