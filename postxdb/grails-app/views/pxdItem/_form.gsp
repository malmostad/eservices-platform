<%@ page import="org.motrice.postxdb.PxdItem" %>



<div class="fieldcontain ${hasErrors(bean: pxdItemObj, field: 'path', 'error')} ">
	<label for="path">
		<g:message code="pxdItem.path.label" default="Path" />
		
	</label>
	<g:textField name="path" value="${pxdItemObj?.path}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdItemObj, field: 'uuid', 'error')} ">
	<label for="uuid">
		<g:message code="pxdItem.uuid.label" default="Uuid" />
		
	</label>
	<g:textField name="uuid" maxlength="200" value="${pxdItemObj?.uuid}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdItemObj, field: 'formDef', 'error')} ">
	<label for="formDef">
		<g:message code="pxdItem.formDef.label" default="Form Def" />
		
	</label>
	<g:textArea name="formDef" cols="40" rows="5" maxlength="400" value="${pxdItemObj?.formDef}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdItemObj, field: 'format', 'error')} ">
	<label for="format">
		<g:message code="pxdItem.format.label" default="Format" />
		
	</label>
	<g:textField name="format" maxlength="80" value="${pxdItemObj?.format}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdItemObj, field: 'size', 'error')} required">
	<label for="size">
		<g:message code="pxdItem.size.label" default="Size" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="size" from="${0..2147483646}" class="range" required="" value="${fieldValue(bean: pxdItemObj, field: 'size')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdItemObj, field: 'text', 'error')} ">
	<label for="text">
		<g:message code="pxdItem.text.label" default="Text" />
		
	</label>
	<g:textField name="text" value="${pxdItemObj?.text}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdItemObj, field: 'stream', 'error')} ">
	<label for="stream">
		<g:message code="pxdItem.stream.label" default="Stream" />
		
	</label>
	<input type="file" id="stream" name="stream" />
</div>

