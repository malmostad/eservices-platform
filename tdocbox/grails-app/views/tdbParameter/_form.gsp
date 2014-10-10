<%@ page import="org.motrice.tdocbox.TdbParameter" %>



<div class="fieldcontain ${hasErrors(bean: tdbParameterObj, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="tdbParameter.name.label" default="Name" />
		
	</label>
	<g:textField name="name" maxlength="64" value="${tdbParameterObj?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: tdbParameterObj, field: 'value', 'error')} ">
	<label for="value">
		<g:message code="tdbParameter.value.label" default="Value" />
		
	</label>
	<g:textArea name="value" cols="40" rows="5" maxlength="400" value="${tdbParameterObj?.value}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: tdbParameterObj, field: 'description', 'error')} ">
	<label for="description">
		<g:message code="tdbParameter.description.label" default="Description" />
		
	</label>
	<g:textArea name="description" cols="40" rows="5" maxlength="400" value="${tdbParameterObj?.description}"/>
</div>

