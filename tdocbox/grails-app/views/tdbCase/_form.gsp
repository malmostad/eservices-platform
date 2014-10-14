<%@ page import="org.motrice.tdocbox.TdbCase" %>



<div class="fieldcontain ${hasErrors(bean: tdbCaseObj, field: 'timeStamp', 'error')} required">
	<label for="timeStamp">
		<g:message code="tdbCase.timeStamp.label" default="Time Stamp" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="timeStamp" precision="day"  value="${tdbCaseObj?.timeStamp}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: tdbCaseObj, field: 'exception', 'error')} ">
	<label for="exception">
		<g:message code="tdbCase.exception.label" default="Exception" />
		
	</label>
	<g:textArea name="exception" cols="40" rows="5" maxlength="400" value="${tdbCaseObj?.exception}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: tdbCaseObj, field: 'drill', 'error')} required">
	<label for="drill">
		<g:message code="tdbCase.drill.label" default="Drill" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="drill" name="drill.id" from="${org.motrice.tdocbox.TdbDrill.list()}" optionKey="id" required="" value="${tdbCaseObj?.drill?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: tdbCaseObj, field: 'items', 'error')} ">
	<label for="items">
		<g:message code="tdbCase.items.label" default="Items" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${tdbCaseObj?.items?}" var="i">
    <li><g:link controller="tdbItem" action="show" id="${i.id}">${i?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="tdbItem" action="create" params="['tdbCase.id': tdbCaseObj?.id]">${message(code: 'default.add.label', args: [message(code: 'tdbItem.label', default: 'TdbItem')])}</g:link>
</li>
</ul>

</div>

