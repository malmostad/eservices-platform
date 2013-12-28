<%@ page import="org.motrice.orifice.OriFormdefVer" %>



<div class="fieldcontain ${hasErrors(bean: oriFormdefVerInst, field: 'app', 'error')} ">
	<label for="app">
		<g:message code="oriFormdefVer.app.label" default="App" />
		
	</label>
	<g:textField name="app" maxlength="120" value="${oriFormdefVerInst?.app}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriFormdefVerInst, field: 'form', 'error')} ">
	<label for="form">
		<g:message code="oriFormdefVer.form.label" default="Form" />
		
	</label>
	<g:textField name="form" maxlength="120" value="${oriFormdefVerInst?.form}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriFormdefVerInst, field: 'path', 'error')} ">
	<label for="path">
		<g:message code="oriFormdefVer.path.label" default="Path" />
		
	</label>
	<g:textArea name="path" cols="40" rows="5" maxlength="400" value="${oriFormdefVerInst?.path}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriFormdefVerInst, field: 'draft', 'error')} ">
	<label for="draft">
		<g:message code="oriFormdefVer.draft.label" default="Draft" />
		
	</label>
	<g:field name="draft" type="number" value="${oriFormdefVerInst.draft}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriFormdefVerInst, field: 'published', 'error')} ">
	<label for="published">
		<g:message code="oriFormdefVer.published.label" default="Published" />
		
	</label>
	<g:checkBox name="published" value="${oriFormdefVerInst?.published}" />
</div>

<div class="fieldcontain ${hasErrors(bean: oriFormdefVerInst, field: 'title', 'error')} ">
	<label for="title">
		<g:message code="oriFormdefVer.title.label" default="Title" />
		
	</label>
	<g:textField name="title" maxlength="120" value="${oriFormdefVerInst?.title}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriFormdefVerInst, field: 'description', 'error')} ">
	<label for="description">
		<g:message code="oriFormdefVer.description.label" default="Description" />
		
	</label>
	<g:textArea name="description" cols="40" rows="5" maxlength="800" value="${oriFormdefVerInst?.description}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriFormdefVerInst, field: 'language', 'error')} ">
	<label for="language">
		<g:message code="oriFormdefVer.language.label" default="Language" />
		
	</label>
	<g:textField name="language" maxlength="80" value="${oriFormdefVerInst?.language}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriFormdefVerInst, field: 'created', 'error')} required">
	<label for="created">
		<g:message code="oriFormdefVer.created.label" default="Created" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="created" precision="day"  value="${oriFormdefVerInst?.created}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: oriFormdefVerInst, field: 'formdef', 'error')} required">
	<label for="formdef">
		<g:message code="oriFormdefVer.formdef.label" default="Formdef" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="formdef" name="formdef.id" from="${org.motrice.orifice.OriFormdef.list()}" optionKey="id" required="" value="${oriFormdefVerInst?.formdef?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriFormdefVerInst, field: 'formref', 'error')} required">
	<label for="formref">
		<g:message code="oriFormdefVer.formref.label" default="Formref" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="formref" type="number" value="${oriFormdefVerInst.formref}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriFormdefVerInst, field: 'pkg', 'error')} required">
	<label for="pkg">
		<g:message code="oriFormdefVer.pkg.label" default="Pkg" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="pkg" name="pkg.id" from="${org.motrice.orifice.OriPackage.list()}" optionKey="id" required="" value="${oriFormdefVerInst?.pkg?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriFormdefVerInst, field: 'ref', 'error')} required">
	<label for="ref">
		<g:message code="oriFormdefVer.ref.label" default="Ref" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="ref" type="number" value="${oriFormdefVerInst.ref}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriFormdefVerInst, field: 'verno', 'error')} required">
	<label for="verno">
		<g:message code="oriFormdefVer.verno.label" default="Verno" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="verno" type="number" value="${oriFormdefVerInst.verno}" required=""/>
</div>

