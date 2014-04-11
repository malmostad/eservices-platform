<%@ page import="org.motrice.coordinatrice.pxd.PxdFormdef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'pxdFormdef.label', default: 'PxdFormdef')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-pxdFormdef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="show-pxdFormdef" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list pxdFormdef">
	<g:if test="${pxdFormdefInst?.path}">
	  <li class="fieldcontain">
	    <span id="path-label" class="property-label"><g:message code="pxdFormdef.path.label" default="Path" /></span>
	    <span class="property-value" aria-labelledby="path-label"><g:fieldValue bean="${pxdFormdefInst}" field="path"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefInst?.appName}">
	  <li class="fieldcontain">
	    <span id="appName-label" class="property-label"><g:message code="pxdFormdef.appName.label" default="App Name" /></span>
	    <span class="property-value" aria-labelledby="appName-label"><g:fieldValue bean="${pxdFormdefInst}" field="appName"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefInst?.formName}">
	  <li class="fieldcontain">
	    <span id="formName-label" class="property-label"><g:message code="pxdFormdef.formName.label" default="Form Name" /></span>
	    <span class="property-value" aria-labelledby="formName-label"><g:fieldValue bean="${pxdFormdefInst}" field="formName"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdFormdefInst?.uuid}">
	  <li class="fieldcontain">
	    <span id="uuid-label" class="property-label"><g:message code="pxdFormdef.uuid.label" default="Uuid" /></span>
	    <span class="property-value" aria-labelledby="uuid-label"><g:fieldValue bean="${pxdFormdefInst}" field="uuid"/></span>
	  </li>
	</g:if>
	<g:if test="${!formdefVerList.isEmpty()}">
	  <li class="fieldcontain">
	    <span id="forms-label" class="property-label"><g:message code="pxdFormdef.forms.label" default="Forms" /></span>
	    <g:each in="${formdefVerList}" var="entry">
	      <g:set var="f" value="${entry.formdefVer}"/>
	      <g:set var="proc" value="${entry?.procdef}"/>
	      <span class="property-value" aria-labelledby="forms-label">
		<g:link controller="pxdFormdefVer" action="show" id="${f.id}">${f?.encodeAsHTML()}</g:link>
		<g:if test="${proc}">
		  <g:set var="imgtitle"><g:message code="pxdFormdefVer.startform.link"/></g:set>
		  <g:link controller="procdef" action="show" id="${proc.uuid}">
		    <g:img uri="/images/silk/asterisk_yellow.png" title="${imgtitle}"/>
		  </g:link>
		</g:if>
	      </span>
	    </g:each>
	  </li>
	</g:if>
	
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${pxdFormdefInst?.id}" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
