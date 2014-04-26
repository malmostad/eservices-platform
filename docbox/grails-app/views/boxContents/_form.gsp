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
<%@ page import="org.motrice.docbox.doc.BoxContents" %>



<div class="fieldcontain ${hasErrors(bean: boxContentsObj, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="boxContents.name.label" default="Name" />
		
	</label>
	<g:textField name="name" maxlength="80" value="${boxContentsObj?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: boxContentsObj, field: 'format', 'error')} ">
	<label for="format">
		<g:message code="boxContents.format.label" default="Format" />
		
	</label>
	<g:textField name="format" maxlength="80" value="${boxContentsObj?.format}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: boxContentsObj, field: 'size', 'error')} required">
	<label for="size">
		<g:message code="boxContents.size.label" default="Size" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="size" from="${0..2147483646}" class="range" required="" value="${fieldValue(bean: boxContentsObj, field: 'size')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: boxContentsObj, field: 'text', 'error')} ">
	<label for="text">
		<g:message code="boxContents.text.label" default="Text" />
		
	</label>
	<g:textField name="text" value="${boxContentsObj?.text}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: boxContentsObj, field: 'stream', 'error')} ">
	<label for="stream">
		<g:message code="boxContents.stream.label" default="Stream" />
		
	</label>
	<input type="file" id="stream" name="stream" />
</div>

<div class="fieldcontain ${hasErrors(bean: boxContentsObj, field: 'step', 'error')} required">
	<label for="step">
		<g:message code="boxContents.step.label" default="Step" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="step" name="step.id" from="${org.motrice.docbox.doc.BoxDocStep.list()}" optionKey="id" required="" value="${boxContentsObj?.step?.id}" class="many-to-one"/>
</div>

