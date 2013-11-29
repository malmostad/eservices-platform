<%@ page import="org.motrice.migratrice.MigPackage" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'migPackage.label', default: 'MigPackage')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-migPackage" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="newpackage" action="listexp"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="list-migPackage" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="siteName" title="${message(code: 'migPackage.siteName.label', default: 'Site Name')}" />
	    <g:sortableColumn property="packageName" title="${message(code: 'migPackage.packageName.label', default: 'Package Name')}" />
	    <g:sortableColumn property="originLocal" title="${message(code: 'migPackage.originLocal.label', default: 'Origin Local')}" />
	    <g:sortableColumn property="siteTstamp" title="${message(code: 'migPackage.siteTstamp.label', default: 'Site Tstamp')}" />
	    <g:sortableColumn property="packageFormat" title="${message(code: 'migPackage.packageFormat.label', default: 'Package Format')}" />
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${migPackageInstList}" status="i" var="migPackageInst">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td>${fieldValue(bean: migPackageInst, field: "siteName")}</td>
	      <td><g:link action="show" id="${migPackageInst.id}">${fieldValue(bean: migPackageInst, field: "packageName")}</g:link></td>
	      <td><g:formatOrigin flag="${migPackageInst.originLocal}" /></td>
	      <td><g:tstamp date="${migPackageInst.siteTstamp}"/></td>
	      <td>${fieldValue(bean: migPackageInst, field: "packageFormat")}</td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${migPackageInstTotal}" />
      </div>
    </div>
  </body>
</html>
