<%@ page import="org.motrice.coordinatrice.ActDef" %>
<%@ page import="org.motrice.coordinatrice.ActivityConnection" %>
<g:set var="procdefInst" value="${actDefInst?.process}"/>
<div class="fieldcontain">
  <label>
    <g:message code="procdef.label" default="Process" />
  </label>
  <g:link controller="procdef" action="show" id="${procdefInst?.uuid}">${procdefInst?.display.encodeAsHTML()}</g:link>
</div>
<div class="fieldcontain">
  <label>
    <g:message code="actDef.label" default="Label" />
  </label>
  <g:link controller="actDef" action="show" id="${actDefInst?.fullId}">${actDefInst?.name.encodeAsHTML()}</g:link>
</div>
<div class="fieldcontain">
  <g:set var="stateInfo" value="${activityConnection?.stateInfo()}"/>
  <label><g:message code="activity.connection.mode"/></label>
  <div class="property-value">
    <hr class="property-value-sep"/>
    <g:radio name="connectionState" value="${ActivityConnection.FORM_STATE}" checked="${activityConnection?.formState}"/>
    <g:message code="activity.connection.state.form"/>
    <g:select id="formsel" name="form.id" from="${formList}" optionKey="id" value="${selectedFormId}"
	      noSelection="['-1': message(code: 'activity.connection.select.form.label')]"/>
  </div>
  <div class="property-value">
    <hr class="property-value-sep"/>
    <g:radio name="connectionState" value="${ActivityConnection.NO_FORM_STATE}" checked="${activityConnection?.noFormState}"/>
    <g:message code="activity.connection.state.noform"/>
  </div>
  <div class="property-value">
    <hr class="property-value-sep"/>
    <g:radio name="connectionState" value="${ActivityConnection.SIGN_START_FORM_STATE}" checked="${activityConnection?.signStartFormState}"/>
    <g:message code="activity.connection.state.signstartform"/>
  </div>
  <g:if test="${!activityList.isEmpty()}">
    <div class="property-value">
      <hr class="property-value-sep"/>
      <g:radio name="connectionState" value="${ActivityConnection.SIGN_ACTIVITY_STATE}" checked="${activityConnection?.signActivityState}"/>
      <g:message code="activity.connection.state.signactivity"/>
      <g:select id="activity" name="activity.id" from="${activityList}" optionKey="uuid" required="" class="many-to-one"/>
    </div>
  </g:if>
</div>
