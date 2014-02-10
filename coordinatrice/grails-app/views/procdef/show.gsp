<%@ page import="org.motrice.coordinatrice.Procdef" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
      <g:set var="entityName" value="${message(code: 'procdef.label', default: 'Procdef')}" />
      <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#show-procdef" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div class="nav" role="navigation">
      <ul>
	<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
	<div class="buttons"><g:uploadForm action="xmlUpload">
	    <g:hiddenField name="id" value="${procdefInst?.uuid}" />
	    <input class="upload" type="submit" value="${message(code: 'procdef.upload.bpmn.label')}"/><input type="file" name="bpmnDef"/>
	  </g:uploadForm></div>
      </ul>
    </div>
    <div id="show-procdef" class="content scaffold-show" role="main">
      <h1><g:message code="default.show.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <ol class="property-list procdef">
	<g:if test="${procdefInst?.name}">
	  <li class="fieldcontain">
	    <span id="name-label" class="property-label"><g:message code="procdef.name.label" default="Name" /></span>
	    <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${procdefInst}" field="name"/>
	      (<g:fieldValue bean="${procdefInst}" field="uuid"/>)
	    </span>
	  </li>
	</g:if>
	<g:if test="${procdefInst?.vno}">
	  <li class="fieldcontain">
	    <span id="vno-label" class="property-label"><g:message code="procdef.vno.label" default="Vno" /></span>
	    <span class="property-value" aria-labelledby="vno-label"><g:fieldValue bean="${procdefInst}" field="vno"/></span>
	  </li>
	</g:if>
	<g:if test="${procdefInst?.state}">
	  <li class="fieldcontain">
	    <span id="state-label" class="property-label"><g:message code="procdef.state.label" default="State" /></span>
	    <span class="property-value" aria-labelledby="state-label"><g:pdefstate state="${procdefInst?.state}"/></span>
	  </li>
	</g:if>
	<g:if test="${procdefInst?.category}">
	  <li class="fieldcontain">
	    <span id="category-label" class="property-label"><g:message code="procdef.category.label" default="Categ"/></span>
	    <span class="property-value" aria-labelledby="category-label">
	      <g:link controller="crdProcCategory" action="show" id="${procdefInst?.category?.id}"><g:fieldValue bean="${procdefInst}" field="category"/></g:link>
	    </span>
	  </li>
	</g:if>
	<g:if test="${procdefInst?.deployment}">
	  <li class="fieldcontain">
	    <span id="deployedTime-label" class="property-label"><g:message code="procdef.deployedTime.label" default="Deployed" /></span>
	    <span class="property-value" aria-labelledby="deployedTime-label">
	      <g:link action="listdepl" id="${procdefInst?.deployment?.id}">
	      <g:fieldValue bean="${procdefInst}" field="deployedTimeStr"/>
	      </g:link>
	    </span>
	  </li>
	</g:if>
	<li class="fieldcontain">
	  <span id="category-label" class="property-label"><g:message code="procdef.downloads.label" default="Downloads"/></span>
	  <span class="property-value" aria-labelledby="category-label">
	    <g:link action="diagramDownload" id="${procdefInst.uuid}"><g:img uri="/images/silk/camera.png" title="${message(code: 'procdef.diagram.label', default: 'Diagram')}"/></g:link>&nbsp;&nbsp;
	    <g:link action="xmlDownload" id="${procdefInst.uuid}"><g:img uri="/images/silk/map_edit.png" title="${message(code: 'procdef.xml.label', default: 'BPMN')}"/></g:link>
	  </span>
	</li>

	<li class="fieldcontain">
	  <span class="property-label"><g:message code="startform.selection.label"/></span>
	  <g:if test="${procdefInst?.startForms}">
	    <g:each in="${procdefInst.startForms}" var="msfd">
	      <span class="property-value">
		<g:set var="pfv" value="${msfd.formdef}"/>
		<g:if test="${pfv}">
		  <g:link controller="pxdFormdefVer" action="show" id="${pfv?.id}">${msfd?.encodeAsHTML()}</g:link>
		</g:if>
		<g:else>
		  <g:set var="linktitle"><g:message code="startform.selection.invalid.link"/></g:set>
		  <g:img uri="/images/silk/exclamation.png" title="${linktitle}"/> ${msfd?.encodeAsHTML()}
		</g:else>
		<g:if test="${editable}">
		  <g:link class="edit" action="edit" id="${procdefInst?.uuid}">
		    <g:img uri="/images/silk/pencil_go.png" title="${message(code: 'default.button.edit.label', default: 'Edit')}"/>
		  </g:link>
		</g:if>
	      </span>
	    </g:each>
	  </g:if>
	  <g:else>
	    <g:set var="statetitle"><g:message code="startform.selection.unselected"/></g:set>
	    <span class="property-value"><g:img uri="/images/silk/exclamation.png" title="${statetitle}"/>
	      <g:link class="edit" action="edit" id="${procdefInst?.uuid}">
		<g:img uri="/images/silk/pencil_go.png" title="${message(code: 'default.button.edit.label', default: 'Edit')}"/>
	      </g:link>
	    </span>
	  </g:else>
	</li>

	<g:if test="${procdefInst?.activities}">
	  <g:set var="procId" value="${procdefInst?.uuid}"/>
	  <g:set var="isEditable" value="${editable}"/>
	  <li class="fieldcontain">
	    <table>
	    <span id="activities-label" class="property-label"><g:message code="procdef.act.cnx.label" default="Activities" /></span>
	    <g:each in="${procdefInst.activities}" var="a">
	      <g:set var="formdef" value="${a?.activityFormdef}"/>
	      <g:set var="actId" value="${a?.uuid}"/>
	      <g:set var="cnx" value="${new org.motrice.coordinatrice.ActivityConnection(a, formdef?.formPath)}"/>
	      <span class="property-value" aria-labelledby="activities-label"><tr>
		  <td><g:link controller="actDef" action="show" id="${a?.fullId}">${a?.encodeAsHTML()}</g:link></td>
		  <g:render template="/activityConnection" model="[connection:cnx, actDefInst:a, editable:isEditable]"/>
	      </tr></span>
	    </g:each>
	    </table>
	  </li>
	</g:if>
      </ol>
      <g:form>
	<fieldset class="buttons">
	  <g:hiddenField name="uuid" value="${procdefInst?.uuid}" />
	  <g:link class="edit" action="newversion" id="${procdefInst?.uuid}"><g:message code="procdef.edit.label" default="Edit" /></g:link>
	</fieldset>
      </g:form>
    </div>
  </body>
</html>
