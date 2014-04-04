<%@ page import="org.motrice.coordinatrice.Procdef" %>
<div class="fieldcontain ${hasErrors(bean: procdefInst, field: 'name', 'error')} ">
  <label for="name">
    <g:message code="procdef.name.label" default="Procdef Key" />
  </label>
  <g:textField name="name" size="32" maxlength="255" value="${procdefInst?.nameOrKey}" readonly="true"/>
</div>
<div class="fieldcontain ${hasErrors(bean: procdefInst, field: 'vno', 'error')} required">
  <label for="vno">
    <g:message code="procdef.vno.label" default="Procdef Ver" />
  </label>
  <g:field name="vno" type="number" min="0" value="${procdefInst.vno}" readonly="true"/>
</div>
<div class="fieldcontain ${hasErrors(bean: procdefInst, field: 'state', 'error')} ">
  <label for="state">
    <g:message code="procdef.state.label" default="State" />
  </label>
  <g:pdefstate state="${procdefInst?.state}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: procdefInst, field: 'state', 'error')} ">
  <label for="state">
    <g:message code="procdef.state.new.label" default="State" />
  </label>
  <g:select id="formsel" name="state.id" from="${stateList}" optionKey="id"/>
</div>
