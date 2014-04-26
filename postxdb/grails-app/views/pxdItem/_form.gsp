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
<%@ page import="org.motrice.postxdb.PxdItem" %>



<div class="fieldcontain ${hasErrors(bean: pxdItemObj, field: 'path', 'error')} ">
	<label for="path">
		<g:message code="pxdItem.path.label" default="Path" />
		
	</label>
	<g:textField name="path" value="${pxdItemObj?.path}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdItemObj, field: 'uuid', 'error')} ">
	<label for="uuid">
		<g:message code="pxdItem.uuid.label" default="Uuid" />
		
	</label>
	<g:textField name="uuid" maxlength="200" value="${pxdItemObj?.uuid}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdItemObj, field: 'formDef', 'error')} ">
	<label for="formDef">
		<g:message code="pxdItem.formDef.label" default="Form Def" />
		
	</label>
	<g:textArea name="formDef" cols="40" rows="5" maxlength="400" value="${pxdItemObj?.formDef}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdItemObj, field: 'format', 'error')} ">
	<label for="format">
		<g:message code="pxdItem.format.label" default="Format" />
		
	</label>
	<g:textField name="format" maxlength="80" value="${pxdItemObj?.format}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdItemObj, field: 'size', 'error')} required">
	<label for="size">
		<g:message code="pxdItem.size.label" default="Size" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="size" from="${0..2147483646}" class="range" required="" value="${fieldValue(bean: pxdItemObj, field: 'size')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdItemObj, field: 'text', 'error')} ">
	<label for="text">
		<g:message code="pxdItem.text.label" default="Text" />
		
	</label>
	<g:textField name="text" value="${pxdItemObj?.text}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdItemObj, field: 'stream', 'error')} ">
	<label for="stream">
		<g:message code="pxdItem.stream.label" default="Stream" />
		
	</label>
	<input type="file" id="stream" name="stream" />
</div>

