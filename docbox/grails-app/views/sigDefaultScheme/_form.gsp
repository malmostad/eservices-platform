<%@ page import="org.motrice.signatrice.SigDefaultScheme" %>
<div class="fieldcontain ${hasErrors(bean: sigDefaultSchemeObj, field: 'defaultScheme', 'error')} ">
  <label for="defaultScheme">
    <g:message code="sigDefaultScheme.defaultScheme.label" default="Default Scheme" />
  </label>
  <g:select id="defaultScheme" name="defaultScheme.id" from="${org.motrice.signatrice.SigScheme.list()}" optionKey="id" value="${sigDefaultSchemeObj?.defaultScheme?.id}" class="many-to-one" noSelection="['null': '--']"/>
</div>
