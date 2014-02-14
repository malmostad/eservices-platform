<%@ page import="org.motrice.migratrice.MigFormdef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'migFormdef.label', default: 'MigFormdef')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-migFormdef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="list-migFormdef" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <g:form method="post" >
	<table>
	  <thead>
	    <tr>
	      <th>Export</th>
	      <g:sortableColumn property="app" title="${message(code: 'migFormdef.app.label', default: 'App')}" />
	      <g:sortableColumn property="form" title="${message(code: 'migFormdef.form.label', default: 'Form')}" />
	      <g:sortableColumn property="currentDraft" title="${message(code: 'migFormdef.currentDraft.label', default: 'Current Draft')}" />
	      <g:sortableColumn property="created" title="${message(code: 'migFormdef.created.label', default: 'Created')}" />
	      <g:sortableColumn property="uuid" title="${message(code: 'migFormdef.uuid.label', default: 'Uuid')}" />
	    </tr>
	  </thead>
	  <tbody>
	    <g:each in="${migFormdefInstList}" status="i" var="migFormdefInst">
	      <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
		<td><g:checkBox name="chb_${migFormdefInst?.path}" value="${false}"/></td>
		<td>${fieldValue(bean: migFormdefInst, field: "app")}</td>
		<td>${fieldValue(bean: migFormdefInst, field: "form")}</td>
		<td>${fieldValue(bean: migFormdefInst, field: "currentDraft")}</td>
		<td><g:tstamp date="${migFormdefInst.created}" /></td>
		<td><g:abbr text="${migFormdefInst?.uuid}"/></td>
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
