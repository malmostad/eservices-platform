<%@ page import="org.motrice.coordinatrice.ProcDef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'procDef.label', default: 'ProcDef')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-procDef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
      </ul>
    </div>
    <div id="show-procDef" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list procDef">
	<g:if test="${procDefInst?.name}">
	  <li class="fieldcontain">
	    <span id="name-label" class="property-label"><g:message code="procDef.name.label" default="Name" /></span>
	    <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${procDefInst}" field="name"/>
	      (<g:fieldValue bean="${procDefInst}" field="uuid"/>)
	    </span>
	  </li>
	</g:if>
	<g:if test="${procDefInst?.vno}">
	  <li class="fieldcontain">
	    <span id="vno-label" class="property-label"><g:message code="procDef.vno.label" default="Vno" /></span>
	    <span class="property-value" aria-labelledby="vno-label"><g:fieldValue bean="${procDefInst}" field="vno"/></span>
	  </li>
	</g:if>
	<g:if test="${procDefInst?.type}">
	  <li class="fieldcontain">
	    <span id="type-label" class="property-label"><g:message code="procDef.type.label" default="Type" /></span>
	    <span class="property-value" aria-labelledby="type-label"><g:fieldValue bean="${procDefInst}" field="type"/></span>
	  </li>
	</g:if>
	<g:if test="${procDefInst?.state}">
	  <li class="fieldcontain">
	    <span id="state-label" class="property-label"><g:message code="procDef.state.label" default="State" /></span>
	    <span class="property-value" aria-labelledby="state-label"><g:pdefstate state="${procDefInst?.state}"/></span>
	  </li>
	</g:if>
	<g:if test="${procDefInst?.deployment}">
	  <li class="fieldcontain">
	    <span id="deployedTime-label" class="property-label"><g:message code="procDef.deployedTime.label" default="Deployed" /></span>
	    <span class="property-value" aria-labelledby="deployedTime-label"><g:fieldValue bean="${procDefInst}" field="deployedTimeStr"/></span>
	  </li>
	</g:if>

	<li class="fieldcontain">
	  <span class="property-label"><g:message code="startform.selection.label"/></span>
	  <g:if test="${procDefInst?.startForms}">
	    <g:each in="${procDefInst.startForms}" var="msfd">
	      <span class="property-value">
		<g:set var="pfv" value="${msfd.formdef}"/>
		<g:if test="${pfv}">
		  <g:link controller="pxdFormdefVer" action="show" id="${pfv?.id}">${msfd?.encodeAsHTML()}</g:link>
		</g:if>
		<g:else>
		  <g:set var="linktitle"><g:message code="startform.selection.invalid.link"/></g:set>
		  <g:img uri="/images/silk/exclamation.png" title="${linktitle}"/> ${msfd?.encodeAsHTML()}
		</g:else>
		<g:link class="edit" action="edit" id="${procDefInst?.uuid}">
		  <g:img uri="/images/silk/pencil_go.png" title="${message(code: 'default.button.edit.label', default: 'Edit')}"/>
		</g:link>
	      </span>
	    </g:each>
	  </g:if>
	  <g:else>
	    <g:set var="statetitle"><g:message code="startform.selection.unselected"/></g:set>
	    <span class="property-value"><g:img uri="/images/silk/exclamation.png" title="${statetitle}"/>
	      <g:link class="edit" action="edit" id="${procDefInst?.uuid}">
		<g:img uri="/images/silk/pencil_go.png" title="${message(code: 'default.button.edit.label', default: 'Edit')}"/>
	      </g:link>
	    </span>
	  </g:else>
	</li>

	<g:if test="${procDefInst?.activities}">
	  <g:set var="procId" value="${procDefInst?.uuid}"/>
	  <li class="fieldcontain">
	    <table>
	    <span id="activities-label" class="property-label"><g:message code="procDef.act.cnx.label" default="Activities" /></span>
	    <g:each in="${procDefInst.activities}" var="a">
	      <g:set var="formdef" value="${a?.activityFormdef}"/>
	      <g:set var="actId" value="${a?.uuid}"/>
	      <g:set var="cnx" value="${new org.motrice.coordinatrice.ActivityConnection(a, formdef?.formPath)}"/>
	      <span class="property-value" aria-labelledby="activities-label"><tr>
		  <td><g:link controller="actDef" action="show" id="${a?.fullId}">${a?.encodeAsHTML()}</g:link></td>
		  <g:render template="/activityConnection" model="[connection:cnx, actDefInst:a]"/>
	      </tr></span>
	    </g:each>
	    </table>
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="id" value="${procDefInst?.id}" />
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
