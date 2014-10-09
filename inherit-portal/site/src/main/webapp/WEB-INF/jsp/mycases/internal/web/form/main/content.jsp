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
 
 
<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>
<%--@elvariable id="document" type="org.inheritsource.portal.beans.NewsDocument"--%>

<c:choose>
  <c:when test="${empty activity}">
    <tag:pagenotfound/>
  </c:when>
  <c:otherwise>
    <c:if test="${not empty activity and not empty processInstanceDetails}">
      <hst:element var="headTitle" name="title">
        <c:out value="${activity.activityLabel}--${processInstanceDetails.processLabel}"/>
      </hst:element>
      <hst:headContribution keyHint="headTitle" element="${headTitle}"/>
    </c:if>
	
	<tag:caseoverview activity="${activity}" processInstanceDetails="${processInstanceDetails}" casetags="${tags}"/>
		
	<section class="box" contextmenu="task-section-menu" id="task-section">
	  <h1 class="box-title"><fmt:message key="mycases.activity.column.lbl"/>: ${activity.activityLabel} i ${processInstanceDetails.processLabel}</h1>
	  <div class="box-instructions">
	     <p>Lås till mig för att utföra aktiviteten. När aktiviten är låst till en person så visas den i inkorgen endast för den personen. Släpp lås om du vill att övriga kandidater ska se aktiviteten igen. </p>
	  </div>
	  
	  <div class="box-content body-copy">
		    <p><button class="btn btn-danger motrice-unassign-user motrice-unassign-user-btn motrice-initial-hidden"><i class="fa fa-unlock-alt"></i>&nbsp;<fmt:message key="mycases.unassign.lbl"/><input type="hidden" name="motrice-unassign-user" value="${user.uuid}"/><input type="hidden" name="motrice-activity-instance-uuid" value="${activity.activityInstanceUuid}"/></button></p>
		    <p><button class="btn btn-primary motrice-assign-to"><i class="fa fa-lock"></i>&nbsp;<fmt:message key="mycases.assigntome.lbl"/><input type="hidden" name="motrice-assign-to" value="${user.uuid}"/><input type="hidden" name="motrice-activity-instance-uuid" value="${activity.activityInstanceUuid}"/></button></p>
	        <p class="motrice-assigned-to-other-user motrice-initial-hidden">Aktiviteten är låst till en annan användare</p>
	        <p class="motrice-assigned-to-other-user motrice-initial-hidden"><button class="btn btn-danger motrice-unassign-user-btn"><i class="fa fa-unlock-alt"></i>&nbsp;<fmt:message key="mycases.unassign.lbl"/><input type="hidden" name="motrice-unassign-user" value="${user.uuid}"/><input type="hidden" name="motrice-activity-instance-uuid" value="${activity.activityInstanceUuid}"/></button></p>
	    <div id="xform" class="komin-xform motrice-unassign-user motrice-initial-hidden"><fmt:message key="orbeon.loading.lbl"/><a class="view-url" href="${activity.editUrl}"/></div>
	  </div>
	<a href="#" class="toggle-instructions" title="Show instructions">?</a>
	</section>

  </c:otherwise>  
</c:choose>