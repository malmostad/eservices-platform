<%@ page import="org.motrice.coordinatrice.CrdI18nActLabel" %>
<div class="fieldcontain ${hasErrors(bean: actLabelInst, field: 'procdefKey', 'error')} ">
  <label for="procdefKey">
    <g:message code="crdI18nActLabel.procdefKey.label" default="Procdef Key" />
  </label>
  <g:textArea name="procdefKey" cols="40" rows="5" maxlength="255" value="${actLabelInst?.procdefKey}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: actLabelInst, field: 'procdefVer', 'error')} required">
  <label for="procdefVer">
    <g:message code="crdI18nActLabel.procdefVer.label" default="Procdef Ver" />
    <span class="required-indicator">*</span>
  </label>
  <g:field name="procdefVer" type="number" min="0" value="${actLabelInst.procdefVer}" required=""/>
</div>
<div class="fieldcontain ${hasErrors(bean: actLabelInst, field: 'actdefName', 'error')} ">
  <label for="actdefName">
    <g:message code="crdI18nActLabel.actdefName.label" default="Actdef Name" />
  </label>
  <g:textArea name="actdefName" cols="40" rows="5" maxlength="255" value="${actLabelInst?.actdefName}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: actLabelInst, field: 'actdefId', 'error')} ">
  <label for="actdefId">
    <g:message code="crdI18nActLabel.actdefId.label" default="Actdef Id" />
  </label>
  <g:textArea name="actdefId" cols="40" rows="5" maxlength="255" value="${actLabelInst?.actdefId}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: actLabelInst, field: 'locale', 'error')} ">
  <label for="locale">
    <g:message code="crdI18nActLabel.locale.label" default="Locale" />
  </label>
  <g:textField name="locale" maxlength="24" value="${actLabelInst?.locale}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: actLabelInst, field: 'label', 'error')} ">
  <label for="label">
    <g:message code="crdI18nActLabel.label.label" default="Label" />
  </label>
  <g:textArea name="label" cols="40" rows="5" maxlength="255" value="${actLabelInst?.label}"/>
</div>

