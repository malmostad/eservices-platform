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
<%@ page import="org.motrice.coordinatrice.Procdef" %>
<div class="fieldcontain ${hasErrors(bean: procdefInst, field: 'name', 'error')} ">
  <label for="name">
    <g:message code="procdef.name.label" default="Procdef Key" />
  </label>
  <g:textField name="name" size="32" maxlength="255" value="${procdefInst?.nameOrKey}" readonly="true"/>
</div>
<div class="fieldcontain ${hasErrors(bean: procdefInst, field: 'vno', 'error')} required">
  <label for="vno">
    <g:message code="procdef.vno.label" default="Procdef Ver" />
  </label>
  <g:field name="vno" type="number" min="0" value="${procdefInst.vno}" readonly="true"/>
</div>
<div class="fieldcontain ${hasErrors(bean: procdefInst, field: 'state', 'error')} ">
  <label for="state">
    <g:message code="procdef.state.label" default="State" />
  </label>
  <g:pdefstate state="${procdefInst?.state}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: procdefInst, field: 'state', 'error')} ">
  <label for="state">
    <g:message code="procdef.state.new.label" default="State" />
  </label>
  <g:select id="formsel" name="state.id" from="${stateList}" optionKey="id"/>
</div>
