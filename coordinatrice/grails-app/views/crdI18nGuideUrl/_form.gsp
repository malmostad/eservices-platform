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
<%@ page import="org.motrice.coordinatrice.CrdI18nGuideUrl" %>
<div class="fieldcontain ${hasErrors(bean: crdI18nGuideUrlInst, field: 'procdefKey', 'error')} ">
  <label for="procdefKey">
    <g:message code="crdI18nGuideUrl.procdefKey.label" default="Procdef Key" />
  </label>
  <g:textField name="procdefKey" size="32" maxlength="255" value="${crdI18nGuideUrlInst?.procdefKey}" readonly="true"/>
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
  <g:textField name="pattern" size="48" maxlength="400" value="${crdI18nGuideUrlInst?.pattern}"/>
</div>
