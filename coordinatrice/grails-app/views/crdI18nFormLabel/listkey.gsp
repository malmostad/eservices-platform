<%-- == Motrice Copyright Notice ==

  Motrice Service Platform

  Copyright (C) 2011-2014 Motrice AB

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU Affero General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU Affero General Public License for more details.

  You should have received a copy of the GNU Affero General Public License
  along with this program. If not, see <http://www.gnu.org/licenses/>.

  e-mail: info _at_ motrice.se
  mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN
  phone: +46 8 641 64 14

--%>
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
