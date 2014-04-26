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
<%@ page import="org.motrice.docbox.doc.BoxDoc" %>



<div class="fieldcontain ${hasErrors(bean: boxDocObj, field: 'docNo', 'error')} ">
	<label for="docNo">
		<g:message code="boxDoc.docNo.label" default="Doc No" />
		
	</label>
	<g:textField name="docNo" maxlength="16" value="${boxDocObj?.docNo}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: boxDocObj, field: 'formDataUuid', 'error')} ">
	<label for="formDataUuid">
		<g:message code="boxDoc.formDataUuid.label" default="Form Data Uuid" />
		
	</label>
	<g:textField name="formDataUuid" maxlength="200" value="${boxDocObj?.formDataUuid}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: boxDocObj, field: 'steps', 'error')} ">
	<label for="steps">
		<g:message code="boxDoc.steps.label" default="Steps" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${boxDocObj?.steps?}" var="s">
    <li><g:link controller="boxDocStep" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="boxDocStep" action="create" params="['boxDoc.id': boxDocObj?.id]">${message(code: 'default.add.label', args: [message(code: 'boxDocStep.label', default: 'BoxDocStep')])}</g:link>
</li>
</ul>

</div>

