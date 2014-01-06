<%@ page import="org.motrice.orifice.OriFormdef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'oriFormdef.label', default: 'OriFormdef')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-oriFormdef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="list-oriFormdef" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <g:form method="post" >
	<table>
	  <thead>
	    <tr>
	      <th>Export</th>
	      <g:sortableColumn property="app" title="${message(code: 'oriFormdef.app.label', default: 'App')}" />
	      <g:sortableColumn property="form" title="${message(code: 'oriFormdef.form.label', default: 'Form')}" />
	      <g:sortableColumn property="currentDraft" title="${message(code: 'oriFormdef.currentDraft.label', default: 'Current Draft')}" />
	      <g:sortableColumn property="created" title="${message(code: 'oriFormdef.created.label', default: 'Created')}" />
	      <g:sortableColumn property="uuid" title="${message(code: 'oriFormdef.uuid.label', default: 'Uuid')}" />
	    </tr>
	  </thead>
	  <tbody>
	    <g:each in="${oriFormdefInstList}" status="i" var="oriFormdefInst">
	      <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
		<td><g:checkBox name="chb_${oriFormdefInst?.path}" value="${false}"/></td>
		<td>${fieldValue(bean: oriFormdefInst, field: "app")}</td>
		<td>${fieldValue(bean: oriFormdefInst, field: "form")}</td>
		<td>${fieldValue(bean: oriFormdefInst, field: "currentDraft")}</td>
		<td><g:tstamp date="${oriFormdefInst.created}" /></td>
		<td><g:abbr text="${oriFormdefInst?.uuid}"/></td>
	      </tr>
	    </g:each>
	  </tbody>
	</table>
	<fieldset class="buttons">
	  <g:actionSubmit class="save" action="export" value="${message(code: 'default.button.update.label', default: 'Update')}"/>
	  <span class="checkall">Check all</span>
	  <span class="clearall">Clear all</span>
	  <span class="add">Add containing</span>
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
