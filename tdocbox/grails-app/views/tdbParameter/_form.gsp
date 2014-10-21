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
  <g:textField class="wide" name="value" value="${tdbParameterObj?.value}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: tdbParameterObj, field: 'b64Encode', 'error')} ">
  <label for="b64Encode">
    <g:message code="tdbParameter.b64Encode.label" default="B64 Encode" />
  </label>
  <g:checkBox name="b64Encode" value="${tdbParameterObj?.b64Encode}" />
</div>
<div class="fieldcontain ${hasErrors(bean: tdbParameterObj, field: 'description', 'error')} ">
  <label for="description">
    <g:message code="tdbParameter.description.label" default="Description" />
  </label>
  <g:textArea class="wide" name="description" cols="40" rows="5" maxlength="400" value="${tdbParameterObj?.description}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: tdbParameterObj, field: 'drill', 'error')} required">
  <label for="drill">
    <g:message code="tdbParameter.drill.label" default="Drill" />
    <span class="required-indicator">*</span>
  </label>
  <g:select id="drill" name="drill.id" from="${org.motrice.tdocbox.TdbDrill.list()}" optionKey="id" required="" value="${tdbDrillObj?.id}" class="many-to-one"/>
</div>
