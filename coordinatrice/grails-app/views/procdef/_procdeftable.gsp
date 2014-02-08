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
	<td><g:link action="show" id="${procdefInst.uuid}">${fieldValue(bean: procdefInst, field: "name")}</g:link></td>
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
