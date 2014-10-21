<%@ page import="org.motrice.tdocbox.TdbProperty" %>



<div class="fieldcontain ${hasErrors(bean: tdbPropertyObj, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="tdbProperty.name.label" default="Name" />
		
	</label>
	<g:textField name="name" maxlength="80" value="${tdbPropertyObj?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: tdbPropertyObj, field: 'value', 'error')} ">
	<label for="value">
		<g:message code="tdbProperty.value.label" default="Value" />
		
	</label>
	<g:textField name="value" value="${tdbPropertyObj?.value}"/>
</div>

