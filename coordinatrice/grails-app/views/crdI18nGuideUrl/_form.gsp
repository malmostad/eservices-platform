<%@ page import="org.motrice.coordinatrice.CrdI18nGuideUrl" %>
<div class="fieldcontain ${hasErrors(bean: crdI18nGuideUrlInst, field: 'procdefKey', 'error')} ">
  <label for="procdefKey">
    <g:message code="crdI18nGuideUrl.procdefKey.label" default="Procdef Key" />
  </label>
  <g:textArea name="procdefKey" cols="40" rows="5" maxlength="255" value="${crdI18nGuideUrlInst?.procdefKey}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: crdI18nGuideUrlInst, field: 'procdefVer', 'error')} required">
  <label for="procdefVer">
    <g:message code="crdI18nGuideUrl.procdefVer.label" default="Procdef Ver" />
    <span class="required-indicator">*</span>
  </label>
  <g:field name="procdefVer" type="number" min="0" value="${crdI18nGuideUrlInst.procdefVer}" required=""/>
</div>
<div class="fieldcontain ${hasErrors(bean: crdI18nGuideUrlInst, field: 'pattern', 'error')} ">
  <label for="pattern">
    <g:message code="crdI18nGuideUrl.pattern.label" default="Pattern" />
  </label>
  <g:textArea name="pattern" cols="40" rows="5" maxlength="400" value="${crdI18nGuideUrlInst?.pattern}"/>
</div>
