<%@ page import="org.motrice.orifice.OriFormdefVer" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'oriFormdefVer.label', default: 'OriFormdefVer')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-oriFormdefVer" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
      </ul>
    </div>
    <div id="show-oriFormdefVer" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list oriFormdefVer">
	<g:if test="${oriFormdefVerInst?.app}">
	  <li class="fieldcontain">
	    <span id="app-label" class="property-label"><g:message code="oriFormdefVer.app.label" default="App" /></span>
	    <span class="property-value" aria-labelledby="app-label"><g:fieldValue bean="${oriFormdefVerInst}" field="app"/></span>
	  </li>
	</g:if>
	<g:if test="${oriFormdefVerInst?.form}">
	  <li class="fieldcontain">
	    <span id="form-label" class="property-label"><g:message code="oriFormdefVer.form.label" default="Form" /></span>
	    <span class="property-value" aria-labelledby="form-label"><g:fieldValue bean="${oriFormdefVerInst}" field="form"/></span>
	  </li>
	</g:if>
	<g:if test="${oriFormdefVerInst?.path}">
	  <li class="fieldcontain">
	    <span id="path-label" class="property-label"><g:message code="oriFormdefVer.path.label" default="Path" /></span>
	    <span class="property-value" aria-labelledby="path-label"><g:fieldValue bean="${oriFormdefVerInst}" field="path"/></span>
	  </li>
	</g:if>
	<g:if test="${oriFormdefVerInst?.ref}">
	  <li class="fieldcontain">
	    <span id="ref-label" class="property-label"><g:message code="oriFormdefVer.ref.label" default="Ref" /></span>
	    <span class="property-value" aria-labelledby="ref-label"><g:fieldValue bean="${oriFormdefVerInst}" field="ref"/></span>
	  </li>
	</g:if>
	<g:if test="${oriFormdefVerInst?.created}">
	  <li class="fieldcontain">
	    <span id="created-label" class="property-label"><g:message code="oriFormdefVer.created.label" default="Created" /></span>
	    <span class="property-value" aria-labelledby="created-label"><g:formatDate date="${oriFormdefVerInst?.created}" /></span>
	  </li>
	</g:if>
	<li class="fieldcontain">
	  <span id="published-label" class="property-label"><g:message code="oriFormdefVer.published.label" default="Published" /></span>
	  <span class="property-value" aria-labelledby="published-label"><g:formatPublished flag="${oriFormdefVerInst?.published}" /></span>
	</li>
	<g:if test="${oriFormdefVerInst?.title}">
	  <li class="fieldcontain">
	    <span id="title-label" class="property-label"><g:message code="oriFormdefVer.title.label" default="Title" /></span>
	    <span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${oriFormdefVerInst}" field="title"/></span>
	  </li>
	</g:if>
	<g:if test="${oriFormdefVerInst?.description}">
	  <li class="fieldcontain">
	    <span id="description-label" class="property-label"><g:message code="oriFormdefVer.description.label" default="Description" /></span>
	    <span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${oriFormdefVerInst}" field="description"/></span>
	  </li>
	</g:if>
	<g:if test="${oriFormdefVerInst?.language}">
	  <li class="fieldcontain">
	    <span id="language-label" class="property-label"><g:message code="oriFormdefVer.language.label" default="Language" /></span>
	    <span class="property-value" aria-labelledby="language-label"><g:fieldValue bean="${oriFormdefVerInst}" field="language"/></span>
	  </li>
	</g:if>
      </ol>
    </div>
  </body>
</html>
