<%@ page import="org.motrice.orifice.OriFormdef" %>

<div class="fieldcontain ${hasErrors(bean: oriFormdefInst, field: 'app', 'error')} ">
	<label for="app">
		<g:message code="oriFormdef.app.label" default="App" />
		
	</label>
	<g:textField name="app" maxlength="120" value="${oriFormdefInst?.app}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriFormdefInst, field: 'form', 'error')} ">
	<label for="form">
		<g:message code="oriFormdef.form.label" default="Form" />
		
	</label>
	<g:textField name="form" maxlength="120" value="${oriFormdefInst?.form}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriFormdefInst, field: 'uuid', 'error')} ">
	<label for="uuid">
		<g:message code="oriFormdef.uuid.label" default="Uuid" />
		
	</label>
	<g:textField name="uuid" maxlength="200" value="${oriFormdefInst?.uuid}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriFormdefInst, field: 'currentDraft', 'error')} ">
	<label for="currentDraft">
		<g:message code="oriFormdef.currentDraft.label" default="Current Draft" />
		
	</label>
	<g:textArea name="currentDraft" cols="40" rows="5" maxlength="400" value="${oriFormdefInst?.currentDraft}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriFormdefInst, field: 'forms', 'error')} ">
	<label for="forms">
		<g:message code="oriFormdef.forms.label" default="Forms" />
		
	</label>
	<g:select name="forms" from="${org.motrice.orifice.OriFormdefVer.list()}" multiple="multiple" optionKey="id" size="5" value="${oriFormdefInst?.forms*.id}" class="many-to-many"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriFormdefInst, field: 'created', 'error')} required">
	<label for="created">
		<g:message code="oriFormdef.created.label" default="Created" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="created" precision="day"  value="${oriFormdefInst?.created}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: oriFormdefInst, field: 'ref', 'error')} required">
	<label for="ref">
		<g:message code="oriFormdef.ref.label" default="Ref" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="ref" type="number" value="${oriFormdefInst.ref}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriFormdefInst, field: 'updated', 'error')} required">
	<label for="updated">
		<g:message code="oriFormdef.updated.label" default="Updated" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="updated" precision="day"  value="${oriFormdefInst?.updated}"  />
</div>

