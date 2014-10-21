<%@ page import="org.motrice.tdocbox.TdbMethod" %>



<div class="fieldcontain ${hasErrors(bean: tdbMethodObj, field: 'urlPattern', 'error')} ">
	<label for="urlPattern">
		<g:message code="tdbMethod.urlPattern.label" default="Url Pattern" />
		
	</label>
	<g:textArea name="urlPattern" cols="40" rows="5" maxlength="512" value="${tdbMethodObj?.urlPattern}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: tdbMethodObj, field: 'description', 'error')} ">
	<label for="description">
		<g:message code="tdbMethod.description.label" default="Description" />
		
	</label>
	<g:textField name="description" maxlength="200" value="${tdbMethodObj?.description}"/>
</div>

