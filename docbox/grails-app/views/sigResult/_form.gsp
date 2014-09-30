<%@ page import="org.motrice.signatrice.SigResult" %>



<div class="fieldcontain ${hasErrors(bean: sigResultInst, field: 'transactionId', 'error')} ">
	<label for="transactionId">
		<g:message code="sigResult.transactionId.label" default="Transaction Id" />
		
	</label>
	<g:textField name="transactionId" maxlength="32" value="${sigResultInst?.transactionId}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sigResultInst, field: 'orderRef', 'error')} ">
	<label for="orderRef">
		<g:message code="sigResult.orderRef.label" default="Order Ref" />
		
	</label>
	<g:textField name="orderRef" maxlength="120" value="${sigResultInst?.orderRef}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sigResultInst, field: 'autoStartToken', 'error')} ">
	<label for="autoStartToken">
		<g:message code="sigResult.autoStartToken.label" default="Auto Start Token" />
		
	</label>
	<g:textField name="autoStartToken" maxlength="120" value="${sigResultInst?.autoStartToken}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sigResultInst, field: 'personalIdNo', 'error')} ">
	<label for="personalIdNo">
		<g:message code="sigResult.personalIdNo.label" default="Personal Id No" />
		
	</label>
	<g:textField name="personalIdNo" value="${sigResultInst?.personalIdNo}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sigResultInst, field: 'progressStatus', 'error')} ">
	<label for="progressStatus">
		<g:message code="sigResult.progressStatus.label" default="Progress Status" />
		
	</label>
	<g:select id="progressStatus" name="progressStatus.id" from="${org.motrice.signatrice.SigProgress.list()}" optionKey="id" value="${sigResultInst?.progressStatus?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sigResultInst, field: 'faultStatus', 'error')} ">
	<label for="faultStatus">
		<g:message code="sigResult.faultStatus.label" default="Fault Status" />
		
	</label>
	<g:select id="faultStatus" name="faultStatus.id" from="${org.motrice.signatrice.SigFaultObj.list()}" optionKey="id" value="${sigResultInst?.faultStatus?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sigResultInst, field: 'signature', 'error')} ">
	<label for="signature">
		<g:message code="sigResult.signature.label" default="Signature" />
		
	</label>
	<input type="file" id="signature" name="signature" />
</div>

<div class="fieldcontain ${hasErrors(bean: sigResultInst, field: 'attrs', 'error')} ">
	<label for="attrs">
		<g:message code="sigResult.attrs.label" default="Attrs" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${sigResultInst?.attrs?}" var="a">
    <li><g:link controller="sigAttribute" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="sigAttribute" action="create" params="['sigResult.id': sigResultInst?.id]">${message(code: 'default.add.label', args: [message(code: 'sigAttribute.label', default: 'SigAttribute')])}</g:link>
</li>
</ul>

</div>

<div class="fieldcontain ${hasErrors(bean: sigResultInst, field: 'displayName', 'error')} required">
	<label for="displayName">
		<g:message code="sigResult.displayName.label" default="Display Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="displayName" name="displayName.id" from="${org.motrice.signatrice.SigDisplayname.list()}" optionKey="id" required="" value="${sigResultInst?.displayName?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sigResultInst, field: 'policy', 'error')} required">
	<label for="policy">
		<g:message code="sigResult.policy.label" default="Policy" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="policy" name="policy.id" from="${org.motrice.signatrice.SigPolicy.list()}" optionKey="id" required="" value="${sigResultInst?.policy?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sigResultInst, field: 'tcase', 'error')} required">
	<label for="tcase">
		<g:message code="sigResult.tcase.label" default="Tcase" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="tcase" name="tcase.id" from="${org.motrice.signatrice.SigScheme.list()}" optionKey="id" required="" value="${sigResultInst?.tcase?.id}" class="many-to-one"/>
</div>

