<%@ page import="org.motrice.postxdb.PxdItem" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'pxdItem.label', default: 'PxdItem')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
    </meta>
  </head>
  <body>
    <a href="#show-pxdItem" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="show-pxdItem" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list pxdItem">
	<g:if test="${pxdItemObj?.path}">
	  <li class="fieldcontain">
	    <span id="path-label" class="property-label"><g:message code="pxdItem.path.label" default="Path" /></span>
	    <span class="property-value" aria-labelledby="path-label"><g:fieldValue bean="${pxdItemObj}" field="path"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdItemObj?.instance != null}">
	  <li class="fieldcontain">
	    <span id="instance-label" class="property-label"><g:message code="pxdItem.instance.label" default="Instance" /></span>
	    <span class="property-value" aria-labelledby="instance-label"><g:instflag flag="${pxdItemObj?.instance}"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdItemObj?.format}">
	  <li class="fieldcontain">
	    <span id="format-label" class="property-label"><g:message code="pxdItem.format.label" default="Format" /></span>
	    <span class="property-value" aria-labelledby="format-label"><g:fieldValue bean="${pxdItemObj}" field="format"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdItemObj?.size}">
	  <li class="fieldcontain">
	    <span id="size-label" class="property-label"><g:message code="pxdItem.size.label" default="Size" /></span>
	    <span class="property-value" aria-labelledby="size-label"><g:fieldValue bean="${pxdItemObj}" field="size"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdItemObj?.uuid}">
	  <li class="fieldcontain">
	    <span id="uuid-label" class="property-label"><g:message code="pxdItem.uuid.label" default="Uuid" /></span>
	    <span class="property-value" aria-labelledby="uuid-label"><g:fieldValue bean="${pxdItemObj}" field="uuid"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdItemObj?.formDef}">
	  <li class="fieldcontain">
	    <span id="formDef-label" class="property-label"><g:message code="pxdItem.formDef.label" default="Form Def" /></span>
	    <span class="property-value" aria-labelledby="formDef-label"><g:fieldValue bean="${pxdItemObj}" field="formDef"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdItemObj?.dateCreated}">
	  <li class="fieldcontain">
	    <span id="dateCreated-label" class="property-label"><g:message code="pxdItem.createdUpdated.label" default="Date Created" /></span>
	    <span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${pxdItemObj?.dateCreated}"/>/
	      <g:formatDate date="${pxdItemObj?.lastUpdated}"/></span>
	  </li>
	</g:if>
	<g:if test="${pxdItemObj?.text}">
	  <li class="fieldcontain">
	    <span id="text-label" class="property-label"><g:message code="pxdItem.text.label" default="Text" /></span>
	    <span class="property-value" aria-labelledby="text-label"><g:fieldValue bean="${pxdItemObj}" field="text"/></span>
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${pxdItemObj?.id}" />
	  <g:link class="edit" action="edit" id="${pxdItemObj?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
	  <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
