<%@ page import="org.motrice.orifice.OriFormdef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'oriFormdef.label', default: 'OriFormdef')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-oriFormdef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
      </ul>
    </div>
    <div id="show-oriFormdef" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list oriFormdef">
	<g:if test="${oriFormdefInst?.app}">
	  <li class="fieldcontain">
	    <span id="app-label" class="property-label"><g:message code="oriFormdef.app.label" default="App" /></span>
	    <span class="property-value" aria-labelledby="app-label"><g:fieldValue bean="${oriFormdefInst}" field="app"/></span>
	  </li>
	</g:if>
	<g:if test="${oriFormdefInst?.form}">
	  <li class="fieldcontain">
	    <span id="form-label" class="property-label"><g:message code="oriFormdef.form.label" default="Form" /></span>
	    <span class="property-value" aria-labelledby="form-label"><g:fieldValue bean="${oriFormdefInst}" field="form"/></span>
	  </li>
	</g:if>
	<g:if test="${oriFormdefInst?.uuid}">
	  <li class="fieldcontain">
	    <span id="uuid-label" class="property-label"><g:message code="oriFormdef.uuid.label" default="Uuid" /></span>
	    <span class="property-value" aria-labelledby="uuid-label"><g:fieldValue bean="${oriFormdefInst}" field="uuid"/></span>
	  </li>
	</g:if>
	<g:if test="${oriFormdefInst?.ref}">
	  <li class="fieldcontain">
	    <span id="ref-label" class="property-label"><g:message code="oriFormdef.ref.label" default="Ref" /></span>
	    <span class="property-value" aria-labelledby="ref-label"><g:fieldValue bean="${oriFormdefInst}" field="ref"/></span>
	  </li>
	</g:if>
	<g:if test="${oriFormdefInst?.currentDraft}">
	  <li class="fieldcontain">
	    <span id="currentDraft-label" class="property-label"><g:message code="oriFormdef.currentDraft.label" default="Current Draft" /></span>
	    <span class="property-value" aria-labelledby="currentDraft-label"><g:fieldValue bean="${oriFormdefInst}" field="currentDraft"/></span>
	  </li>
	</g:if>
	<g:if test="${oriFormdefInst?.created}">
	  <li class="fieldcontain">
	    <span id="created-label" class="property-label"><g:message code="oriFormdef.createdupdated.label" default="Created/Updated" /></span>
	    <span class="property-value" aria-labelledby="created-label"><g:tstamp date="${oriFormdefInst?.created}"/> /
	      <g:tstamp date="${oriFormdefInst?.updated}"/></span>
	  </li>
	</g:if>
	<g:if test="${oriFormdefInst?.versions}">
	  <li class="fieldcontain">
	    <span id="versions-label" class="property-label"><g:message code="oriFormdef.versions.label" default="Versions" /></span>
	    <g:each in="${oriFormdefInst.versions}" var="f">
	      <span class="property-value" aria-labelledby="versions-label"><g:link controller="oriFormdefVer" action="show" id="${f.id}">${f?.display()?.encodeAsHTML()}</g:link></span>
	    </g:each>
	  </li>
	</g:if>
	<g:if test="${oriFormdefInst?.items}">
	  <li class="fieldcontain">
	    <span id="items-label" class="property-label"><g:message code="oriFormdef.items.label" default="Items" /></span>
	    <g:each in="${oriFormdefInst.items}" var="f">
	      <span class="property-value" aria-labelledby="items-label"><g:link controller="oriItem" action="show" id="${f.id}">${f?.display()?.encodeAsHTML()}</g:link></span>
	    </g:each>
	  </li>
	</g:if>
      </ol>
    </div>
  </body>
</html>
