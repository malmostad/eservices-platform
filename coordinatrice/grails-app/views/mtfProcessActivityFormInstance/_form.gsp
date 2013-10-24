<%@ page import="org.motrice.coordinatrice.MtfProcessActivityFormInstance" %>



<div class="fieldcontain ${hasErrors(bean: mtfProcessActivityFormInstanceInst, field: 'activityInstanceUuid', 'error')} ">
	<label for="activityInstanceUuid">
		<g:message code="mtfProcessActivityFormInstance.activityInstanceUuid.label" default="Activity Instance Uuid" />
		
	</label>
	<g:textArea name="activityInstanceUuid" cols="40" rows="5" maxlength="255" value="${mtfProcessActivityFormInstanceInst?.activityInstanceUuid}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: mtfProcessActivityFormInstanceInst, field: 'formDocId', 'error')} ">
	<label for="formDocId">
		<g:message code="mtfProcessActivityFormInstance.formDocId.label" default="Form Doc Id" />
		
	</label>
	<g:textArea name="formDocId" cols="40" rows="5" maxlength="255" value="${mtfProcessActivityFormInstanceInst?.formDocId}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: mtfProcessActivityFormInstanceInst, field: 'formPath', 'error')} ">
	<label for="formPath">
		<g:message code="mtfProcessActivityFormInstance.formPath.label" default="Form Path" />
		
	</label>
	<g:textArea name="formPath" cols="40" rows="5" maxlength="255" value="${mtfProcessActivityFormInstanceInst?.formPath}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: mtfProcessActivityFormInstanceInst, field: 'processInstanceUuid', 'error')} ">
	<label for="processInstanceUuid">
		<g:message code="mtfProcessActivityFormInstance.processInstanceUuid.label" default="Process Instance Uuid" />
		
	</label>
	<g:textArea name="processInstanceUuid" cols="40" rows="5" maxlength="255" value="${mtfProcessActivityFormInstanceInst?.processInstanceUuid}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: mtfProcessActivityFormInstanceInst, field: 'submitted', 'error')} ">
	<label for="submitted">
		<g:message code="mtfProcessActivityFormInstance.submitted.label" default="Submitted" />
		
	</label>
	<g:datePicker name="submitted" precision="day"  value="${mtfProcessActivityFormInstanceInst?.submitted}" default="none" noSelection="['': '']" />
</div>

<div class="fieldcontain ${hasErrors(bean: mtfProcessActivityFormInstanceInst, field: 'userid', 'error')} ">
	<label for="userid">
		<g:message code="mtfProcessActivityFormInstance.userid.label" default="Userid" />
		
	</label>
	<g:textArea name="userid" cols="40" rows="5" maxlength="255" value="${mtfProcessActivityFormInstanceInst?.userid}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: mtfProcessActivityFormInstanceInst, field: 'startForm', 'error')} required">
	<label for="startForm">
		<g:message code="mtfProcessActivityFormInstance.startForm.label" default="Start Form" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="startForm" name="startForm.id" from="${org.motrice.coordinatrice.MtfStartFormDefinition.list()}" optionKey="id" required="" value="${mtfProcessActivityFormInstanceInst?.startForm?.id}" class="many-to-one"/>
</div>

