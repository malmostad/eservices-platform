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

	<section class="box" contextmenu="case-details-menu" id="case-details">
	  <h1 class="box-title"><fmt:message key="mycases.processInstanceDetails.lbl"/></h1>
	  <div class="box-content body-copy">
	        <h3><fmt:message key="mycases.diaryNo.lbl" /></h3>
	        <form>
	        	<input type="hidden" name="motrice-activity-instance-uuid" value="${activity.activityInstanceUuid}"/>
	        	<input type="hidden" name="motrice-process-instance-uuid" value="${activity.processInstanceUuid}"/>
				<ul id="diaryNo">
				  <c:forEach var="tag" items="${tags}">
			        <c:if test="${not empty tag and tag.typeId eq 1}">
					  <li>${tag.value}</li>
					</c:if>
			      </c:forEach>
				</ul>
			</form>
	        
			<h3>Status</h3>
			<ul>
			  <li>Registrering</li>
			</ul>
			
			<h3><fmt:message key="mycases.actlist.lbl" /></h3>
			<tag:actlist timelineItems="${processInstanceDetails.timeline.items}" viewMode="full"/>
			<h3><fmt:message key="mycases.commentFeed.lbl"/></h3>
			<ul id="commentfeed">
			</ul>			
			<h3><fmt:message key="mycases.tags.lbl" /></h3> 			
			<form>
			  <input type="hidden" name="motrice-activity-instance-uuid" value="${activity.activityInstanceUuid}"/>
			  <input type="hidden" name="motrice-process-instance-uuid" value="${activity.processInstanceUuid}"/>
					<ul id="myTags">
				     <c:if test="${not empty tags}">
			           <c:forEach var="tag" items="${tags}">
			             <c:if test="${not empty tag and tag.typeId ne 1}">
						   <li>${tag.value}</li>
						 </c:if>
					   </c:forEach>
					 </c:if>
					</ul>
				</form>
	  </div>
	</section>		

	<section class="box" contextmenu="task-section-menu" id="task-section">
	  <h1 class="box-title"><fmt:message key="mycases.activity.column.lbl"/>: ${activity.activityLabel} i ${processInstanceDetails.processLabel}</h1>
	  <div class="box-instructions">
	     <p>Lås till mig för att utföra aktiviteten. När aktiviten är låst till en person så visas den i inkorgen endast för den personen. Släpp lås om du vill att övriga kandidater ska se aktiviteten igen. </p>
	  </div>
	  
	  <div class="box-content body-copy">
		    <p><button class="btn btn-danger motrice-unassign-user"><fmt:message key="mycases.unassign.lbl"/><input type="hidden" name="motrice-unassign-user" value="${user.uuid}"/><input type="hidden" name="motrice-activity-instance-uuid" value="${activity.activityInstanceUuid}"/></button></p>
		    <p><button class="btn btn-primary motrice-assign-to"><fmt:message key="mycases.assigntome.lbl"/><input type="hidden" name="motrice-assign-to" value="${user.uuid}"/><input type="hidden" name="motrice-activity-instance-uuid" value="${activity.activityInstanceUuid}"/></button></p>
	    
	    <div id="xform" class="komin-xform"><fmt:message key="orbeon.loading.lbl"/><a class="view-url" href="${activity.editUrl}"/></div>
	  </div>
	<a href="#" class="toggle-instructions" title="Show instructions">?</a>
	</section>

  </c:otherwise>  
</c:choose>
	
<p></p>
<h1><fmt:message key="mycases.processInstanceDetails.lbl"/></h1>


<c:choose>
  <c:when test="${empty processInstanceDetails}">
  	<fmt:message key="mycases.noProcessInstanceDetails.lbl"/>
  </c:when>
  <c:otherwise> 
 	<h2><fmt:message key="mycases.pendingActivities.lbl"/></h2>   
 	<p></p>
	 <table class="display dataTable">
		<thead>
			<tr>
			   <th><fmt:message key="mycases.activity.column.lbl"/></th>
			   <th><fmt:message key="mycases.startDate.column.lbl"/></th>
			   <th><fmt:message key="mycases.expectedEndDate.column.lbl"/></th>
			   <th><fmt:message key="mycases.candidates.column.lbl"/></th>
			   <th><fmt:message key="mycases.assignedto.column.lbl"/></th>			   
			</tr>
		</thead>
		<tbody>
	    <c:if test="${not empty processInstanceDetails.pending}">
			<c:forEach var="pendingTask" items="${processInstanceDetails.pending}">
				<tr>
			 	  	<td>${pendingTask.activityLabel}</td>
			 	  	<!--  TODO check start date vs lastStateUpdate -->
			 	  	<td><fmt:formatDate value="${pendingTask.lastStateUpdate}" type="Both" dateStyle="short" timeStyle="short"/></td>
			 	  	<td><fmt:formatDate value="${pendingTask.expectedEndDate}" type="Date" dateStyle="short" timeStyle="short"/></td> 
			 	  	<td>
			 	  	<c:choose>
				 	  	<c:when test="${empty pendingTask.candidates}}">
				 	  		
				 	  	</c:when>
				 	  	<c:otherwise>
				 	  		<c:forEach var="candidate" items="${pendingTask.candidates}">
				 	  			${candidate}<br>
				 	  		</c:forEach>
				 	  	</c:otherwise>
			 	  	</c:choose>
			 	  	</td>
					<td>${pendingTask.assignedUser}</td>
				</tr>
			</c:forEach>
		</c:if>
		</tbody>
	</table>
	<p></p> 
	
  	    
  	 <h2><fmt:message key="mycases.timeline.lbl"/></h2>
  	 <p></p>
  	 
  	 <tag:timelinebyday timelineByDay="${timelineByDay}" viewMode="full"/>
  	     
  </c:otherwise>
 </c:choose>