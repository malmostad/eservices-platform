<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'procdef.label', default: 'Procdef')}" />
    <g:set var="categEntityName" value="${message(code: 'crdProcCategory.label', default: 'CrdProcCategory')}" />
    <title><g:message code="config.list.title"/></title>
  </head>
  <body>
    <div id="list-procdef" class="content scaffold-list" role="main">
      <h1><g:message code="config.list.title"/></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <th><g:message code="config.name.label" default="Name"/></th>
	    <th><g:message code="config.value.label" default="Value"/></th>
	    <th><g:message code="config.liveness.label" default="Live"/></th>
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${config}" status="i" var="entry">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:message code="${entry?.name}" default="${entry?.name}"/></td>
	      <td>${entry?.value?.encodeAsHTML()}</td>
	      <td><g:img uri="${entry?.img}" title="${entry?.title}"/></td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
    </div>
  </body>
</html>
