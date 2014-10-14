<%@ page import="org.motrice.tdocbox.TdbItem" %>



<div class="fieldcontain ${hasErrors(bean: tdbItemObj, field: 'binaryFlag', 'error')} ">
	<label for="binaryFlag">
		<g:message code="tdbItem.binaryFlag.label" default="Binary Flag" />
		
	</label>
	<g:checkBox name="binaryFlag" value="${tdbItemObj?.binaryFlag}" />
</div>

<div class="fieldcontain ${hasErrors(bean: tdbItemObj, field: 'text', 'error')} ">
	<label for="text">
		<g:message code="tdbItem.text.label" default="Text" />
		
	</label>
	<g:textField name="text" value="${tdbItemObj?.text}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: tdbItemObj, field: 'bytes', 'error')} ">
	<label for="bytes">
		<g:message code="tdbItem.bytes.label" default="Bytes" />
		
	</label>
	<input type="file" id="bytes" name="bytes" />
</div>

<div class="fieldcontain ${hasErrors(bean: tdbItemObj, field: 'case', 'error')} required">
	<label for="case">
		<g:message code="tdbItem.case.label" default="Case" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="case" name="case.id" from="${org.motrice.tdocbox.TdbCase.list()}" optionKey="id" required="" value="${tdbItemObj?.case?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: tdbItemObj, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="tdbItem.name.label" default="Name" />
		
	</label>
	<g:textField name="name" value="${tdbItemObj?.name}"/>
</div>

