<%@ page import="org.motrice.postxdb.PxdFormdef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'pxdFormdef.label', default: 'PxdFormdef')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
    </meta>
  </head>
  <body>
    <a href="#show-pxdFormdef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="show-pxdFormdef" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list pxdFormdef">
	<g:if test="${pxdFormdefObj?.path}">
	  <li class="fieldcontain">
	    <span id="path-label" class="property-label"><g:message code="pxdFormdef.path.label" default="Path" /></span>
	    <span class="property-value" aria-labelledby="path-label"><g:fieldValue bean="${pxdFormdefObj}" field="path"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefObj?.currentDraft}">
	  <li class="fieldcontain">
	    <span id="currentDraft-label" class="property-label"><g:message code="pxdFormdef.currentDraft.label" default="Current Draft" /></span>
	    <span class="property-value" aria-labelledby="currentDraft-label"><g:fieldValue bean="${pxdFormdefObj}" field="currentDraft"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefObj?.uuid}">
	  <li class="fieldcontain">
	    <span id="uuid-label" class="property-label"><g:message code="pxdFormdef.uuid.label" default="Uuid" /></span>
	    <span class="property-value" aria-labelledby="uuid-label"><g:fieldValue bean="${pxdFormdefObj}" field="uuid"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefObj?.appName}">
	  <li class="fieldcontain">
	    <span id="appName-label" class="property-label"><g:message code="pxdFormdef.appName.label" default="App Name" /></span>
	    <span class="property-value" aria-labelledby="appName-label"><g:fieldValue bean="${pxdFormdefObj}" field="appName"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefObj?.formName}">
	  <li class="fieldcontain">
	    <span id="formName-label" class="property-label"><g:message code="pxdFormdef.formName.label" default="Form Name" /></span>
	    <span class="property-value" aria-labelledby="formName-label"><g:fieldValue bean="${pxdFormdefObj}" field="formName"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefObj?.dateCreated}">
	  <li class="fieldcontain">
	    <span id="dateCreated-label" class="property-label"><g:message code="pxdFormdef.createdUpdated.label" default="Date Created" /></span>
	    <span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${pxdFormdefObj?.dateCreated}"/>/
	    <g:formatDate date="${pxdFormdefObj?.lastUpdated}"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefObj?.forms}">
	  <li class="fieldcontain">
	    <span id="forms-label" class="property-label"><g:message code="pxdFormdef.forms.label" default="Forms" /></span>
	    <g:each in="${pxdFormdefObj.forms}" var="f">
	      <span class="property-value" aria-labelledby="forms-label"><g:link controller="pxdFormdefVer" action="show" id="${f.id}">${f.display()?.encodeAsHTML()}</g:link></span>
	    </g:each>
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${pxdFormdefObj?.id}" />
	  <g:link class="edit" action="edit" id="${pxdFormdefObj?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
	  <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
