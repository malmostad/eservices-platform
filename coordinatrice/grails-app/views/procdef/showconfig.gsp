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
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'procdef.label', default: 'Procdef')}" />
    <g:set var="categEntityName" value="${message(code: 'crdProcCategory.label', default: 'CrdProcCategory')}" />
    <title><g:message code="config.list.title"/></title>
  </head>
  <body>
    <div id="list-procdef" class="content scaffold-list" role="main">
      <h1><g:message code="config.list.title"/></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <th><g:message code="config.name.label" default="Name"/></th>
	    <th><g:message code="config.value.label" default="Value"/></th>
	    <th><g:message code="config.liveness.label" default="Live"/></th>
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${config}" status="i" var="entry">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:message code="${entry?.name}" default="${entry?.name}"/></td>
	      <td>${entry?.value?.encodeAsHTML()}</td>
	      <td><g:img uri="${entry?.img}" title="${entry?.title}"/></td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
    </div>
  </body>
</html>
