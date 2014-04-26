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
<%@ page import="org.motrice.coordinatrice.pxd.PxdFormdef" %>



<div class="fieldcontain ${hasErrors(bean: pxdFormdefInst, field: 'path', 'error')} ">
	<label for="path">
		<g:message code="pxdFormdef.path.label" default="Path" />
		
	</label>
	<g:textArea name="path" cols="40" rows="5" maxlength="256" value="${pxdFormdefInst?.path}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefInst, field: 'uuid', 'error')} ">
	<label for="uuid">
		<g:message code="pxdFormdef.uuid.label" default="Uuid" />
		
	</label>
	<g:textField name="uuid" maxlength="200" value="${pxdFormdefInst?.uuid}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefInst, field: 'appName', 'error')} ">
	<label for="appName">
		<g:message code="pxdFormdef.appName.label" default="App Name" />
		
	</label>
	<g:textField name="appName" maxlength="120" value="${pxdFormdefInst?.appName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefInst, field: 'formName', 'error')} ">
	<label for="formName">
		<g:message code="pxdFormdef.formName.label" default="Form Name" />
		
	</label>
	<g:textField name="formName" maxlength="120" value="${pxdFormdefInst?.formName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefInst, field: 'forms', 'error')} ">
	<label for="forms">
		<g:message code="pxdFormdef.forms.label" default="Forms" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${pxdFormdefInst?.forms?}" var="f">
    <li><g:link controller="pxdFormdefVer" action="show" id="${f.id}">${f?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="pxdFormdefVer" action="create" params="['pxdFormdef.id': pxdFormdefInst?.id]">${message(code: 'default.add.label', args: [message(code: 'pxdFormdefVer.label', default: 'PxdFormdefVer')])}</g:link>
</li>
</ul>

</div>

