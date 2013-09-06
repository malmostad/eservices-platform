<%@ page import="org.motrice.postxdb.PxdFormdef" %>



<div class="fieldcontain ${hasErrors(bean: pxdFormdefObj, field: 'path', 'error')} ">
	<label for="path">
		<g:message code="pxdFormdef.path.label" default="Path" />
		
	</label>
	<g:textArea name="path" cols="40" rows="5" maxlength="256" value="${pxdFormdefObj?.path}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefObj, field: 'uuid', 'error')} ">
	<label for="uuid">
		<g:message code="pxdFormdef.uuid.label" default="Uuid" />
		
	</label>
	<g:textField name="uuid" maxlength="200" value="${pxdFormdefObj?.uuid}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefObj, field: 'appName', 'error')} ">
	<label for="appName">
		<g:message code="pxdFormdef.appName.label" default="App Name" />
		
	</label>
	<g:textField name="appName" maxlength="120" value="${pxdFormdefObj?.appName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefObj, field: 'formName', 'error')} ">
	<label for="formName">
		<g:message code="pxdFormdef.formName.label" default="Form Name" />
		
	</label>
	<g:textField name="formName" maxlength="120" value="${pxdFormdefObj?.formName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefObj, field: 'currentDraft', 'error')} ">
	<label for="currentDraft">
		<g:message code="pxdFormdef.currentDraft.label" default="Current Draft" />
		
	</label>
	<g:textArea name="currentDraft" cols="40" rows="5" maxlength="400" value="${pxdFormdefObj?.currentDraft}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefObj, field: 'forms', 'error')} ">
	<label for="forms">
		<g:message code="pxdFormdef.forms.label" default="Forms" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${pxdFormdefObj?.forms?}" var="f">
    <li><g:link controller="pxdFormdefVer" action="show" id="${f.id}">${f?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="pxdFormdefVer" action="create" params="['pxdFormdef.id': pxdFormdefObj?.id]">${message(code: 'default.add.label', args: [message(code: 'pxdFormdefVer.label', default: 'PxdFormdefVer')])}</g:link>
</li>
</ul>

</div>

