<%@ page import="org.motrice.signatrice.SigDefaultScheme" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'sigDefaultScheme.label', default: 'SigDefaultScheme')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-sigDefaultScheme" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="list-sigDefaultScheme" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <th><g:message code="sigDefaultScheme.defaultScheme.label" default="Default Scheme" /></th>
	    <g:sortableColumn property="lastUpdated" title="${message(code: 'sigDefaultScheme.lastUpdated.label', default: 'Last Updated')}" />
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${sigDefaultSchemeObjList}" status="i" var="sigDefaultSchemeObj">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:link action="show" id="${sigDefaultSchemeObj.id}">${fieldValue(bean: sigDefaultSchemeObj, field: "defaultScheme")}</g:link></td>
	      <td><g:tstamp date="${sigDefaultSchemeObj.lastUpdated}" /></td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${sigDefaultSchemeObjTotal}" />
      </div>
    </div>
  </body>
</html>
