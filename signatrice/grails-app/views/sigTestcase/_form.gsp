<%@ page import="org.motrice.signatrice.SigTestcase" %>



<div class="fieldcontain ${hasErrors(bean: sigTestcaseInst, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="sigTestcase.name.label" default="Name" />
		
	</label>
	<g:textField name="name" maxlength="120" value="${sigTestcaseInst?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sigTestcaseInst, field: 'personalIdNo', 'error')} ">
	<label for="personalIdNo">
		<g:message code="sigTestcase.personalIdNo.label" default="Personal Id No" />
		
	</label>
	<g:textField name="personalIdNo" maxlength="12" value="${sigTestcaseInst?.personalIdNo}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sigTestcaseInst, field: 'results', 'error')} ">
	<label for="results">
		<g:message code="sigTestcase.results.label" default="Results" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${sigTestcaseInst?.results?}" var="r">
    <li><g:link controller="sigResult" action="show" id="${r.id}">${r?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="sigResult" action="create" params="['sigTestcase.id': sigTestcaseInst?.id]">${message(code: 'default.add.label', args: [message(code: 'sigResult.label', default: 'SigResult')])}</g:link>
</li>
</ul>

</div>

<div class="fieldcontain ${hasErrors(bean: sigTestcaseInst, field: 'displayName', 'error')} required">
	<label for="displayName">
		<g:message code="sigTestcase.displayName.label" default="Display Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="displayName" name="displayName.id" from="${org.motrice.signatrice.SigDisplayname.list()}" optionKey="id" required="" value="${sigTestcaseInst?.displayName?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sigTestcaseInst, field: 'policy', 'error')} required">
	<label for="policy">
		<g:message code="sigTestcase.policy.label" default="Policy" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="policy" name="policy.id" from="${org.motrice.signatrice.SigPolicy.list()}" optionKey="id" required="" value="${sigTestcaseInst?.policy?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sigTestcaseInst, field: 'service', 'error')} required">
	<label for="service">
		<g:message code="sigTestcase.service.label" default="Service" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="service" name="service.id" from="${org.motrice.signatrice.SigService.list()}" optionKey="id" required="" value="${sigTestcaseInst?.service?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sigTestcaseInst, field: 'userVisibleText', 'error')} ">
	<label for="userVisibleText">
		<g:message code="sigTestcase.userVisibleText.label" default="User Visible Text" />
		
	</label>
	<g:textField name="userVisibleText" value="${sigTestcaseInst?.userVisibleText}"/>
</div>

