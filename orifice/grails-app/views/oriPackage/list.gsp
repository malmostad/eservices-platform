<%@ page import="org.motrice.orifice.OriPackage" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'oriPackage.label', default: 'OriPackage')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-oriPackage" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="create" action="listexp"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="list-oriPackage" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="siteName" title="${message(code: 'oriPackage.siteName.label', default: 'Site Name')}" />
	    <g:sortableColumn property="packageName" title="${message(code: 'oriPackage.packageName.label', default: 'Package Name')}" />
	    <g:sortableColumn property="originLocal" title="${message(code: 'oriPackage.originLocal.label', default: 'Origin Local')}" />
	    <g:sortableColumn property="siteTstamp" title="${message(code: 'oriPackage.siteTstamp.label', default: 'Site Tstamp')}" />
	    <g:sortableColumn property="packageFormat" title="${message(code: 'oriPackage.packageFormat.label', default: 'Package Format')}" />
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${oriPackageInstList}" status="i" var="oriPackageInst">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td>${fieldValue(bean: oriPackageInst, field: "siteName")}</td>
	      <td><g:link action="show" id="${oriPackageInst.id}">${fieldValue(bean: oriPackageInst, field: "packageName")}</g:link></td>
	      <td><g:formatOrigin flag="${oriPackageInst.originLocal}" /></td>
	      <td><g:tstamp date="${oriPackageInst.siteTstamp}"/></td>
	      <td>${fieldValue(bean: oriPackageInst, field: "packageFormat")}</td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${oriPackageInstTotal}" />
      </div>
    </div>
  </body>
</html>
