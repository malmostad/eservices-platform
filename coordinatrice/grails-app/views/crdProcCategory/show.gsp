<%@ page import="org.motrice.coordinatrice.CrdProcCategory" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'crdProcCategory.label', default: 'CrdProcCategory')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-crdProcCategory" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
	<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="show-crdProcCategory" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list crdProcCategory">
	<g:if test="${crdProcCategoryInst?.name}">
	  <li class="fieldcontain">
	    <span id="name-label" class="property-label"><g:message code="crdProcCategory.name.label" default="Name" /></span>
	    <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${crdProcCategoryInst}" field="name"/></span>
	  </li>
	</g:if>
	<g:if test="${crdProcCategoryInst?.description}">
	  <li class="fieldcontain">
	    <span id="description-label" class="property-label"><g:message code="crdProcCategory.description.label" default="Description" /></span>
	    <span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${crdProcCategoryInst}" field="description"/></span>
	  </li>
	</g:if>
	<g:if test="${crdProcCategoryInst?.dateCreated}">
	  <li class="fieldcontain">
	    <span id="dateCreated-label" class="property-label"><g:message code="crdProcCategory.dateCreated.label" default="Date Created" /></span>
	    <span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${crdProcCategoryInst?.dateCreated}" /></span>
	  </li>
	</g:if>
	<g:if test="${crdProcCategoryInst?.lastUpdated}">
	  <li class="fieldcontain">
	    <span id="lastUpdated-label" class="property-label"><g:message code="crdProcCategory.lastUpdated.label" default="Last Updated" /></span>
	    <span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${crdProcCategoryInst?.lastUpdated}" /></span>
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${crdProcCategoryInst?.id}" />
	  <g:link class="edit" action="edit" id="${crdProcCategoryInst?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
	  <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
