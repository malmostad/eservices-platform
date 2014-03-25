<%@ page import="org.motrice.coordinatrice.CrdI18nFormLabel" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'crdI18nFormLabel.label', default: 'CrdI18nFormLabel')}" />
      <g:set var="formEntityName" value="${message(code: 'pxdFormdef.label', default: 'PxdFormdef')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-crdI18nFormLabel" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="list" controller="PxdFormdef" action="list"><g:message code="default.list.label" args="[formEntityName]" /></g:link></li>
      </ul>
    </div>
    <div id="list-crdI18nFormLabel" class="content scaffold-list" role="main">
      <g:set var="formdefPath" value="${formdefInst?.path}"/>
      <h1><g:message code="crdI18nFormLabel.list.header" args="[formdefPath]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="formdefVer" title="${message(code: 'crdI18nFormLabel.formdefVer.label', default: 'Formdef Ver')}" />
	    <g:sortableColumn property="locale" title="${message(code: 'crdI18nFormLabel.locale.label', default: 'Locale')}" />
	    <th></th>
	    <g:sortableColumn property="label" title="${message(code: 'crdI18nFormLabel.label.label', default: 'Label')}" />
	    <th></th>
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${formLabelInstList}" status="i" var="formLabelInst">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td>${fieldValue(bean: formLabelInst, field: "formdefVer")}</td>
	      <td>${fieldValue(bean: formLabelInst, field: "locale")}</td>
	      <td><g:link action="edit" id="${formLabelInst.id}">
		  <g:img uri="/images/silk/page_edit.png" title="${message(code: 'crdI18nFormLabel.edit.label')}"/>
	      </g:link></td>
	      <td>${fieldValue(bean: formLabelInst, field: "label")}</td>
	      <td><g:link action="createversion" id="${formLabelInst.id}">
		  <g:img uri="/images/silk/page_copy.png" title="${message(code: 'crdI18nFormLabel.add.version.label')}"/>
	      </g:link></td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${formLabelInstTotal}" />
	<g:link class="menu-left edit" action="createlocale" id="${formdefInst?.id}">
	  <g:message code="crdI18nFormLabel.generate.label" default="Generate"/>
	</g:link>
      </div>
    </div>
  </body>
</html>
