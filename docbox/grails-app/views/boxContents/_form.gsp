<%@ page import="org.motrice.docbox.doc.BoxContents" %>



<div class="fieldcontain ${hasErrors(bean: boxContentsObj, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="boxContents.name.label" default="Name" />
		
	</label>
	<g:textField name="name" maxlength="80" value="${boxContentsObj?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: boxContentsObj, field: 'format', 'error')} ">
	<label for="format">
		<g:message code="boxContents.format.label" default="Format" />
		
	</label>
	<g:textField name="format" maxlength="80" value="${boxContentsObj?.format}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: boxContentsObj, field: 'size', 'error')} required">
	<label for="size">
		<g:message code="boxContents.size.label" default="Size" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="size" from="${0..2147483646}" class="range" required="" value="${fieldValue(bean: boxContentsObj, field: 'size')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: boxContentsObj, field: 'text', 'error')} ">
	<label for="text">
		<g:message code="boxContents.text.label" default="Text" />
		
	</label>
	<g:textField name="text" value="${boxContentsObj?.text}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: boxContentsObj, field: 'stream', 'error')} ">
	<label for="stream">
		<g:message code="boxContents.stream.label" default="Stream" />
		
	</label>
	<input type="file" id="stream" name="stream" />
</div>

<div class="fieldcontain ${hasErrors(bean: boxContentsObj, field: 'step', 'error')} required">
	<label for="step">
		<g:message code="boxContents.step.label" default="Step" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="step" name="step.id" from="${org.motrice.docbox.doc.BoxDocStep.list()}" optionKey="id" required="" value="${boxContentsObj?.step?.id}" class="many-to-one"/>
</div>

