<%@ page import="org.motrice.coordinatrice.bonita.BnActDef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'bnActDef.label', default: 'BnActDef')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-bnActDef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
      </ul>
    </div>
    <div id="list-bnActDef" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    
	    <g:sortableColumn property="uuid" title="${message(code: 'bnActDef.uuid.label', default: 'Uuid')}" />
	    
	    <g:sortableColumn property="name" title="${message(code: 'bnActDef.name.label', default: 'Name')}" />
	    
	    <g:sortableColumn property="label" title="${message(code: 'bnActDef.label.label', default: 'Label')}" />
	    
	    <g:sortableColumn property="type" title="${message(code: 'bnActDef.type.label', default: 'Type')}" />
	    
	    <th><g:message code="bnActDef.process.label" default="Process" /></th>
	    
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${bnActDefInstList}" status="i" var="bnActDefInst">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      
	      <td><g:link action="show" id="${bnActDefInst.id}">${fieldValue(bean: bnActDefInst, field: "uuid")}</g:link></td>
	      
	      <td>${fieldValue(bean: bnActDefInst, field: "name")}</td>
	      
	      <td>${fieldValue(bean: bnActDefInst, field: "label")}</td>
	      
	      <td>${fieldValue(bean: bnActDefInst, field: "type")}</td>
	      
	      <td>${fieldValue(bean: bnActDefInst, field: "process")}</td>
	      
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${bnActDefInstTotal}" />
      </div>
    </div>
  </body>
</html>
