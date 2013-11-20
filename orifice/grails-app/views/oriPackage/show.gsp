<%@ page import="org.motrice.orifice.OriPackage" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'oriPackage.label', default: 'OriPackage')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-oriPackage" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
	<li><g:link class="createexp" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="show-oriPackage" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list oriPackage">
	<g:if test="${oriPackageInst?.siteName}">
	  <li class="fieldcontain">
	    <span id="siteName-label" class="property-label"><g:message code="oriPackage.siteName.label" default="Site Name" /></span>
	    <span class="property-value" aria-labelledby="siteName-label"><g:fieldValue bean="${oriPackageInst}" field="siteName"/></span>
	  </li>
	</g:if>
	<g:if test="${oriPackageInst?.packageName}">
	  <li class="fieldcontain">
	    <span id="packageName-label" class="property-label"><g:message code="oriPackage.packageName.label" default="Package Name" /></span>
	    <span class="property-value" aria-labelledby="packageName-label"><g:fieldValue bean="${oriPackageInst}" field="packageName"/></span>
	  </li>
	</g:if>
	<g:if test="${oriPackageInst?.originLocal}">
	  <li class="fieldcontain">
	    <span id="originLocal-label" class="property-label"><g:message code="oriPackage.originLocal.label" default="Origin Local" /></span>
	    <span class="property-value" aria-labelledby="originLocal-label"><g:formatOrigin flag="${oriPackageInst?.originLocal}"/></span>
	  </li>
	</g:if>
	<g:if test="${oriPackageInst?.siteTstamp}">
	  <li class="fieldcontain">
	    <span id="siteTstamp-label" class="property-label"><g:message code="oriPackage.siteTstamp.label" default="Site Tstamp" /></span>
	    <span class="property-value" aria-labelledby="siteTstamp-label"><g:tstamp date="${oriPackageInst?.siteTstamp}"/></span>
	  </li>
	</g:if>
	<g:if test="${oriPackageInst?.dateCreated}">
	  <li class="fieldcontain">
	    <span id="dateCreated-label" class="property-label"><g:message code="oriPackage.dateCreated.label" default="Date Created" /></span>
	    <span class="property-value" aria-labelledby="dateCreated-label"><g:tstamp date="${oriPackageInst?.dateCreated}"/></span>
	  </li>
	</g:if>
	<g:if test="${oriPackageInst?.packageFormat}">
	  <li class="fieldcontain">
	    <span id="packageFormat-label" class="property-label"><g:message code="oriPackage.packageFormat.label" default="Package Format" /></span>
	    <span class="property-value" aria-labelledby="packageFormat-label"><g:fieldValue bean="${oriPackageInst}" field="packageFormat"/></span>
	  </li>
	</g:if>
	<g:if test="${oriPackageInst?.formdefs}">
	  <li class="fieldcontain">
	    <span id="formdefs-label" class="property-label"><g:message code="oriPackage.formdefs.label" default="Formdefs" /></span>
	    <g:each in="${oriPackageInst.formdefs}" var="f">
	      <span class="property-value" aria-labelledby="formdefs-label"><g:link controller="oriFormdef" action="show" id="${f.id}">${f?.encodeAsHTML()}</g:link></span>
	    </g:each>
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${oriPackageInst?.id}" />
	  <g:link class="edit" action="edit" id="${oriPackageInst?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
	  <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
