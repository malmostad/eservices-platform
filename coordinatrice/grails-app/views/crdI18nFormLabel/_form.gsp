<%-- == Motrice Copyright Notice ==

  Motrice Service Platform

  Copyright (C) 2011-2014 Motrice AB

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU Affero General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU Affero General Public License for more details.

  You should have received a copy of the GNU Affero General Public License
  along with this program. If not, see <http://www.gnu.org/licenses/>.

  e-mail: info _at_ motrice.se
  mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN
  phone: +46 8 641 64 14

--%>
<%@ page import="org.motrice.coordinatrice.CrdI18nFormLabel" %>
<g:set var="formdefPath" value="${formLabelInst?.formdef?.path}"/>
<div class="fieldcontain ${hasErrors(bean: formLabelInst, field: 'formdefId', 'error')} ">
  <label for="formdefId">
    <g:message code="crdI18nFormLabel.formdefPath.label" default="Formdef Path" />
  </label>
  <g:textField name="formdefPath" cols="40" rows="5" maxlength="255" value="${formdefPath?.encodeAsHTML()}" readonly="true"/>
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
