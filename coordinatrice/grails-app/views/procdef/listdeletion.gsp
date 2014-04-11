<%@ page import="org.motrice.coordinatrice.Procdef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'procdef.label', default: 'Procdef')}" />
    <g:set var="formEntityName" value="${message(code: 'pxdFormdef.label', default: 'PxdFormdef')}" />
    <g:set var="categEntityName" value="${message(code: 'crdProcCategory.label', default: 'CrdProcCategory')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-procdef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <g:form>
      <div id="list-procdef" class="content scaffold-list" role="main">
	<h1><g:message code="default.list.label" args="[entityName]" /></h1>
	<g:if test="${flash.message}">
	  <div class="message" role="status">${flash.message}</div>
	</g:if>
	<g:render template="procdeftable"/>
	<div class="pagination buttons">&nbsp;<g:hiddenField name="id" value="${procdefKey}" />
	  <g:paginate id="${procdefKey}" total="${procdefInstTotal}" />
	  <g:if test="${procdefInstList}">
	    <g:actionSubmit class="delete" action="deletionconfirm" value="${message(code: 'procdef.deletion.proceed.button.label', default: 'Delete')}"/>
	  </g:if>
	</div>
      </div>
    </g:form>
  </body>
</html>
