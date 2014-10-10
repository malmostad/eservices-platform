<%@ page import="org.motrice.tdocbox.TdbDrill" %>



<div class="fieldcontain ${hasErrors(bean: tdbDrillObj, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="tdbDrill.name.label" default="Name" />
		
	</label>
	<g:textField name="name" maxlength="40" value="${tdbDrillObj?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: tdbDrillObj, field: 'verb', 'error')} required">
	<label for="verb">
		<g:message code="tdbDrill.verb.label" default="Verb" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="verb" name="verb.id" from="${org.motrice.tdocbox.TdbHttpVerb.list()}" optionKey="id" required="" value="${tdbDrillObj?.verb?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: tdbDrillObj, field: 'method', 'error')} required">
	<label for="method">
		<g:message code="tdbDrill.method.label" default="Method" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="method" name="method.id" from="${org.motrice.tdocbox.TdbMethod.list()}" optionKey="id" required="" value="${tdbDrillObj?.method?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: tdbDrillObj, field: 'parameters', 'error')} required">
	<label for="parameters">
		<g:message code="tdbDrill.parameters.label" default="Parameters" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="parameters" from="${org.motrice.tdocbox.TdbParameter.list()}" multiple="multiple" optionKey="id" size="5" required="" value="${tdbDrillObj?.parameters*.id}" class="many-to-many"/>
</div>

