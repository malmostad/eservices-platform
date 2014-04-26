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
<%@ page import="org.motrice.postxdb.PxdFormdefVer" %>



<div class="fieldcontain ${hasErrors(bean: pxdFormdefVerObj, field: 'path', 'error')} ">
	<label for="path">
		<g:message code="pxdFormdefVer.path.label" default="Path" />
		
	</label>
	<g:textArea name="path" cols="40" rows="5" maxlength="400" value="${pxdFormdefVerObj?.path}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefVerObj, field: 'appName', 'error')} ">
	<label for="appName">
		<g:message code="pxdFormdefVer.appName.label" default="App Name" />
		
	</label>
	<g:textField name="appName" maxlength="120" value="${pxdFormdefVerObj?.appName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefVerObj, field: 'formName', 'error')} ">
	<label for="formName">
		<g:message code="pxdFormdefVer.formName.label" default="Form Name" />
		
	</label>
	<g:textField name="formName" maxlength="120" value="${pxdFormdefVerObj?.formName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefVerObj, field: 'fvno', 'error')} required">
	<label for="fvno">
		<g:message code="pxdFormdefVer.fvno.label" default="Fvno" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="fvno" from="${1..9999}" class="range" required="" value="${fieldValue(bean: pxdFormdefVerObj, field: 'fvno')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefVerObj, field: 'draft', 'error')} required">
	<label for="draft">
		<g:message code="pxdFormdefVer.draft.label" default="Draft" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="draft" from="${1..10000}" class="range" required="" value="${fieldValue(bean: pxdFormdefVerObj, field: 'draft')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefVerObj, field: 'title', 'error')} ">
	<label for="title">
		<g:message code="pxdFormdefVer.title.label" default="Title" />
		
	</label>
	<g:textField name="title" maxlength="120" value="${pxdFormdefVerObj?.title}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefVerObj, field: 'description', 'error')} ">
	<label for="description">
		<g:message code="pxdFormdefVer.description.label" default="Description" />
		
	</label>
	<g:textArea name="description" cols="40" rows="5" maxlength="800" value="${pxdFormdefVerObj?.description}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefVerObj, field: 'language', 'error')} ">
	<label for="language">
		<g:message code="pxdFormdefVer.language.label" default="Language" />
		
	</label>
	<g:textField name="language" maxlength="16" value="${pxdFormdefVerObj?.language}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefVerObj, field: 'author', 'error')} ">
	<label for="author">
		<g:message code="pxdFormdefVer.author.label" default="Author" />
		
	</label>
	<g:textField name="author" maxlength="80" value="${pxdFormdefVerObj?.author}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefVerObj, field: 'logoRef', 'error')} ">
	<label for="logoRef">
		<g:message code="pxdFormdefVer.logoRef.label" default="Logo Ref" />
		
	</label>
	<g:textArea name="logoRef" cols="40" rows="5" maxlength="800" value="${pxdFormdefVerObj?.logoRef}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pxdFormdefVerObj, field: 'formdef', 'error')} required">
	<label for="formdef">
		<g:message code="pxdFormdefVer.formdef.label" default="Formdef" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="formdef" name="formdef.id" from="${org.motrice.postxdb.PxdFormdef.list()}" optionKey="id" required="" value="${pxdFormdefVerObj?.formdef?.id}" class="many-to-one"/>
</div>

