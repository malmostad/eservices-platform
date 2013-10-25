<%@ page import="org.motrice.coordinatrice.bonita.BnActDef" %>



<div class="fieldcontain ${hasErrors(bean: bnActDefInst, field: 'uuid', 'error')} ">
	<label for="uuid">
		<g:message code="bnActDef.uuid.label" default="Uuid" />
		
	</label>
	<g:textArea name="uuid" cols="40" rows="5" maxlength="255" value="${bnActDefInst?.uuid}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bnActDefInst, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="bnActDef.name.label" default="Name" />
		
	</label>
	<g:textArea name="name" cols="40" rows="5" maxlength="255" value="${bnActDefInst?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bnActDefInst, field: 'label', 'error')} ">
	<label for="label">
		<g:message code="bnActDef.label.label" default="Label" />
		
	</label>
	<g:textArea name="label" cols="40" rows="5" maxlength="255" value="${bnActDefInst?.label}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bnActDefInst, field: 'type', 'error')} ">
	<label for="type">
		<g:message code="bnActDef.type.label" default="Type" />
		
	</label>
	<g:textArea name="type" cols="40" rows="5" maxlength="255" value="${bnActDefInst?.type}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bnActDefInst, field: 'process', 'error')} required">
	<label for="process">
		<g:message code="bnActDef.process.label" default="Process" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="process" name="process.id" from="${org.motrice.coordinatrice.bonita.BnProcDef.list()}" optionKey="id" required="" value="${bnActDefInst?.process?.id}" class="many-to-one"/>
</div>

