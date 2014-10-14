<%@ page import="org.motrice.tdocbox.TdbItem" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'tdbItem.label', default: 'TdbItem')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-tdbItem" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
	<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="show-tdbItem" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list tdbItem">
	<g:if test="${tdbItemObj?.name}">
	  <li class="fieldcontain">
	    <span id="name-label" class="property-label"><g:message code="tdbItem.name.label" default="Name" /></span>
	    <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${tdbItemObj}" field="name"/></span>
	  </li>
	</g:if>
	<g:if test="${tdbItemObj?.binaryFlag}">
	  <li class="fieldcontain">
	    <span id="binaryFlag-label" class="property-label"><g:message code="tdbItem.binaryFlag.label" default="Binary Flag" /></span>
	    <span class="property-value" aria-labelledby="binaryFlag-label"><g:formatBoolean boolean="${tdbItemObj?.binaryFlag}" /></span>
	  </li>
	</g:if>
	<g:if test="${tdbItemObj?.text}">
	  <li class="fieldcontain">
	    <span id="text-label" class="property-label"><g:message code="tdbItem.text.label" default="Text" /></span>
	    <span class="property-value" aria-labelledby="text-label"><g:fieldValue bean="${tdbItemObj}" field="text"/></span>
	  </li>
	</g:if>
	<g:if test="${tdbItemObj?.bytes}">
	  <li class="fieldcontain">
	    <span id="bytes-label" class="property-label"><g:message code="tdbItem.bytes.label" default="Bytes" /></span>
	    <span class="property-value" aria-labelledby="bytes-label">${tdbItemObj?.bytes?.size()} bytes</span>
	  </li>
	</g:if>
	<g:if test="${tdbItemObj?.case}">
	  <li class="fieldcontain">
	    <span id="case-label" class="property-label"><g:message code="tdbItem.case.label" default="Case" /></span>
	    <span class="property-value" aria-labelledby="case-label"><g:link controller="tdbCase" action="show" id="${tdbItemObj?.case?.id}">${tdbItemObj?.case?.encodeAsHTML()}</g:link></span>
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${tdbItemObj?.id}" />
	  <g:link class="edit" action="edit" id="${tdbItemObj?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
	  <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
