<%@ page import="org.motrice.tdocbox.TdbHttpVerb" %>



<div class="fieldcontain ${hasErrors(bean: tdbHttpVerbObj, field: 'verb', 'error')} ">
	<label for="verb">
		<g:message code="tdbHttpVerb.verb.label" default="Verb" />
		
	</label>
	<g:textField name="verb" maxlength="16" value="${tdbHttpVerbObj?.verb}"/>
</div>

