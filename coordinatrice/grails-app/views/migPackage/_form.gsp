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
<%@ page import="org.motrice.migratrice.MigPackage" %>

<div class="fieldcontain ${hasErrors(bean: migPackageInst, field: 'siteName', 'error')} ">
  <label for="siteName">
    <g:message code="migPackage.siteName.label" default="Site Name"/>
  </label>
  <g:textField name="siteName" maxlength="120" value="${migPackageInst?.siteName}" readonly="readonly"/>
</div>

<div class="fieldcontain ${hasErrors(bean: migPackageInst, field: 'packageName', 'error')} ">
  <label for="packageName">
    <g:message code="migPackage.packageName.label" default="Package Name" />
    <span class="required-indicator">*</span>
  </label>
  <g:textField name="packageName" maxlength="120" value="${migPackageInst?.packageName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: migPackageInst, field: 'originLocal', 'error')} ">
  <label for="originLocal">
    <g:message code="migPackage.originLocal.label" default="Origin Local" />
  </label>
  <g:checkBox name="originLocal" value="${migPackageInst?.originLocal}" readonly="readonly"/>
</div>

<div class="fieldcontain ${hasErrors(bean: migPackageInst, field: 'packageFormat', 'error')} ">
  <label for="packageFormat">
    <g:message code="migPackage.packageFormat.label" default="Package Format" />
  </label>
  <g:textField name="packageFormat" value="${migPackageInst?.packageFormat}" readonly="readonly"/>
</div>

<div class="fieldcontain ${hasErrors(bean: migPackageInst, field: 'siteTstamp', 'error')} required">
  <label for="siteTstamp">
    <g:message code="migPackage.siteTstamp.label" default="Site Tstamp" />
  </label>
  <g:tstamp date="${migPackageInst?.siteTstamp}"/>
</div>
