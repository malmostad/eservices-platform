<%@ page import="org.motrice.coordinatrice.Procdef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'procdef.label', default: 'Procdef')}" />
      <g:set var="categEntityName" value="${message(code: 'crdProcCategory.label', default: 'CrdProcCategory')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-procdef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="list" controller="MtfStartFormDefinition" action="list"><g:message code="startform.list.label"/></g:link></li>
	<li><g:link class="list" controller="PxdFormdef" action="list"><g:message code="pxdFormdef.list.label"/></g:link></li>
	<li><g:link class="list" controller="crdProcCategory" action="list"><g:message code="default.list.label" args="[categEntityName]" /></g:link></li>
      </ul>
    </div>
    <div id="list-procdef" class="content scaffold-list" role="main">
      <g:if test="${deploymentId}">
	<g:set var="deplId" value="${deploymentId}"/>
	<h1><g:message code="procdef.deployment.list.label" args="[deplId]" /></h1>
      </g:if>
      <g:else>
	<h1><g:message code="procdef.versions.list.label" args="[procdefKey]" /></h1>
      </g:else>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <g:render template="procdeftable"/>
      <div class="pagination">&nbsp;
	<g:paginate id="${procdefKey}" total="${procdefInstTotal}" />
	<g:if test="${deleteEnabled}">
	  <g:link class="menu-left delete" action="deletionconfirm" id="${deleteEnabled}">
	    <g:message code="procdef.deletion.proceed.button.label" default="Deletion"/>
	  </g:link>
	</g:if>
	<g:else>
	<g:link class="menu-left delete" action="listdeletion" id="${procdefKey}" params="[max:params.max,offset:params.offset]">
	  <g:message code="procdef.deletion.label" default="Deletion"/>
	</g:link>
	</g:else>
      </div>
    </div>
  </body>
</html>
