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
	    <th></th>
	    <g:sortableColumn property="pattern" title="${message(code: 'crdI18nGuideUrl.pattern.label', default: 'Pattern')}" />
	    <th></th>
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${guideUrlInstList}" status="i" var="guideUrlInst">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:link controller="procdef" action="listname" id="${guideUrlInst?.procdefKey}">${fieldValue(bean: guideUrlInst, field: "procdefKey")}</g:link></td>
	      <td>${fieldValue(bean: guideUrlInst, field: "procdefVer")}</td>
	      <td><g:link action="edit" id="${guideUrlInst?.id}">
		  <g:img uri="/images/silk/page_edit.png" title="${message(code: 'crdI18nGuideUrl.edit.label', default: 'Edit')}"/>
	      </g:link></td>
	      <td><g:link action="show" id="${guideUrlInst?.id}">${fieldValue(bean: guideUrlInst, field: "pattern")}</g:link></td>
	      <td><g:link action="createduplicate" id="${guideUrlInst?.id}">
		  <g:img uri="/images/silk/page_copy.png" title="${message(code: 'crdI18nGuideUrl.duplicate.label', default: 'Duplicate')}"/>
	      </g:link></td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${guideUrlInstTotal}" />
	<g:link class="menu-left edit" action="create" id="${procdefKey}">
	  <g:message code="crdI18nGuideUrl.create.label" default="Add"/>
	</g:link>
      </div>
    </div>
  </body>
</html>
