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
<%@ page import="org.motrice.coordinatrice.Procdef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'procdef.label', default: 'Procdef')}" />
    <g:set var="categEntityName" value="${message(code: 'crdProcCategory.label', default: 'CrdProcCategory')}" />
    <title><g:message code="procdef.list.label"/></title>
  </head>
  <body>
    <div id="list-procdef" class="content scaffold-list" role="main">
      <h1><g:message code="procdef.list.label"/></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="name" title="${message(code: 'procdef.name.label', default: 'Name')}" />
	    <g:sortableColumn property="versions" title="${message(code: 'procdef.versions.label', default: 'Versions')}" />
	    <th><g:message code="procdef.i18n.labels.title" default="Intl"/></th>
	    <th><g:message code="procdef.i18n.guides.title" default="Intl"/></th>
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${procdefList}" status="i" var="procdefInfo">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:link action="listname" id="${procdefInfo.key}">${procdefInfo.name}</g:link></td>
	      <td>${procdefInfo.versions}</td>
	      <td><g:link controller="crdI18nActLabel" action="listkey" id="${procdefInfo.key}">
		  <g:img uri="/images/silk/tag_orange.png" title="${message(code: 'procdef.i18n.act.labels.label')}"/>
	      </g:link></td>
	      <td><g:link controller="crdI18nGuideUrl" action="listkey" id="${procdefInfo.key}">
		  <g:message code="procdef.i18n.guides.label" default="See labels"/>
	      </g:link></td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${procdefTotal}" />
      </div>
    </div>
  </body>
</html>
