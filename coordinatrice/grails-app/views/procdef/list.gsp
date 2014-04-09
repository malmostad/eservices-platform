<%@ page import="org.motrice.coordinatrice.Procdef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'procdef.label', default: 'Procdef')}" />
    <g:set var="categEntityName" value="${message(code: 'crdProcCategory.label', default: 'CrdProcCategory')}" />
    <title><g:message code="procdef.list.label"/></title>
  </head>
  <body>
    <div id="list-procdef" class="content scaffold-list" role="main">
      <h1><g:message code="procdef.list.label"/></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="name" title="${message(code: 'procdef.name.label', default: 'Name')}" />
	    <g:sortableColumn property="versions" title="${message(code: 'procdef.versions.label', default: 'Versions')}" />
	    <th><g:message code="procdef.i18n.labels.title" default="Intl"/></th>
	    <th><g:message code="procdef.i18n.guides.title" default="Intl"/></th>
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${procdefList}" status="i" var="procdefInfo">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:link action="listname" id="${procdefInfo.key}">${procdefInfo.name}</g:link></td>
	      <td>${procdefInfo.versions}</td>
	      <td><g:link controller="crdI18nActLabel" action="listkey" id="${procdefInfo.key}">
		  <g:message code="procdef.i18n.act.labels.label" default="See labels"/>
	      </g:link></td>
	      <td><g:link controller="crdI18nGuideUrl" action="listkey" id="${procdefInfo.key}">
		  <g:message code="procdef.i18n.guides.label" default="See labels"/>
	      </g:link></td>
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
