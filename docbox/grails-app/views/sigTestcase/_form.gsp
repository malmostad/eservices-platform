<%@ page import="org.motrice.signatrice.SigTestcase" %>
<div class="fieldcontain ${hasErrors(bean: sigTestcaseObj, field: 'name', 'error')} ">
  <label for="name">
    <g:message code="sigTestcase.name.label" default="Name" />
  </label>
  <g:textField name="name" maxlength="80" value="${sigTestcaseObj?.name}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: sigTestcaseObj, field: 'personalIdNo', 'error')} ">
  <label for="personalIdNo">
    <g:message code="sigTestcase.personalIdNo.label" default="Personal Id No" />
  </label>
  <g:textField name="personalIdNo" maxlength="24" value="${sigTestcaseObj?.personalIdNo}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: sigTestcaseObj, field: 'userVisibleText', 'error')} ">
  <label for="userVisibleText">
    <g:message code="sigTestcase.userVisibleText.label" default="User Visible Text" />
  </label>
  <g:textArea class="wide" name="userVisibleText" value="${sigTestcaseObj?.userVisibleText}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: sigTestcaseObj, field: 'scheme', 'error')} required">
  <label for="scheme">
    <g:message code="sigTestcase.scheme.label" default="Scheme" />
    <span class="required-indicator">*</span>
  </label>
  <g:select id="scheme" name="scheme.id" from="${org.motrice.signatrice.SigScheme.list()}" optionKey="id" required="" value="${sigTestcaseObj?.scheme?.id}" class="many-to-one"/>
</div>
