<%@ page import="org.motrice.signatrice.SigAttribute" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'sigAttribute.label', default: 'SigAttribute')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-sigAttribute" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="list-sigAttribute" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    
	    <g:sortableColumn property="name" title="${message(code: 'sigAttribute.name.label', default: 'Name')}" />
	    
	    <th><g:message code="sigAttribute.result.label" default="Result" /></th>
	    
	    <g:sortableColumn property="value" title="${message(code: 'sigAttribute.value.label', default: 'Value')}" />
	    
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${sigAttributeInstList}" status="i" var="sigAttributeInst">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      
	      <td><g:link action="show" id="${sigAttributeInst.id}">${fieldValue(bean: sigAttributeInst, field: "name")}</g:link></td>
	      
	      <td>${fieldValue(bean: sigAttributeInst, field: "result")}</td>
	      
	      <td>${fieldValue(bean: sigAttributeInst, field: "value")}</td>
	      
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${sigAttributeInstTotal}" />
      </div>
    </div>
  </body>
</html>
