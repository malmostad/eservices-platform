<%@ page import="org.motrice.signatrice.audit.AudEventRecord" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'audEventRecord.label', default: 'EventRecord')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-eventRecord" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="list-eventRecord" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="dateCreated" title="${message(code: 'audEventRecord.dateCreated.label', default: 'Date Created')}" />
	    <g:sortableColumn property="eventType" title="${message(code: 'audEventRecord.eventType.label', default: 'Event Type')}" />
	    <g:sortableColumn property="description" title="${message(code: 'audEventRecord.description.description', default: 'Description')}" />
	    <g:sortableColumn property="label" title="${message(code: 'audEventRecord.label.label', default: 'Label')}" />
	    <g:sortableColumn property="remoteAddr" title="${message(code: 'audEventRecord.remoteAddr.label', default: 'Remote Addr')}" />
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${eventRecordInstList}" status="i" var="eventRecordInst">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:link action="show" id="${eventRecordInst.id}"><g:formatDate format="yyyy-MM-dd HH:mm:ss" date="${eventRecordInst?.dateCreated}"/></g:link></td>
	      <g:set var="icon"><g:failure flag="${eventRecordInst.failure}"/></g:set>
	      <td><g:img dir="images/silk" file="${icon}"/>&nbsp;&nbsp;${fieldValue(bean: eventRecordInst, field: "eventType")}</td>
	      <td>${fieldValue(bean: eventRecordInst, field: "description")}</td>
	      <td>${fieldValue(bean: eventRecordInst, field: "label")}</td>
	      <td>${fieldValue(bean: eventRecordInst, field: "remoteAddr")}</td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${eventRecordInstTotal}" />
      </div>
    </div>
  </body>
</html>
