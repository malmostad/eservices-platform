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
<%@ page import="org.motrice.migratrice.MigFormdef" %>

<div class="fieldcontain ${hasErrors(bean: migFormdefInst, field: 'app', 'error')} ">
	<label for="app">
		<g:message code="migFormdef.app.label" default="App" />
		
	</label>
	<g:textField name="app" maxlength="120" value="${migFormdefInst?.app}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: migFormdefInst, field: 'form', 'error')} ">
	<label for="form">
		<g:message code="migFormdef.form.label" default="Form" />
		
	</label>
	<g:textField name="form" maxlength="120" value="${migFormdefInst?.form}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: migFormdefInst, field: 'uuid', 'error')} ">
	<label for="uuid">
		<g:message code="migFormdef.uuid.label" default="Uuid" />
		
	</label>
	<g:textField name="uuid" maxlength="200" value="${migFormdefInst?.uuid}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: migFormdefInst, field: 'currentDraft', 'error')} ">
	<label for="currentDraft">
		<g:message code="migFormdef.currentDraft.label" default="Current Draft" />
		
	</label>
	<g:textArea name="currentDraft" cols="40" rows="5" maxlength="400" value="${migFormdefInst?.currentDraft}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: migFormdefInst, field: 'forms', 'error')} ">
	<label for="forms">
		<g:message code="migFormdef.forms.label" default="Forms" />
		
	</label>
	<g:select name="forms" from="${org.motrice.migratrice.MigFormdefVer.list()}" multiple="multiple" optionKey="id" size="5" value="${migFormdefInst?.forms*.id}" class="many-to-many"/>
</div>

<div class="fieldcontain ${hasErrors(bean: migFormdefInst, field: 'created', 'error')} required">
	<label for="created">
		<g:message code="migFormdef.created.label" default="Created" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="created" precision="day"  value="${migFormdefInst?.created}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: migFormdefInst, field: 'ref', 'error')} required">
	<label for="ref">
		<g:message code="migFormdef.ref.label" default="Ref" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="ref" type="number" value="${migFormdefInst.ref}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: migFormdefInst, field: 'updated', 'error')} required">
	<label for="updated">
		<g:message code="migFormdef.updated.label" default="Updated" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="updated" precision="day"  value="${migFormdefInst?.updated}"  />
</div>

