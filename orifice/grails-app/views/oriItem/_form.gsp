<%@ page import="org.motrice.orifice.OriItem" %>



<div class="fieldcontain ${hasErrors(bean: oriItemInst, field: 'formref', 'error')} ">
	<label for="formref">
		<g:message code="oriItem.formref.label" default="Formref" />
		
	</label>
	<g:field name="formref" type="number" value="${oriItemInst.formref}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriItemInst, field: 'path', 'error')} ">
	<label for="path">
		<g:message code="oriItem.path.label" default="Path" />
		
	</label>
	<g:textArea name="path" cols="40" rows="5" maxlength="400" value="${oriItemInst?.path}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriItemInst, field: 'uuid', 'error')} ">
	<label for="uuid">
		<g:message code="oriItem.uuid.label" default="Uuid" />
		
	</label>
	<g:textField name="uuid" maxlength="200" value="${oriItemInst?.uuid}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriItemInst, field: 'formDef', 'error')} ">
	<label for="formDef">
		<g:message code="oriItem.formDef.label" default="Form Def" />
		
	</label>
	<g:textArea name="formDef" cols="40" rows="5" maxlength="400" value="${oriItemInst?.formDef}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriItemInst, field: 'format', 'error')} ">
	<label for="format">
		<g:message code="oriItem.format.label" default="Format" />
		
	</label>
	<g:textField name="format" maxlength="80" value="${oriItemInst?.format}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriItemInst, field: 'size', 'error')} required">
	<label for="size">
		<g:message code="oriItem.size.label" default="Size" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="size" from="${0..2147483646}" class="range" required="" value="${fieldValue(bean: oriItemInst, field: 'size')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriItemInst, field: 'text', 'error')} ">
	<label for="text">
		<g:message code="oriItem.text.label" default="Text" />
		
	</label>
	<g:textField name="text" value="${oriItemInst?.text}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriItemInst, field: 'stream', 'error')} ">
	<label for="stream">
		<g:message code="oriItem.stream.label" default="Stream" />
		
	</label>
	<input type="file" id="stream" name="stream" />
</div>

<div class="fieldcontain ${hasErrors(bean: oriItemInst, field: 'sha1', 'error')} ">
	<label for="sha1">
		<g:message code="oriItem.sha1.label" default="Sha1" />
		
	</label>
	<g:textArea name="sha1" cols="40" rows="5" maxlength="400" value="${oriItemInst?.sha1}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriItemInst, field: 'created', 'error')} required">
	<label for="created">
		<g:message code="oriItem.created.label" default="Created" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="created" precision="day"  value="${oriItemInst?.created}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: oriItemInst, field: 'formdef', 'error')} required">
	<label for="formdef">
		<g:message code="oriItem.formdef.label" default="Formdef" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="formdef" name="formdef.id" from="${org.motrice.orifice.OriFormdef.list()}" optionKey="id" required="" value="${oriItemInst?.formdef?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriItemInst, field: 'pkg', 'error')} required">
	<label for="pkg">
		<g:message code="oriItem.pkg.label" default="Pkg" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="pkg" name="pkg.id" from="${org.motrice.orifice.OriPackage.list()}" optionKey="id" required="" value="${oriItemInst?.pkg?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriItemInst, field: 'ref', 'error')} required">
	<label for="ref">
		<g:message code="oriItem.ref.label" default="Ref" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="ref" type="number" value="${oriItemInst.ref}" required=""/>
</div>

