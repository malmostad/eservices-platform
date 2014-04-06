<%@ page import="org.motrice.coordinatrice.pxd.PxdFormdef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'pxdFormdef.label', default: 'PxdFormdef')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-pxdFormdef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><a class="create" href="${orbeonUri}/new" target="_"><g:message code="default.new.label" args="[entityName]"/></a></li>
      </ul>
    </div>
    <div id="list-pxdFormdef" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="path" title="${message(code: 'pxdFormdef.path.label', default: 'Path')}" />
	    <th>${message(code: 'pxdFormdef.latest.published.title')}</th>
	    <th></th>
	    <g:sortableColumn property="uuid" title="${message(code: 'pxdFormdef.uuid.label', default: 'Uuid')}" />
	    <th>${message(code: 'pxdFormdef.i18n.title')}</th>
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${pxdFormdefInstList}" status="i" var="pxdFormdefInst">
	    <g:set var="latestId" value="${pubMap[pxdFormdefInst.id]?.id}"/>
	    <g:set var="latestLabel" value="${pubMap[pxdFormdefInst.id]?.version}"/>
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:link action="show" id="${pxdFormdefInst.id}">${fieldValue(bean: pxdFormdefInst, field: "path")}</g:link></td>
	      <td><g:link controller="pxdFormdefVer" action="show" id="${latestId}">${latestLabel}</g:link></td>
	      <td><a class="create" href="${orbeonUri}/edit/${pxdFormdefInst?.uuid}" target="_">
		  <g:img uri="/images/silk/application_form_edit.png" title="${message(code: 'pxdFormdef.link.edit.label', default: 'Edit')}"/>
	      </a></td>
	      <td><g:abbr text="${pxdFormdefInst?.uuid}"/></td>
	      <td><g:link controller="crdI18nFormLabel" action="listkey" id="${pxdFormdefInst?.id}">${message(code: 'pxdFormdef.i18n.start.labels.label')}</td></g:link>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${pxdFormdefInstTotal}" />
      </div>
    </div>
  </body>
</html>
