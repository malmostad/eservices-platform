<%@ page import="org.motrice.coordinatrice.bonita.BnProcDef" %>



<div class="fieldcontain ${hasErrors(bean: bnProcDefInst, field: 'uuid', 'error')} ">
	<label for="uuid">
		<g:message code="bnProcDef.uuid.label" default="Uuid" />
		
	</label>
	<g:textArea name="uuid" cols="40" rows="5" maxlength="255" value="${bnProcDefInst?.uuid}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bnProcDefInst, field: 'state', 'error')} ">
	<label for="state">
		<g:message code="bnProcDef.state.label" default="State" />
		
	</label>
	<g:textArea name="state" cols="40" rows="5" maxlength="255" value="${bnProcDefInst?.state}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bnProcDefInst, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="bnProcDef.name.label" default="Name" />
		
	</label>
	<g:textArea name="name" cols="40" rows="5" maxlength="255" value="${bnProcDefInst?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bnProcDefInst, field: 'label', 'error')} ">
	<label for="label">
		<g:message code="bnProcDef.label.label" default="Label" />
		
	</label>
	<g:textArea name="label" cols="40" rows="5" maxlength="255" value="${bnProcDefInst?.label}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bnProcDefInst, field: 'vno', 'error')} ">
	<label for="vno">
		<g:message code="bnProcDef.vno.label" default="Vno" />
		
	</label>
	<g:textArea name="vno" cols="40" rows="5" maxlength="255" value="${bnProcDefInst?.vno}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bnProcDefInst, field: 'type', 'error')} ">
	<label for="type">
		<g:message code="bnProcDef.type.label" default="Type" />
		
	</label>
	<g:textArea name="type" cols="40" rows="5" maxlength="255" value="${bnProcDefInst?.type}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: bnProcDefInst, field: 'activities', 'error')} ">
	<label for="activities">
		<g:message code="bnProcDef.activities.label" default="Activities" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${bnProcDefInst?.activities?}" var="a">
    <li><g:link controller="bnActDef" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="bnActDef" action="create" params="['bnProcDef.id': bnProcDefInst?.id]">${message(code: 'default.add.label', args: [message(code: 'bnActDef.label', default: 'BnActDef')])}</g:link>
</li>
</ul>

</div>

<div class="fieldcontain ${hasErrors(bean: bnProcDefInst, field: 'deployedMillis', 'error')} required">
	<label for="deployedMillis">
		<g:message code="bnProcDef.deployedMillis.label" default="Deployed Millis" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="deployedMillis" type="number" value="${bnProcDefInst.deployedMillis}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: bnProcDefInst, field: 'undeployedMillis', 'error')} required">
	<label for="undeployedMillis">
		<g:message code="bnProcDef.undeployedMillis.label" default="Undeployed Millis" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="undeployedMillis" type="number" value="${bnProcDefInst.undeployedMillis}" required=""/>
</div>

