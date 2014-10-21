<%@ page import="org.motrice.tdocbox.TdbSuite" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'tdbSuite.label', default: 'TdbSuite')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-tdbSuite" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
	<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
	<g:set var="tdbSuiteId" value="${tdbSuiteObj?.id}"/>
	<li><g:link class="list" controller="tdbCase" action="list" params="[tdbSuiteId:tdbSuiteId]"><g:message code="tdbCase.list.label"/></g:link></li>
      </ul>
    </div>
    <div id="show-tdbSuite" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list tdbSuite">
	<g:if test="${tdbSuiteObj?.name}">
	  <li class="fieldcontain">
	    <span id="name-label" class="property-label"><g:message code="tdbSuite.name.label" default="Name" /></span>
	    <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${tdbSuiteObj}" field="name"/></span>
	  </li>
	</g:if>
	<g:if test="${tdbSuiteObj?.description}">
	  <li class="fieldcontain">
	    <span id="description-label" class="property-label"><g:message code="tdbSuite.description.label" default="Description" /></span>
	    <span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${tdbSuiteObj}" field="description"/></span>
	  </li>
	</g:if>
	<g:if test="${tdbSuiteObj?.chainedSuite}">
	  <li class="fieldcontain">
	    <span id="suite-label" class="property-label"><g:message code="tdbDrill.chained.suite.label" default="Chained Suite" /></span>
	    <span class="property-value" aria-labelledby="suite-label">
	      <g:link controller="tdbSuite" action="show" id="${tdbSuiteObj?.chainedSuite?.id}">${tdbSuiteObj?.chainedSuite?.encodeAsHTML()}
	    </g:link></span>
	  </li>
	</g:if>
	<g:if test="${tdbSuiteObj?.drills}">
	  <li class="fieldcontain">
	    <span id="drills-label" class="property-label"><g:message code="tdbSuite.drills.label" default="Drills" /></span>
	    <g:each in="${tdbSuiteObj.drills}" var="d">
	      <span class="property-value" aria-labelledby="drills-label">
		<g:link controller="tdbDrill" action="show" id="${d.id}">${d?.encodeAsHTML()}</g:link> (${d?.method?.encodeAsHTML()})
	      </span>
	    </g:each>
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${tdbSuiteObj?.id}" />
	  <g:link class="edit" action="edit" id="${tdbSuiteObj?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
	  <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
	  <g:link class="run" action="run" id="${tdbSuiteObj?.id}"><g:message code="default.button.run.label" default="Run" /></g:link>
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
