
<%@ page import="org.motrice.signatrice.audit.AuditRecord" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'auditRecord.label', default: 'AuditRecord')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-auditRecord" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="list-auditRecord" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="dateCreated" title="${message(code: 'auditRecord.dateCreated.label', default: 'Date Created')}" />
	    <g:sortableColumn property="eventType" title="${message(code: 'auditRecord.eventType.label', default: 'Event Type')}" />
	    <g:sortableColumn property="description" title="${message(code: 'auditRecord.description.label', default: 'Description')}" />
	    <g:sortableColumn property="remoteAddr" title="${message(code: 'auditRecord.remoteAddr.label', default: 'Remote Addr')}" />
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${auditRecordInstList}" status="i" var="auditRecordInst">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:link action="show" id="${auditRecordInst.id}">${fieldValue(bean: auditRecordInst, field: "dateCreated")}</g:link></td>
	      <g:set var="icon"><g:failure flag="${auditRecordInst.failure}"/></g:set>
	      <td><g:img dir="images/silk" file="${icon}"/>&nbsp;&nbsp;${fieldValue(bean: auditRecordInst, field: "eventType")}</td>
	      <td>${fieldValue(bean: auditRecordInst, field: "description")}</td>
	      <td>${fieldValue(bean: auditRecordInst, field: "remoteAddr")}</td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${auditRecordInstTotal}" />
      </div>
    </div>
  </body>
</html>
