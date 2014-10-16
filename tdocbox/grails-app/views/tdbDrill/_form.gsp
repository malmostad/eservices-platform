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
<div class="fieldcontain ${hasErrors(bean: tdbDrillObj, field: 'mode', 'error')} required">
  <label for="mode">
    <g:message code="tdbDrill.mode.label" default="Mode" />
    <span class="required-indicator">*</span>
  </label>
  <g:select id="mode" name="mode.id" from="${org.motrice.tdocbox.TdbMode.list()}" optionKey="id" required="" value="${tdbDrillObj?.mode?.id}" class="many-to-one"/>
</div>
<div class="fieldcontain ${hasErrors(bean: tdbDrillObj, field: 'method', 'error')} required">
  <label for="method">
    <g:message code="tdbDrill.method.label" default="Method" />
    <span class="required-indicator">*</span>
  </label>
  <g:select id="method" name="method.id" from="${org.motrice.tdocbox.TdbMethod.list()}" optionKey="id" required="" value="${tdbDrillObj?.method?.id}" class="many-to-one"/>
</div>
<div class="fieldcontain ${hasErrors(bean: tdbDrillObj, field: 'queryString', 'error')} ">
  <label for="queryString">
    <g:message code="tdbDrill.queryString.label" default="Query String" />
  </label>
  <g:textField name="queryString" maxlength="400" value="${tdbDrillObj?.queryString}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: tdbDrillObj, field: 'parameters', 'error')} ">
  <label for="parameters">
    <g:message code="tdbDrill.parameters.label" default="Parameters" />
  </label>
  <ul class="one-to-many">
    <g:each in="${tdbDrillObj?.parameters?}" var="p">
      <li><g:link controller="tdbParameter" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link></li>
    </g:each>
    <li class="add">
      <g:link controller="tdbParameter" action="create" params="['tdbDrill.id': tdbDrillObj?.id]">${message(code: 'default.add.label', args: [message(code: 'tdbParameter.label', default: 'TdbParameter')])}</g:link>
    </li>
  </ul>
</div>
<div class="fieldcontain ${hasErrors(bean: tdbDrillObj, field: 'suite', 'error')} required">
  <label for="suite">
    <g:message code="tdbDrill.suite.label" default="Suite" />
    <span class="required-indicator">*</span>
  </label>
  <g:select id="suite" name="suite.id" from="${org.motrice.tdocbox.TdbSuite.list()}" optionKey="id" required="" value="${tdbDrillObj?.suite?.id}" class="many-to-one"/>
</div>

