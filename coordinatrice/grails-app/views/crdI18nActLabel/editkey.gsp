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
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'crdI18nActLabel.label', default: 'CrdI18nActLabel')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-crdI18nActLabel" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="list-crdI18nActLabel" class="content scaffold-list" role="main">
      <h1><g:message code="crdI18nActLabel.list.header" args="[procdefKey]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <g:form method="post" >
	<g:hiddenField name="idList" value="${idList}"/>
	<g:hiddenField name="procdefKey" value="${procdefKey}"/>
	<table>
	  <thead>
	    <tr>
	      <g:sortableColumn property="procdefVer" title="${message(code: 'crdI18nActLabel.procdefVer.label', default: 'Procdef Ver')}" />
	      <g:sortableColumn property="actdefName" title="${message(code: 'crdI18nActLabel.actdefName.label', default: 'Actdef Name')}" />
	      <g:sortableColumn property="actdefId" title="${message(code: 'crdI18nActLabel.actdefId.label', default: 'Actdef Id')}" />
	      <g:sortableColumn property="locale" title="${message(code: 'crdI18nActLabel.locale.label', default: 'Locale')}" />
	      <g:sortableColumn property="label" title="${message(code: 'crdI18nActLabel.label.label', default: 'Label')}" />
	    </tr>
	  </thead>
	  <tbody>
	    <g:each in="${actLabelList}" status="i" var="actLabel">
	      <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
		<g:render template="multiedit" bean="${actLabel}"/>
	      </tr>
	    </g:each>
	  </tbody>
	</table>
	<fieldset class="buttons">
	  <g:actionSubmit class="save" action="updatekey" value="${message(code: 'default.button.update.label', default: 'Update')}" />
	  <g:actionSubmit class="delete" action="deletekey" value="${message(code: 'crdI18nActLabel.delete.all.label', default: 'Delete')}"
			  onclick="return confirm('${message(code: 'crdI18nActLabel.delete.all.confirm', default: 'Are you sure?')}');"/>
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
