<%@ page import="org.motrice.docbox.doc.BoxDoc" %>



<div class="fieldcontain ${hasErrors(bean: boxDocObj, field: 'docNo', 'error')} ">
	<label for="docNo">
		<g:message code="boxDoc.docNo.label" default="Doc No" />
		
	</label>
	<g:textField name="docNo" maxlength="16" value="${boxDocObj?.docNo}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: boxDocObj, field: 'formDataUuid', 'error')} ">
	<label for="formDataUuid">
		<g:message code="boxDoc.formDataUuid.label" default="Form Data Uuid" />
		
	</label>
	<g:textField name="formDataUuid" maxlength="200" value="${boxDocObj?.formDataUuid}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: boxDocObj, field: 'steps', 'error')} ">
	<label for="steps">
		<g:message code="boxDoc.steps.label" default="Steps" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${boxDocObj?.steps?}" var="s">
    <li><g:link controller="boxDocStep" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="boxDocStep" action="create" params="['boxDoc.id': boxDocObj?.id]">${message(code: 'default.add.label', args: [message(code: 'boxDocStep.label', default: 'BoxDocStep')])}</g:link>
</li>
</ul>

</div>

