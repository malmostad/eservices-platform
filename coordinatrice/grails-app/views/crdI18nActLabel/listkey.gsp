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
    <div id="list-crdI18nActLabel" class="content scaffold-list" role="main">
      <h1><g:message code="crdI18nActLabel.list.header" args="[procdefKey]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="procdefVer" title="${message(code: 'crdI18nActLabel.procdefVer.label', default: 'Procdef Ver')}" />
	    <th></th>
	    <g:sortableColumn property="actdefName" title="${message(code: 'crdI18nActLabel.actdefName.label', default: 'Actdef Name')}" />
	    <g:sortableColumn property="actdefId" title="${message(code: 'crdI18nActLabel.actdefId.label', default: 'Actdef Id')}" />
	    <th></th>
	    <g:sortableColumn property="locale" title="${message(code: 'crdI18nActLabel.locale.label', default: 'Locale')}" />
	    <g:sortableColumn property="label" title="${message(code: 'crdI18nActLabel.label.label', default: 'Label')}" />
	    <th></th>
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${actLabelInstList}" status="i" var="actLabelInst">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td>${fieldValue(bean: actLabelInst, field: "procdefVer")}</td>
	      <td><g:link action="editkey" id="${actLabelInst?.id}" params="[mode: 'activity']">
		  <g:img uri="/images/silk/page_edit.png" title="${message(code: 'crdI18nActLabel.edit.activity.label', default: 'Edit')}"/>
	      </g:link></td>
	      <td>${fieldValue(bean: actLabelInst, field: "actdefName")}</td>
	      <td>${fieldValue(bean: actLabelInst, field: "actdefId")}</td>
	      <td><g:link action="editkey" id="${actLabelInst?.id}" params="[mode: 'locale']">
		  <g:img uri="/images/silk/page_edit.png" title="${message(code: 'crdI18nActLabel.edit.locale.label', default: 'Edit')}"/>
	      </g:link></td>
	      <td>${fieldValue(bean: actLabelInst, field: "locale")}</td>
	      <td>${fieldValue(bean: actLabelInst, field: "label")}</td>
	      <td><g:link action="createversion" id="${actLabelInst.id}">
		  <g:img uri="/images/silk/page_copy.png" title="${message(code: 'crdI18nActLabel.add.version.label')}"/>
	      </g:link></td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${actLabelInstTotal}" />
	<g:link class="menu-left edit" action="createlocale" id="${procdefKey}">
	  <g:message code="crdI18nActLabel.generate.label" default="Generate"/>
	</g:link>
      </div>
    </div>
  </body>
</html>
