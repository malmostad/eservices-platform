<%@ page import="org.motrice.migratrice.MigFormdefVer" %>



<div class="fieldcontain ${hasErrors(bean: migFormdefVerInst, field: 'app', 'error')} ">
	<label for="app">
		<g:message code="migFormdefVer.app.label" default="App" />
		
	</label>
	<g:textField name="app" maxlength="120" value="${migFormdefVerInst?.app}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: migFormdefVerInst, field: 'form', 'error')} ">
	<label for="form">
		<g:message code="migFormdefVer.form.label" default="Form" />
		
	</label>
	<g:textField name="form" maxlength="120" value="${migFormdefVerInst?.form}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: migFormdefVerInst, field: 'path', 'error')} ">
	<label for="path">
		<g:message code="migFormdefVer.path.label" default="Path" />
		
	</label>
	<g:textArea name="path" cols="40" rows="5" maxlength="400" value="${migFormdefVerInst?.path}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: migFormdefVerInst, field: 'draft', 'error')} ">
	<label for="draft">
		<g:message code="migFormdefVer.draft.label" default="Draft" />
		
	</label>
	<g:field name="draft" type="number" value="${migFormdefVerInst.draft}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: migFormdefVerInst, field: 'published', 'error')} ">
	<label for="published">
		<g:message code="migFormdefVer.published.label" default="Published" />
		
	</label>
	<g:checkBox name="published" value="${migFormdefVerInst?.published}" />
</div>

<div class="fieldcontain ${hasErrors(bean: migFormdefVerInst, field: 'title', 'error')} ">
	<label for="title">
		<g:message code="migFormdefVer.title.label" default="Title" />
		
	</label>
	<g:textField name="title" maxlength="120" value="${migFormdefVerInst?.title}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: migFormdefVerInst, field: 'description', 'error')} ">
	<label for="description">
		<g:message code="migFormdefVer.description.label" default="Description" />
		
	</label>
	<g:textArea name="description" cols="40" rows="5" maxlength="800" value="${migFormdefVerInst?.description}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: migFormdefVerInst, field: 'language', 'error')} ">
	<label for="language">
		<g:message code="migFormdefVer.language.label" default="Language" />
		
	</label>
	<g:textField name="language" maxlength="80" value="${migFormdefVerInst?.language}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: migFormdefVerInst, field: 'created', 'error')} required">
	<label for="created">
		<g:message code="migFormdefVer.created.label" default="Created" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="created" precision="day"  value="${migFormdefVerInst?.created}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: migFormdefVerInst, field: 'formdef', 'error')} required">
	<label for="formdef">
		<g:message code="migFormdefVer.formdef.label" default="Formdef" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="formdef" name="formdef.id" from="${org.motrice.migratrice.MigFormdef.list()}" optionKey="id" required="" value="${migFormdefVerInst?.formdef?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: migFormdefVerInst, field: 'formref', 'error')} required">
	<label for="formref">
		<g:message code="migFormdefVer.formref.label" default="Formref" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="formref" type="number" value="${migFormdefVerInst.formref}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: migFormdefVerInst, field: 'pkg', 'error')} required">
	<label for="pkg">
		<g:message code="migFormdefVer.pkg.label" default="Pkg" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="pkg" name="pkg.id" from="${org.motrice.migratrice.MigPackage.list()}" optionKey="id" required="" value="${migFormdefVerInst?.pkg?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: migFormdefVerInst, field: 'ref', 'error')} required">
	<label for="ref">
		<g:message code="migFormdefVer.ref.label" default="Ref" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="ref" type="number" value="${migFormdefVerInst.ref}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: migFormdefVerInst, field: 'verno', 'error')} required">
	<label for="verno">
		<g:message code="migFormdefVer.verno.label" default="Verno" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="verno" type="number" value="${migFormdefVerInst.verno}" required=""/>
</div>

