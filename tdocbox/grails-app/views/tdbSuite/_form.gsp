<%@ page import="org.motrice.tdocbox.TdbSuite" %>
<div class="fieldcontain ${hasErrors(bean: tdbSuiteObj, field: 'name', 'error')} ">
  <label for="name">
    <g:message code="tdbSuite.name.label" default="Name" />
  </label>
  <g:textField name="name" maxlength="40" value="${tdbSuiteObj?.name}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: tdbSuiteObj, field: 'description', 'error')} ">
  <label for="description">
    <g:message code="tdbSuite.description.label" default="Description" />
  </label>
  <g:textArea name="description" cols="40" rows="5" maxlength="400" value="${tdbSuiteObj?.description}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: tdbDrillObj, field: 'chainedSuite', 'error')} required">
  <label for="chainedSuite">
    <g:message code="tdbDrill.chained.suite.label" default="Chained Suite" />
  </label>
  <g:select id="chainedSuite" name="chainedSuite.id"
	    from="${org.motrice.tdocbox.TdbSuite.list()}"
	    noSelection="${['null':'-Not Chained-']}"
	    value="${tdbSuiteObj?.chainedSuite?.id}"
	    optionKey="id" class="many-to-one"/>
</div>
<div class="fieldcontain ${hasErrors(bean: tdbSuiteObj, field: 'drills', 'error')} ">
  <label for="drills">
    <g:message code="tdbSuite.drills.label" default="Drills" />
  </label>
  <ul class="one-to-many">
    <g:each in="${tdbSuiteObj?.drills?}" var="d">
      <li><g:link controller="tdbDrill" action="show" id="${d.id}">${d?.encodeAsHTML()}</g:link></li>
    </g:each>
    <li class="add">
      <g:link controller="tdbDrill" action="create" params="['tdbSuite.id': tdbSuiteObj?.id]">${message(code: 'default.add.label', args: [message(code: 'tdbDrill.label', default: 'TdbDrill')])}</g:link>
    </li>
  </ul>
</div>
