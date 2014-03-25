<%@ page import="org.motrice.coordinatrice.CrdI18nGuideUrl" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'crdI18nGuideUrl.label', default: 'CrdI18nGuideUrl')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-crdI18nGuideUrl" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="list-crdI18nGuideUrl" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="procdefKey" title="${message(code: 'crdI18nGuideUrl.procdefKey.label', default: 'Procdef Key')}" />
	    <g:sortableColumn property="procdefVer" title="${message(code: 'crdI18nGuideUrl.procdefVer.label', default: 'Procdef Ver')}" />
	    <g:sortableColumn property="pattern" title="${message(code: 'crdI18nGuideUrl.pattern.label', default: 'Pattern')}" />
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${crdI18nGuideUrlInstList}" status="i" var="crdI18nGuideUrlInst">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:link action="show" id="${crdI18nGuideUrlInst.id}">${fieldValue(bean: crdI18nGuideUrlInst, field: "procdefKey")}</g:link></td>
	      <td>${fieldValue(bean: crdI18nGuideUrlInst, field: "procdefVer")}</td>
	      <td>${fieldValue(bean: crdI18nGuideUrlInst, field: "pattern")}</td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${crdI18nGuideUrlInstTotal}" />
      </div>
    </div>
  </body>
</html>
