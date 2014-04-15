<%@ page import="org.motrice.coordinatrice.MtfStartFormDefinition" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'mtfStartFormDefinition.label', default: 'MtfStartFormDefinition')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-mtfStartFormDefinition" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="list-mtfStartFormDefinition" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="formPath" title="${message(code: 'startform.form.label')}"/>
	    <th>${message(code: 'pxdFormdef.i18n.title')}</th>
	    <g:sortableColumn property="procdefId" title="${message(code: 'procdef.label')}"/>
	    <th>${message(code: 'procdef.vno.label')}</th>
	    <th>${message(code: 'procdef.state.label')}</th>
	    <th></th>
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${startFormdefList}" status="i" var="startFormdefInst">
	    <g:set var="procdefId" value="${startFormdefInst?.procdefId}"/>
	    <g:set var="startFormChangeAllowed" value="${startFormdefInst?.tmpProcdef?.state?.startFormChangeAllowed}"/>
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:link controller="pxdFormdefVer" action="show" id="${startFormdefInst?.formdefId}">${fieldValue(bean: startFormdefInst, field: "formConnectionKey")}</g:link></td>
	      <td><g:link controller="crdI18nFormLabel" action="listkey" id="${formdefMap[startFormdefInst?.id]?.id}">
		  <g:img uri="/images/silk/tag_blue.png" title="${message(code: 'pxdFormdef.i18n.start.labels.label')}"/>
	      </g:link></td>
	      <td><g:link controller="procdef" action="show" id="${procdefId}">${startFormdefInst?.tmpProcdef?.nameOrKey?.encodeAsHTML()}</g:link></td>
	      <td>${startFormdefInst?.tmpProcdef?.vno}</td>
	      <td><g:pdefstate state="${startFormdefInst?.tmpProcdef?.state}"/></td>
	      <g:if test="${startFormChangeAllowed}">
		<td><g:link action="delete" id="${startFormdefInst?.id}" params="[procdefId: procdefId]"><g:img uri="/images/silk/delete.png" title="${message(code: 'startform.disconnect.label')}"/></g:link></td>
	      </g:if>
	      <g:else>
		<td> </td>
	      </g:else>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${startFormdefTotal}" />
      </div>
    </div>
  </body>
</html>
