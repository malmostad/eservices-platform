<%@ page import="org.motrice.coordinatrice.CrdI18nFormLabel" %>



<div class="fieldcontain ${hasErrors(bean: crdI18nFormLabelInst, field: 'formdefPath', 'error')} ">
	<label for="formdefPath">
		<g:message code="crdI18nFormLabel.formdefPath.label" default="Formdef Path" />
		
	</label>
	<g:textArea name="formdefPath" cols="40" rows="5" maxlength="255" value="${crdI18nFormLabelInst?.formdefPath}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: crdI18nFormLabelInst, field: 'formdefVer', 'error')} required">
	<label for="formdefVer">
		<g:message code="crdI18nFormLabel.formdefVer.label" default="Formdef Ver" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="formdefVer" type="number" min="0" value="${crdI18nFormLabelInst.formdefVer}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: crdI18nFormLabelInst, field: 'label', 'error')} ">
	<label for="label">
		<g:message code="crdI18nFormLabel.label.label" default="Label" />
		
	</label>
	<g:textArea name="label" cols="40" rows="5" maxlength="255" value="${crdI18nFormLabelInst?.label}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: crdI18nFormLabelInst, field: 'locale', 'error')} ">
	<label for="locale">
		<g:message code="crdI18nFormLabel.locale.label" default="Locale" />
		
	</label>
	<g:textField name="locale" value="${crdI18nFormLabelInst?.locale}"/>
</div>

