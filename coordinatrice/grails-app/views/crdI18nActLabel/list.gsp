<%@ page import="org.motrice.coordinatrice.CrdI18nActLabel" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'crdI18nActLabel.label', default: 'CrdI18nActLabel')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-crdI18nActLabel" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="list-crdI18nActLabel" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="procdefKey" title="${message(code: 'crdI18nActLabel.procdefKey.label', default: 'Procdef Key')}" />
	    <g:sortableColumn property="procdefVer" title="${message(code: 'crdI18nActLabel.procdefVer.label', default: 'Procdef Ver')}" />
	    <g:sortableColumn property="actdefName" title="${message(code: 'crdI18nActLabel.actdefName.label', default: 'Actdef Name')}" />
	    <g:sortableColumn property="actdefId" title="${message(code: 'crdI18nActLabel.actdefId.label', default: 'Actdef Id')}" />
	    <g:sortableColumn property="locale" title="${message(code: 'crdI18nActLabel.locale.label', default: 'Locale')}" />
	    <g:sortableColumn property="label" title="${message(code: 'crdI18nActLabel.label.label', default: 'Label')}" />
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${actLabelInstList}" status="i" var="actLabelInst">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:link action="show" id="${actLabelInst.id}">${fieldValue(bean: actLabelInst, field: "procdefKey")}</g:link></td>
	      <td>${fieldValue(bean: actLabelInst, field: "procdefVer")}</td>
	      <td>${fieldValue(bean: actLabelInst, field: "actdefName")}</td>
	      <td>${fieldValue(bean: actLabelInst, field: "actdefId")}</td>
	      <td>${fieldValue(bean: actLabelInst, field: "locale")}</td>
	      <td>${fieldValue(bean: actLabelInst, field: "label")}</td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${actLabelInstTotal}" />
      </div>
    </div>
  </body>
</html>
