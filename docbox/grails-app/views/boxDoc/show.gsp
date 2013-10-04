<%@ page import="org.motrice.docbox.doc.BoxDoc" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'boxDoc.label', default: 'BoxDoc')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-boxDoc" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
	<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="show-boxDoc" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list boxDoc">
	<g:if test="${boxDocObj?.docNo}">
	  <li class="fieldcontain">
	    <span id="docNo-label" class="property-label"><g:message code="boxDoc.docNo.label" default="Doc No" /></span>
	    <span class="property-value" aria-labelledby="docNo-label"><g:fieldValue bean="${boxDocObj}" field="docNo"/></span>
	  </li>
	</g:if>
	<g:if test="${boxDocObj?.formDataUuid}">
	  <li class="fieldcontain">
	    <span id="formDataUuid-label" class="property-label"><g:message code="boxDoc.formDataUuid.label" default="Form Data Uuid" /></span>
	    <span class="property-value" aria-labelledby="formDataUuid-label"><g:fieldValue bean="${boxDocObj}" field="formDataUuid"/></span>
	  </li>
	</g:if>
	<g:if test="${boxDocObj?.dateCreated}">
	  <li class="fieldcontain">
	    <span id="dateCreated-label" class="property-label"><g:message code="boxDoc.dateCreated.label" default="Date Created" /></span>
	    <span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${boxDocObj?.dateCreated}" /></span>
	  </li>
	</g:if>
	<g:if test="${boxDocObj?.lastUpdated}">
	  <li class="fieldcontain">
	    <span id="lastUpdated-label" class="property-label"><g:message code="boxDoc.lastUpdated.label" default="Last Updated" /></span>
	    <span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${boxDocObj?.lastUpdated}" /></span>
	  </li>
	</g:if>
	<g:if test="${boxDocObj?.steps}">
	  <li class="fieldcontain">
	    <span id="steps-label" class="property-label"><g:message code="boxDoc.steps.label" default="Steps" /></span>
	    <g:each in="${boxDocObj.steps}" var="s">
	      <span class="property-value" aria-labelledby="steps-label"><g:link controller="boxDocStep" action="show" id="${s.id}">${s?.display()?.encodeAsHTML()}</g:link></span>
	    </g:each>
	    
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${boxDocObj?.id}" />
	  <g:link class="edit" action="edit" id="${boxDocObj?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
	  <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
