<%@ page import="org.motrice.tdocbox.TdbCase" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'tdbCase.label', default: 'TdbCase')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-tdbCase" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
	<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="show-tdbCase" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list tdbCase">
	<g:if test="${tdbCaseObj?.timeStamp}">
	  <li class="fieldcontain">
	    <span id="timeStamp-label" class="property-label"><g:message code="tdbCase.timeStamp.label" default="Time Stamp" /></span>
	    <span class="property-value" aria-labelledby="timeStamp-label"><g:formatDate date="${tdbCaseObj?.timeStamp}" /></span>
	  </li>
	</g:if>
	<g:if test="${tdbCaseObj?.exception}">
	  <li class="fieldcontain">
	    <span id="exception-label" class="property-label"><g:message code="tdbCase.exception.label" default="Exception" /></span>
	    <span class="property-value" aria-labelledby="exception-label"><g:fieldValue bean="${tdbCaseObj}" field="exception"/></span>
	  </li>
	</g:if>
	<g:if test="${tdbCaseObj?.suite}">
	  <li class="fieldcontain">
	    <span id="suite-label" class="property-label"><g:message code="tdbCase.suite.label" default="Suite" /></span>
	    <span class="property-value" aria-labelledby="suite-label"><g:link controller="tdbSuite" action="show" id="${tdbCaseObj?.suite?.id}">${tdbCaseObj?.suite?.encodeAsHTML()}</g:link></span>
	  </li>
	</g:if>
	<g:if test="${tdbCaseObj?.items}">
	  <li class="fieldcontain">
	    <span id="items-label" class="property-label"><g:message code="tdbCase.items.label" default="Items" /></span>
	    <g:each in="${tdbCaseObj.items}" var="i">
	      <span class="property-value" aria-labelledby="items-label"><g:link controller="tdbItem" action="show" id="${i.id}">${i?.encodeAsHTML()}</g:link></span>
	    </g:each>
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${tdbCaseObj?.id}" />
	  <g:link class="edit" action="edit" id="${tdbCaseObj?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
	  <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
