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

