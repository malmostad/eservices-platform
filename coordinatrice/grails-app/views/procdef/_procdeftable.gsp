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
<table>
  <thead>
    <tr>
      <g:sortableColumn property="name" title="${message(code: 'procdef.name.label', default: 'Name')}" />
      <g:sortableColumn property="vno" title="${message(code: 'procdef.vno.label', default: 'Version')}" />
      <g:sortableColumn property="state" title="${message(code: 'procdef.state.label', default: 'State')}" />
      <g:if test="${delprefix}">
	<g:sortableColumn property="category" title="${message(code: 'default.button.delete.label', default: 'Del')}" />
      </g:if>
      <g:else>
	<g:sortableColumn property="category" title="${message(code: 'procdef.category.label', default: 'Categ')}" />
      </g:else>
      <g:sortableColumn property="deployedTime" title="${message(code: 'procdef.deployedTime.label', default: 'Deployed')}" />
      <th></th>
      <th></th>
    </tr>
  </thead>
  <tbody>
    <g:each in="${procdefInstList}" status="i" var="procdefInst">
      <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	<td><g:link action="show" id="${procdefInst.uuid}">${fieldValue(bean: procdefInst, field: "nameOrKey")}</g:link></td>
	<td>${fieldValue(bean: procdefInst, field: "vno")}</td>
	<td><g:pdefstate state="${procdefInst?.state}"/></td>
	<g:if test="${delprefix}">
	  <td><g:checkBox name="${delprefix}${procdefInst?.uuid}"/></td>
	</g:if>
	<g:else>
	  <td>${fieldValue(bean: procdefInst, field: "category")}</td>
	</g:else>
	<td><g:link action="listdepl" id="${procdefInst?.deployment?.id}">
	    <g:fieldValue bean="${procdefInst}" field="deployedTimeStr"/>
	</g:link></td>
	<td><g:link action="diagramDownload" id="${procdefInst.uuid}"><g:img uri="/images/silk/camera.png" title="${message(code: 'procdef.diagram.label', default: 'Diagram')}"/></g:link></td>
	<td><g:link action="xmlDownload" id="${procdefInst.uuid}"><g:img uri="/images/silk/map_edit.png" title="${message(code: 'procdef.xml.label', default: 'BPMN')}"/></g:link></td>
      </tr>
    </g:each>
  </tbody>
</table>
