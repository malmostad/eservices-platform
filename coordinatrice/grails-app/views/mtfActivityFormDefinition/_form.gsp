<%@ page import="org.motrice.coordinatrice.MtfActivityFormDefinition" %>



<div class="fieldcontain ${hasErrors(bean: mtfActivityFormDefinitionInst, field: 'activityDefinitionUuid', 'error')} ">
	<label for="activityDefinitionUuid">
		<g:message code="mtfActivityFormDefinition.activityDefinitionUuid.label" default="Activity Definition Uuid" />
		
	</label>
	<g:textArea name="activityDefinitionUuid" cols="40" rows="5" maxlength="255" value="${mtfActivityFormDefinitionInst?.activityDefinitionUuid}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: mtfActivityFormDefinitionInst, field: 'formPath', 'error')} ">
	<label for="formPath">
		<g:message code="mtfActivityFormDefinition.formPath.label" default="Form Path" />
		
	</label>
	<g:textArea name="formPath" cols="40" rows="5" maxlength="255" value="${mtfActivityFormDefinitionInst?.formPath}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: mtfActivityFormDefinitionInst, field: 'startForm', 'error')} required">
	<label for="startForm">
		<g:message code="mtfActivityFormDefinition.startForm.label" default="Start Form" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="startForm" name="startForm.id" from="${org.motrice.coordinatrice.MtfStartFormDefinition.list()}" optionKey="id" required="" value="${mtfActivityFormDefinitionInst?.startForm?.id}" class="many-to-one"/>
</div>

