<%@ page import="org.motrice.coordinatrice.CrdI18nFormLabel" %>
<g:set var="formdefPath" value="${formdefInst?.path}"/>
<div class="fieldcontain ${hasErrors(bean: formLabelInst, field: 'formdefId', 'error')} ">
  <label for="formdefId">
    <g:message code="crdI18nFormLabel.formdefPath.label" default="Formdef Path" />
  </label>
  <g:textField name="formdefPath" cols="40" rows="5" maxlength="255" value="${formdefPath}" readonly="true"/>
</div>
<div class="fieldcontain ${hasErrors(bean: formLabelInst, field: 'locale', 'error')} ">
  <label for="locale">
    <g:message code="crdI18nFormLabel.locale.label" default="Locale" />
  </label>
  <g:textField name="locale" value="${formLabelInst?.locale}" readonly="true"/>
</div>
<div class="fieldcontain ${hasErrors(bean: formLabelInst, field: 'formdefVer', 'error')} required">
  <label for="formdefVer">
    <g:message code="crdI18nFormLabel.formdefVer.label" default="Formdef Ver" />
    <span class="required-indicator">*</span>
  </label>
  <g:field name="formdefVer" type="number" min="0" value="${formLabelInst.formdefVer}" required=""/>
</div>
<div class="fieldcontain ${hasErrors(bean: formLabelInst, field: 'label', 'error')} ">
  <label for="label">
    <g:message code="crdI18nFormLabel.label.label" default="Label" />
  </label>
  <g:textField name="label" size="48" maxlength="255" value="${formLabelInst?.label}"/>
</div>
