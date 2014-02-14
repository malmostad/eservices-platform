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

