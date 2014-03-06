<%@ page import="org.motrice.coordinatrice.CrdI18nGuideUrl" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'crdI18nGuideUrl.label', default: 'CrdI18nGuideUrl')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-crdI18nGuideUrl" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
	<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="show-crdI18nGuideUrl" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list crdI18nGuideUrl">
	<g:if test="${crdI18nGuideUrlInst?.procdefKey}">
	  <li class="fieldcontain">
	    <span id="procdefKey-label" class="property-label"><g:message code="crdI18nGuideUrl.procdefKey.label" default="Procdef Key" /></span>
	    <span class="property-value" aria-labelledby="procdefKey-label"><g:fieldValue bean="${crdI18nGuideUrlInst}" field="procdefKey"/></span>
	  </li>
	</g:if>
	<g:if test="${crdI18nGuideUrlInst?.procdefVer}">
	  <li class="fieldcontain">
	    <span id="procdefVer-label" class="property-label"><g:message code="crdI18nGuideUrl.procdefVer.label" default="Procdef Ver" /></span>
	    <span class="property-value" aria-labelledby="procdefVer-label"><g:fieldValue bean="${crdI18nGuideUrlInst}" field="procdefVer"/></span>
	  </li>
	</g:if>
	<g:if test="${crdI18nGuideUrlInst?.pattern}">
	  <li class="fieldcontain">
	    <span id="pattern-label" class="property-label"><g:message code="crdI18nGuideUrl.pattern.label" default="Pattern" /></span>
	    <span class="property-value" aria-labelledby="pattern-label"><g:fieldValue bean="${crdI18nGuideUrlInst}" field="pattern"/></span>
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${crdI18nGuideUrlInst?.id}" />
	  <g:link class="edit" action="edit" id="${crdI18nGuideUrlInst?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
	  <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
