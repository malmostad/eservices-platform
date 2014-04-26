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
