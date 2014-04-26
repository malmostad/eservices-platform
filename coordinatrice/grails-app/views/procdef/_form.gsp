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
<div class="fieldcontain ${hasErrors(bean: procdefInst, field: 'uuid', 'error')} ">
  <label>
    <g:message code="procdef.label" default="Process" />
  </label>
  <g:link action="show" id="${procdefInst?.uuid}">${procdefInst?.display.encodeAsHTML()}</g:link>
</div>
<div class="fieldcontain">
  <label><g:message code="startform.selection.label"/></label>
  <div class="property-value">
    <g:select id="formsel" name="form.id" from="${formList}" optionKey="id" value="${selectedFormId}"
	      noSelection="['-1': message(code: 'startform.selection.select.form.label')]"/>
  </div>
</div>
<div class="fieldcontain">
  <g:if test="${startForms}">
    <g:set var="procdefId" value="${procdefInst?.uuid}"/>
    <g:each in="${startForms}" var="startFormView">
      <span class="property-value">
	<g:set var="pfv" value="${startFormView.formdefId}"/>
	<g:if test="${pfv}">
	  <g:link controller="pxdFormdefVer" action="show" id="${pfv}">${startFormView?.formConnectionKey?.encodeAsHTML()}</g:link>
	</g:if>
	<g:else>
	  <g:set var="linktitle"><g:message code="startform.selection.invalid.link"/></g:set>
	  <g:img uri="/images/silk/exclamation.png" title="${linktitle}"/> ${startFormView?.formConnectionKey?.encodeAsHTML()}
	</g:else>
	<g:link class="delete" controller="mtfStartFormDefinition" action="delete" id="${startFormView?.id}" params="[procdefId: procdefId]">
	  <g:img uri="/images/silk/delete.png" title="${message(code: 'default.button.delete.label', default: 'Delete')}"/>
	</g:link>
      </span>
    </g:each>
  </g:if>
  
</div>
