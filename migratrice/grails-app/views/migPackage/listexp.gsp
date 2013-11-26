<%@ page import="org.motrice.migratrice.MigFormdef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'migFormdef.label', default: 'MigFormdef')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
    <r:require modules="jquery"/>
  </head>
  <body>
    <r:script disposition="defer">
      function showOff(msg) {
      window.alert('Deep voice: ' + msg);
      }
      function exportCheckAll(flag) {
      $('input[type="checkbox"]').attr('checked', flag);
      }
      function exportAddContaining(str, flag) {
      $('input[type="checkbox"]').filter('input[name*='+str+']').attr('checked', flag);
      }
    </r:script>
    <a href="#list-migFormdef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="list-migFormdef" class="content scaffold-list" role="main">
      <g:form method="post" action="saveexp">
	<div class="fieldcontain ${hasErrors(bean: migPackageInst, field: 'packageName', 'error')} ">
	  <label for="packageName">
	    <g:message code="migPackage.packageName.label" default="Package Name" />
	    <span class="required-indicator">*</span>
	  </label>
	  <g:textField name="packageName" maxlength="120" value="${migPackageInst?.packageName}"/>
	</div>
	<g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
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
		<td><g:checkBox name="${migFormdefInst?.path}" value="${false}"/>
		  <g:hiddenField name="ref-${migFormdefInst?.path}" value="${migFormdefInst?.ref}"/></td>
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
	  <g:actionSubmit class="save" action="saveexp" value="${message(code: 'default.button.update.label', default: 'Update')}"/>
	  <span class="checkall" onclick="exportCheckAll(true)">Check all</span>
	  <span class="clearall" onclick="exportCheckAll(false)">Clear all</span>
	  <span class="add" onclick="exportAddContaining($('#textForAddContaining').val(),true)">Add all containing:</span>
	  <g:textField id="textForAddContaining" name="ignoredOnInput" size="7" maxlength="20"/>
	  <span class="remove" onclick="exportAddContaining($('#textForAddContaining').val(),false)">Clear all containing</span>
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
