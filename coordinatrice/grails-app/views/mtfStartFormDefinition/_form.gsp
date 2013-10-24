<%@ page import="org.motrice.coordinatrice.MtfStartFormDefinition" %>



<div class="fieldcontain ${hasErrors(bean: mtfStartFormDefinitionInst, field: 'authTypeReq', 'error')} ">
	<label for="authTypeReq">
		<g:message code="mtfStartFormDefinition.authTypeReq.label" default="Auth Type Req" />
		
	</label>
	<g:textArea name="authTypeReq" cols="40" rows="5" maxlength="255" value="${mtfStartFormDefinitionInst?.authTypeReq}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: mtfStartFormDefinitionInst, field: 'formPath', 'error')} ">
	<label for="formPath">
		<g:message code="mtfStartFormDefinition.formPath.label" default="Form Path" />
		
	</label>
	<g:textArea name="formPath" cols="40" rows="5" maxlength="255" value="${mtfStartFormDefinitionInst?.formPath}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: mtfStartFormDefinitionInst, field: 'processDefinitionUuid', 'error')} ">
	<label for="processDefinitionUuid">
		<g:message code="mtfStartFormDefinition.processDefinitionUuid.label" default="Process Definition Uuid" />
		
	</label>
	<g:textArea name="processDefinitionUuid" cols="40" rows="5" maxlength="255" value="${mtfStartFormDefinitionInst?.processDefinitionUuid}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: mtfStartFormDefinitionInst, field: 'userDataXpath', 'error')} ">
	<label for="userDataXpath">
		<g:message code="mtfStartFormDefinition.userDataXpath.label" default="User Data Xpath" />
		
	</label>
	<g:textArea name="userDataXpath" cols="40" rows="5" maxlength="255" value="${mtfStartFormDefinitionInst?.userDataXpath}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: mtfStartFormDefinitionInst, field: 'activityFormDefs', 'error')} ">
	<label for="activityFormDefs">
		<g:message code="mtfStartFormDefinition.activityFormDefs.label" default="Activity Form Defs" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${mtfStartFormDefinitionInst?.activityFormDefs?}" var="a">
    <li><g:link controller="mtfActivityFormDefinition" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="mtfActivityFormDefinition" action="create" params="['mtfStartFormDefinition.id': mtfStartFormDefinitionInst?.id]">${message(code: 'default.add.label', args: [message(code: 'mtfActivityFormDefinition.label', default: 'MtfActivityFormDefinition')])}</g:link>
</li>
</ul>

</div>

<div class="fieldcontain ${hasErrors(bean: mtfStartFormDefinitionInst, field: 'activityFormInst', 'error')} ">
	<label for="activityFormInst">
		<g:message code="mtfStartFormDefinition.activityFormInst.label" default="Activity Form Inst" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${mtfStartFormDefinitionInst?.activityFormInst?}" var="a">
    <li><g:link controller="mtfProcessActivityFormInstance" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="mtfProcessActivityFormInstance" action="create" params="['mtfStartFormDefinition.id': mtfStartFormDefinitionInst?.id]">${message(code: 'default.add.label', args: [message(code: 'mtfProcessActivityFormInstance.label', default: 'MtfProcessActivityFormInstance')])}</g:link>
</li>
</ul>

</div>

