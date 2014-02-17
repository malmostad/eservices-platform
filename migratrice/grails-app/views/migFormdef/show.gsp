<%@ page import="org.motrice.migratrice.MigFormdef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'migFormdef.label', default: 'MigFormdef')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-migFormdef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
      </ul>
    </div>
    <div id="show-migFormdef" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list migFormdef">
	<g:if test="${migFormdefInst?.app}">
	  <li class="fieldcontain">
	    <span id="app-label" class="property-label"><g:message code="migFormdef.app.label" default="App" /></span>
	    <span class="property-value" aria-labelledby="app-label"><g:fieldValue bean="${migFormdefInst}" field="app"/></span>
	  </li>
	</g:if>
	<g:if test="${migFormdefInst?.form}">
	  <li class="fieldcontain">
	    <span id="form-label" class="property-label"><g:message code="migFormdef.form.label" default="Form" /></span>
	    <span class="property-value" aria-labelledby="form-label"><g:fieldValue bean="${migFormdefInst}" field="form"/></span>
	  </li>
	</g:if>
	<g:if test="${migFormdefInst?.uuid}">
	  <li class="fieldcontain">
	    <span id="uuid-label" class="property-label"><g:message code="migFormdef.uuid.label" default="Uuid" /></span>
	    <span class="property-value" aria-labelledby="uuid-label"><g:fieldValue bean="${migFormdefInst}" field="uuid"/></span>
	  </li>
	</g:if>
	<g:if test="${migFormdefInst?.ref}">
	  <li class="fieldcontain">
	    <span id="ref-label" class="property-label"><g:message code="migFormdef.ref.label" default="Ref" /></span>
	    <span class="property-value" aria-labelledby="ref-label"><g:fieldValue bean="${migFormdefInst}" field="ref"/></span>
	  </li>
	</g:if>
	<g:if test="${migFormdefInst?.currentDraft}">
	  <li class="fieldcontain">
	    <span id="currentDraft-label" class="property-label"><g:message code="migFormdef.currentDraft.label" default="Current Draft" /></span>
	    <span class="property-value" aria-labelledby="currentDraft-label"><g:fieldValue bean="${migFormdefInst}" field="currentDraft"/></span>
	  </li>
	</g:if>
	<g:if test="${migFormdefInst?.created}">
	  <li class="fieldcontain">
	    <span id="created-label" class="property-label"><g:message code="migFormdef.createdupdated.label" default="Created/Updated" /></span>
	    <span class="property-value" aria-labelledby="created-label"><g:tstamp date="${migFormdefInst?.created}"/> /
	      <g:tstamp date="${migFormdefInst?.updated}"/></span>
	  </li>
	</g:if>
	<g:if test="${migFormdefInst?.versions}">
	  <li class="fieldcontain">
	    <span id="versions-label" class="property-label"><g:message code="migFormdef.versions.label" default="Versions" /></span>
	    <g:each in="${migFormdefInst.versions}" var="f">
	      <span class="property-value" aria-labelledby="versions-label"><g:link controller="migFormdefVer" action="show" id="${f.id}">${f?.display()?.encodeAsHTML()}</g:link></span>
	    </g:each>
	  </li>
	</g:if>
	<g:if test="${migFormdefInst?.items}">
	  <li class="fieldcontain">
	    <span id="items-label" class="property-label"><g:message code="migFormdef.items.label" default="Items" /></span>
	    <g:each in="${migFormdefInst.items}" var="f">
	      <span class="property-value" aria-labelledby="items-label"><g:link controller="migItem" action="show" id="${f.id}">${f?.display()?.encodeAsHTML()}</g:link></span>
	    </g:each>
	  </li>
	</g:if>
	<g:if test="${migFormdefInst?.pack}">
	  <li class="fieldcontain">
	    <span id="pack-label" class="property-label"><g:message code="migFormdef.pack.label" default="Pack" /></span>
	    <span class="property-value" aria-labelledby="pack-label"><g:link controller="migPackage" action="show" id="${migFormdefInst?.pack?.id}">${migFormdefInst?.pack?.display()?.encodeAsHTML()}</g:link></span>
	  </li>
	</g:if>
      </ol>
    </div>
  </body>
</html>
