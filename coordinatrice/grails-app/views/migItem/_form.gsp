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
<%@ page import="org.motrice.migratrice.MigItem" %>

<div class="fieldcontain ${hasErrors(bean: migItemInst, field: 'formref', 'error')} ">
  <label for="formref">
    <g:message code="migItem.formref.label" default="Formref" />
  </label>
  <g:field name="formref" type="number" value="${migItemInst.formref}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: migItemInst, field: 'path', 'error')} ">
  <label for="path">
    <g:message code="migItem.path.label" default="Path" />
  </label>
  <g:textArea name="path" cols="40" rows="5" maxlength="400" value="${migItemInst?.path}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: migItemInst, field: 'uuid', 'error')} ">
  <label for="uuid">
    <g:message code="migItem.uuid.label" default="Uuid" />
  </label>
  <g:textField name="uuid" maxlength="200" value="${migItemInst?.uuid}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: migItemInst, field: 'formDef', 'error')} ">
  <label for="formDef">
    <g:message code="migItem.formDef.label" default="Form Def" />
  </label>
  <g:textArea name="formDef" cols="40" rows="5" maxlength="400" value="${migItemInst?.formDef}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: migItemInst, field: 'format', 'error')} ">
  <label for="format">
    <g:message code="migItem.format.label" default="Format" />
  </label>
  <g:textField name="format" maxlength="80" value="${migItemInst?.format}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: migItemInst, field: 'size', 'error')} required">
  <label for="size">
    <g:message code="migItem.size.label" default="Size" />
    <span class="required-indicator">*</span>
  </label>
  <g:select name="size" from="${0..2147483646}" class="range" required="" value="${fieldValue(bean: migItemInst, field: 'size')}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: migItemInst, field: 'text', 'error')} ">
  <label for="text">
    <g:message code="migItem.text.label" default="Text" />
  </label>
  <g:textField name="text" value="${migItemInst?.text}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: migItemInst, field: 'stream', 'error')} ">
  <label for="stream">
    <g:message code="migItem.stream.label" default="Stream" />
  </label>
  <input type="file" id="stream" name="stream" />
</div>
<div class="fieldcontain ${hasErrors(bean: migItemInst, field: 'sha1', 'error')} ">
  <label for="sha1">
    <g:message code="migItem.sha1.label" default="Sha1" />
  </label>
  <g:textArea name="sha1" cols="40" rows="5" maxlength="400" value="${migItemInst?.sha1}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: migItemInst, field: 'created', 'error')} required">
  <label for="created">
    <g:message code="migItem.created.label" default="Created" />
    <span class="required-indicator">*</span>
  </label>
  <g:datePicker name="created" precision="day"  value="${migItemInst?.created}"  />
</div>
<div class="fieldcontain ${hasErrors(bean: migItemInst, field: 'formdef', 'error')} required">
  <label for="formdef">
    <g:message code="migItem.formdef.label" default="Formdef" />
    <span class="required-indicator">*</span>
  </label>
  <g:select id="formdef" name="formdef.id" from="${org.motrice.migratrice.MigFormdef.list()}" optionKey="id" required="" value="${migItemInst?.formdef?.id}" class="many-to-one"/>
</div>
<div class="fieldcontain ${hasErrors(bean: migItemInst, field: 'pack', 'error')} required">
  <label for="pack">
    <g:message code="migItem.pack.label" default="Pack" />
    <span class="required-indicator">*</span>
  </label>
  <g:select id="pack" name="pack.id" from="${org.motrice.migratrice.MigPackage.list()}" optionKey="id" required="" value="${migItemInst?.pack?.id}" class="many-to-one"/>
</div>
<div class="fieldcontain ${hasErrors(bean: migItemInst, field: 'ref', 'error')} required">
  <label for="ref">
    <g:message code="migItem.ref.label" default="Ref" />
    <span class="required-indicator">*</span>
  </label>
  <g:field name="ref" type="number" value="${migItemInst.ref}" required=""/>
</div>
