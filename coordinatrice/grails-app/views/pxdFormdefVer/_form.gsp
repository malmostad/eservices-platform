<%@ page import="org.motrice.coordinatrice.pxd.PxdFormdefVer" %>



<div class="fieldcontain ${hasErrors(bean: pxdFormdefVerInst, field: 'path', 'error')} ">
	<label for="path">
		<g:message code="pxdFormdefVer.path.label" default="Path" />
		
	</label>
	<g:textArea name="path" cols="40" rows="5" maxlength="400" value="${pxdFormdefVerInst?.path}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefVerInst, field: 'appName', 'error')} ">
	<label for="appName">
		<g:message code="pxdFormdefVer.appName.label" default="App Name" />
		
	</label>
	<g:textField name="appName" maxlength="120" value="${pxdFormdefVerInst?.appName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefVerInst, field: 'formName', 'error')} ">
	<label for="formName">
		<g:message code="pxdFormdefVer.formName.label" default="Form Name" />
		
	</label>
	<g:textField name="formName" maxlength="120" value="${pxdFormdefVerInst?.formName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefVerInst, field: 'fvno', 'error')} required">
	<label for="fvno">
		<g:message code="pxdFormdefVer.fvno.label" default="Fvno" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="fvno" from="${1..9999}" class="range" required="" value="${fieldValue(bean: pxdFormdefVerInst, field: 'fvno')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefVerInst, field: 'draft', 'error')} required">
	<label for="draft">
		<g:message code="pxdFormdefVer.draft.label" default="Draft" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="draft" from="${1..10000}" class="range" required="" value="${fieldValue(bean: pxdFormdefVerInst, field: 'draft')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefVerInst, field: 'title', 'error')} ">
	<label for="title">
		<g:message code="pxdFormdefVer.title.label" default="Title" />
		
	</label>
	<g:textField name="title" maxlength="120" value="${pxdFormdefVerInst?.title}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefVerInst, field: 'description', 'error')} ">
	<label for="description">
		<g:message code="pxdFormdefVer.description.label" default="Description" />
		
	</label>
	<g:textArea name="description" cols="40" rows="5" maxlength="800" value="${pxdFormdefVerInst?.description}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefVerInst, field: 'language', 'error')} ">
	<label for="language">
		<g:message code="pxdFormdefVer.language.label" default="Language" />
		
	</label>
	<g:textField name="language" maxlength="16" value="${pxdFormdefVerInst?.language}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefVerInst, field: 'formdef', 'error')} required">
	<label for="formdef">
		<g:message code="pxdFormdefVer.formdef.label" default="Formdef" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="formdef" name="formdef.id" from="${org.motrice.coordinatrice.pxd.PxdFormdef.list()}" optionKey="id" required="" value="${pxdFormdefVerInst?.formdef?.id}" class="many-to-one"/>
</div>

