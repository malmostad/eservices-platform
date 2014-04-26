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
<%@ page import="org.motrice.coordinatrice.CrdProcCategory" %>
<div class="fieldcontain ${hasErrors(bean: crdProcCategoryInst, field: 'name', 'error')} required">
  <label for="name">
    <g:message code="crdProcCategory.name.label" default="Name" />
    <span class="required-indicator">*</span>
  </label>
  <g:textField name="name" maxlength="200" required="" value="${crdProcCategoryInst?.name}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: crdProcCategoryInst, field: 'description', 'error')} ">
  <label for="description">
    <g:message code="crdProcCategory.description.label" default="Description" />
    
  </label>
  <g:textField name="description" value="${crdProcCategoryInst?.description}"/>
</div>
