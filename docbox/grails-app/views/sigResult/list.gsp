<%@ page import="org.motrice.signatrice.SigResult" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'sigResult.label', default: 'SigResult')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-sigResult" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="list-sigResult" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="transactionId" title="${message(code: 'sigResult.transactionId.label', default: 'Transaction Id')}" />
	    <g:sortableColumn property="dateCreated" title="${message(code: 'sigResult.dateCreated.label', default: 'Created')}" />
	    <th><g:message code="sigResult.progressStatus.label" default="Progress Status" /></th>
	    <th><g:message code="sigResult.faultStatus.label" default="Fault Status" /></th>
	    <g:sortableColumn property="orderRef" title="${message(code: 'sigResult.orderRef.label', default: 'Order Ref')}" />
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${sigResultInstList}" status="i" var="sigResultInst">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:link action="show" id="${sigResultInst.id}">${fieldValue(bean: sigResultInst, field: "transactionId")}</g:link></td>
	      <td><g:tstamp date="${sigResultInst?.dateCreated}"/></td>
	      <td>${fieldValue(bean: sigResultInst, field: "progressStatus")}</td>
	      <td>${fieldValue(bean: sigResultInst, field: "faultStatus")}</td>
	      <td>${fieldValue(bean: sigResultInst, field: "orderRef")}</td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${sigResultInstTotal}" />
      </div>
    </div>
  </body>
</html>
