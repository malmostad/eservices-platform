<%@ page import="org.motrice.migratrice.MigFormdefVer" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'migFormdefVer.label', default: 'MigFormdefVer')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-migFormdefVer" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
      </ul>
    </div>
    <div id="show-migFormdefVer" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list migFormdefVer">
	<g:if test="${migFormdefVerInst?.app}">
	  <li class="fieldcontain">
	    <span id="app-label" class="property-label"><g:message code="migFormdefVer.app.label" default="App" /></span>
	    <span class="property-value" aria-labelledby="app-label"><g:fieldValue bean="${migFormdefVerInst}" field="app"/></span>
	  </li>
	</g:if>
	<g:if test="${migFormdefVerInst?.form}">
	  <li class="fieldcontain">
	    <span id="form-label" class="property-label"><g:message code="migFormdefVer.form.label" default="Form" /></span>
	    <span class="property-value" aria-labelledby="form-label"><g:fieldValue bean="${migFormdefVerInst}" field="form"/></span>
	  </li>
	</g:if>
	<g:if test="${migFormdefVerInst?.path}">
	  <li class="fieldcontain">
	    <span id="path-label" class="property-label"><g:message code="migFormdefVer.path.label" default="Path" /></span>
	    <span class="property-value" aria-labelledby="path-label"><g:fieldValue bean="${migFormdefVerInst}" field="path"/></span>
	  </li>
	</g:if>
	<li class="fieldcontain">
	  <span id="published-label" class="property-label"><g:message code="migFormdefVer.published.label" default="Published" /></span>
	  <span class="property-value" aria-labelledby="published-label"><g:formatPublished flag="${migFormdefVerInst?.published}" /></span>
	</li>
	<g:if test="${migFormdefVerInst?.ref}">
	  <li class="fieldcontain">
	    <span id="ref-label" class="property-label"><g:message code="migFormdefVer.ref.label" default="Ref" /></span>
	    <span class="property-value" aria-labelledby="ref-label"><g:fieldValue bean="${migFormdefVerInst}" field="ref"/></span>
	  </li>
	</g:if>
	<g:if test="${migFormdefVerInst?.created}">
	  <li class="fieldcontain">
	    <span id="created-label" class="property-label"><g:message code="migFormdefVer.created.label" default="Created" /></span>
	    <span class="property-value" aria-labelledby="created-label"><g:formatDate date="${migFormdefVerInst?.created}" /></span>
	  </li>
	</g:if>
	<g:if test="${migFormdefVerInst?.title}">
	  <li class="fieldcontain">
	    <span id="title-label" class="property-label"><g:message code="migFormdefVer.title.label" default="Title" /></span>
	    <span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${migFormdefVerInst}" field="title"/></span>
	  </li>
	</g:if>
	<g:if test="${migFormdefVerInst?.description}">
	  <li class="fieldcontain">
	    <span id="description-label" class="property-label"><g:message code="migFormdefVer.description.label" default="Description" /></span>
	    <span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${migFormdefVerInst}" field="description"/></span>
	  </li>
	</g:if>
	<g:if test="${migFormdefVerInst?.language}">
	  <li class="fieldcontain">
	    <span id="language-label" class="property-label"><g:message code="migFormdefVer.language.label" default="Language" /></span>
	    <span class="property-value" aria-labelledby="language-label"><g:fieldValue bean="${migFormdefVerInst}" field="language"/></span>
	  </li>
	</g:if>
      </ol>
    </div>
  </body>
</html>
