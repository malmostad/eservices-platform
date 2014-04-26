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
