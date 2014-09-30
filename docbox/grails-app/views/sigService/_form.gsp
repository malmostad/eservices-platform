<%@ page import="org.motrice.signatrice.SigService" %>



<div class="fieldcontain ${hasErrors(bean: sigServiceInst, field: 'wsdlLocation', 'error')} ">
	<label for="wsdlLocation">
		<g:message code="sigService.wsdlLocation.label" default="Wsdl Location" />
		
	</label>
	<g:textField name="wsdlLocation" maxlength="200" value="${sigServiceInst?.wsdlLocation}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sigServiceInst, field: 'alias', 'error')} ">
	<label for="alias">
		<g:message code="sigService.alias.label" default="Alias" />
		
	</label>
	<g:textField name="alias" maxlength="24" pattern="${sigServiceInst.constraints.alias.matches}" value="${sigServiceInst?.alias}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sigServiceInst, field: 'qNameUri', 'error')} ">
	<label for="qNameUri">
		<g:message code="sigService.qNameUri.label" default="QN ame Uri" />
		
	</label>
	<g:textField name="qNameUri" maxlength="200" value="${sigServiceInst?.qNameUri}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sigServiceInst, field: 'qNameLocalPart', 'error')} ">
	<label for="qNameLocalPart">
		<g:message code="sigService.qNameLocalPart.label" default="QN ame Local Part" />
		
	</label>
	<g:textField name="qNameLocalPart" maxlength="120" value="${sigServiceInst?.qNameLocalPart}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sigServiceInst, field: 'cases', 'error')} ">
	<label for="cases">
		<g:message code="sigService.cases.label" default="Cases" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${sigServiceInst?.cases?}" var="c">
    <li><g:link controller="sigScheme" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="sigScheme" action="create" params="['sigService.id': sigServiceInst?.id]">${message(code: 'default.add.label', args: [message(code: 'sigScheme.label', default: 'SigScheme')])}</g:link>
</li>
</ul>

</div>

<div class="fieldcontain ${hasErrors(bean: sigServiceInst, field: 'defaultDisplayName', 'error')} required">
	<label for="defaultDisplayName">
		<g:message code="sigService.defaultDisplayName.label" default="Default Display Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="defaultDisplayName" name="defaultDisplayName.id" from="${org.motrice.signatrice.SigDisplayname.list()}" optionKey="id" required="" value="${sigServiceInst?.defaultDisplayName?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sigServiceInst, field: 'defaultPolicy', 'error')} required">
	<label for="defaultPolicy">
		<g:message code="sigService.defaultPolicy.label" default="Default Policy" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="defaultPolicy" name="defaultPolicy.id" from="${org.motrice.signatrice.SigPolicy.list()}" optionKey="id" required="" value="${sigServiceInst?.defaultPolicy?.id}" class="many-to-one"/>
</div>

