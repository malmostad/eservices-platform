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

