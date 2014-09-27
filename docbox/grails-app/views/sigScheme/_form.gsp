<%@ page import="org.motrice.signatrice.SigScheme" %>



<div class="fieldcontain ${hasErrors(bean: sigSchemeObj, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="sigScheme.name.label" default="Name" />
		
	</label>
	<g:textField name="name" maxlength="120" value="${sigSchemeObj?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sigSchemeObj, field: 'personalIdNo', 'error')} ">
	<label for="personalIdNo">
		<g:message code="sigScheme.personalIdNo.label" default="Personal Id No" />
		
	</label>
	<g:textField name="personalIdNo" maxlength="12" value="${sigSchemeObj?.personalIdNo}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sigSchemeObj, field: 'results', 'error')} ">
	<label for="results">
		<g:message code="sigScheme.results.label" default="Results" />
		
	</label>
	<g:select name="results" from="${org.motrice.signatrice.SigResult.list()}" multiple="multiple" optionKey="id" size="5" value="${sigSchemeObj?.results*.id}" class="many-to-many"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sigSchemeObj, field: 'displayName', 'error')} required">
	<label for="displayName">
		<g:message code="sigScheme.displayName.label" default="Display Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="displayName" name="displayName.id" from="${org.motrice.signatrice.SigDisplayname.list()}" optionKey="id" required="" value="${sigSchemeObj?.displayName?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sigSchemeObj, field: 'policy', 'error')} required">
	<label for="policy">
		<g:message code="sigScheme.policy.label" default="Policy" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="policy" name="policy.id" from="${org.motrice.signatrice.SigPolicy.list()}" optionKey="id" required="" value="${sigSchemeObj?.policy?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sigSchemeObj, field: 'service', 'error')} required">
	<label for="service">
		<g:message code="sigScheme.service.label" default="Service" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="service" name="service.id" from="${org.motrice.signatrice.SigService.list()}" optionKey="id" required="" value="${sigSchemeObj?.service?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sigSchemeObj, field: 'userVisibleText', 'error')} ">
	<label for="userVisibleText">
		<g:message code="sigScheme.userVisibleText.label" default="User Visible Text" />
		
	</label>
	<g:textField name="userVisibleText" value="${sigSchemeObj?.userVisibleText}"/>
</div>

