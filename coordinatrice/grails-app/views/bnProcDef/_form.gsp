<%@ page import="org.motrice.coordinatrice.bonita.BnProcDef" %>
<div class="fieldcontain ${hasErrors(bean: bnProcDefInst, field: 'uuid', 'error')} ">
  <label>
    <g:message code="bnProcDef.label" default="Process" />
  </label>
  <g:link action="show" id="${bnProcDefInst?.id}">${bnProcDefInst?.uuid.encodeAsHTML()}</g:link>
</div>
<div class="fieldcontain">
  <label><g:message code="startform.selection.label"/></label>
  <div class="property-value">
    <g:select id="formsel" name="form.id" from="${formList}" optionKey="id" value="${selectedFormId}"
	      noSelection="['-1': message(code: 'startform.selection.select.form.label')]"/>
  </div>
</div>
<div class="fieldcontain">
  <g:if test="${bnProcDefInst?.startForms}">
    <g:set var="bnProcDefId" value="${bnProcDefInst?.id}"/>
    <g:each in="${bnProcDefInst.startForms}" var="msfd">
      <span class="property-value">
	<g:set var="pfv" value="${msfd.formdef}"/>
	<g:if test="${pfv}">
	  <g:link controller="pxdFormdefVer" action="show" id="${pfv?.id}">${msfd?.encodeAsHTML()}</g:link>
	</g:if>
	<g:else>
	  <g:set var="linktitle"><g:message code="startform.selection.invalid.link"/></g:set>
	  <g:img uri="/images/silk/exclamation.png" title="${linktitle}"/> ${msfd?.encodeAsHTML()}
	</g:else>
	<g:link class="delete" controller="mtfStartFormDefinition" action="delete" id="${msfd?.id}" params="[bnProcDefId: bnProcDefId]">
	  <g:img uri="/images/silk/delete.png" title="${message(code: 'default.button.delete.label', default: 'Delete')}"/>
	</g:link>
      </span>
    </g:each>
  </g:if>
  
</div>
