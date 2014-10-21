<%@ page import="org.motrice.tdocbox.TdbMode" %>



<div class="fieldcontain ${hasErrors(bean: tdbModeObj, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="tdbMode.name.label" default="Name" />
		
	</label>
	<g:textField name="name" maxlength="40" value="${tdbModeObj?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: tdbModeObj, field: 'description', 'error')} ">
	<label for="description">
		<g:message code="tdbMode.description.label" default="Description" />
		
	</label>
	<g:textArea name="description" cols="40" rows="5" maxlength="400" value="${tdbModeObj?.description}"/>
</div>

