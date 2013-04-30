<%-- 
    Process Aware Web Application Platform 
 
    Copyright (C) 2011-2013 Inherit S AB 
 
    This program is free software: you can redistribute it and/or modify 
    it under the terms of the GNU Affero General Public License as published by 
    the Free Software Foundation, either version 3 of the License, or 
    (at your option) any later version. 
 
    This program is distributed in the hope that it will be useful, 
    but WITHOUT ANY WARRANTY; without even the implied warranty of 
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
    GNU Affero General Public License for more details. 
 
    You should have received a copy of the GNU Affero General Public License 
    along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 
    e-mail: info _at_ inherit.se 
    mail: Inherit S AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN 
    phone: +46 8 641 64 14 
 --%> 
 
<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>
<%--@elvariable id="document" type="org.inheritsource.portal.beans.NewsDocument"--%>

<script type="text/javascript" charset="utf-8">
		        jQuery.noConflict();
		        var $j = jQuery;
		        $j(document).ready(function () {
		        	$j("#procLogDetail tr:odd").addClass("odd");
		            $j("#procLogDetail tr:not(.odd)").hide();
		            $j("#procLogDetail tr:first-child").show();
		            
		            $j("#procLogDetail tr.odd").click(function(){
			           	var url = $j(this).children('td').eq(0).children('a').attr('href');
			           	if (typeof(url) !== 'undefined' && url.length>0) {
				            var nextRow =  $j(this).next("tr");
							
					        $j(nextRow).find("div").load("<fmt:message key="orbeonbase.portal.url"/>" + url, function(data) {
						                if (typeof ORBEON != "undefined") { 
						                    if (!document.all) {
						                        ORBEON.xforms.Init.document(); 
						                    } 
						                } 
						        	    }
								); 
				                $j(nextRow).toggle();
				                $j(this).find(".arrow").toggleClass("up");
			            }
		            });
				});
		        
		</script>
		
<h1><fmt:message key="mycases.processInstanceDetails.lbl"/></h1>

<c:choose>
  <c:when test="${empty processInstanceDetails}">
  	<fmt:message key="mycases.noProcessInstanceDetails.lbl"/>
  </c:when>
  <c:otherwise> 
  	<p>${processInstanceDetails.processLabel}
  	</p>
 	<h2><fmt:message key="mycases.pendingActivities.lbl"/></h2>   
 	<p>
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
	</p>    
	
	<h2><fmt:message key="mycases.commentFeed.lbl"/></h2>   
 	<p>
		 <table class="display dataTable">
			<thead>
				<tr>
				   <th><fmt:message key="mycases.timestamp.column.lbl"/></th>
				   <th><fmt:message key="mycases.userId.column.lbl"/></th>
				   <th><fmt:message key="mycases.message.column.lbl"/></th>
				</tr>
			</thead>
			<tbody>
		    <c:if test="${not empty processInstanceDetails.commentFeed}">
				<c:forEach var="comment" items="${processInstanceDetails.commentFeed}">
					<tr>
				 	  	<td><fmt:formatDate value="${comment.timeStamp}" type="Both" dateStyle="short" timeStyle="short"/></td>
				 	  	</td>
						<td>${comment.user}</td>
						<td>${comment.message}</td>
					</tr>
				</c:forEach>
			</c:if>
			</tbody>
		</table>
	</p>    
	
  	 <h2><fmt:message key="mycases.activityLog.lbl"/></h2>   
  	 <p>
		 <table id="procLogDetail" class="display dataTable">
			<thead>
				<tr>
				   <th><fmt:message key="mycases.activity.column.lbl"/></th>
				   <th><fmt:message key="mycases.startDate.column.lbl"/></th>
				   <th><fmt:message key="mycases.endDate.column.lbl"/></th>
				   <th><fmt:message key="mycases.performedBy.column.lbl"/></th>
				</tr>
			</thead>
			<tbody>
		    <c:if test="${not empty processInstanceDetails.activityLog}">
				<c:forEach var="logItem" items="${processInstanceDetails.activityLog}">
					<tr>
				 	  	<td><a href="${logItem.viewUrl}">${logItem.activityLabel}</a></td>
				 	  	<td><fmt:formatDate value="${logItem.startDate}" type="Both" dateStyle="short" timeStyle="short" /></td>
				 	  	<td><fmt:formatDate value="${logItem.endDate}" type="Both" dateStyle="short" timeStyle="short"/></td>
						<td>${logItem.performedByUser}</td>
					</tr>
					<tr>
					  <td colspan="4">
					     <div class="xform">Loading activity details...please wait...</div>
					  </td>
					</tr>
				</c:forEach>
				<tr>
				 	  	<td><a href="${processInstanceDetails.startActivity.viewUrl}"><fmt:message key="mycases.caseStarted.lbl"/></a></td>
				 	  	<td><fmt:formatDate value="${processInstanceDetails.startDate}" type="Both" dateStyle="short" timeStyle="short"/></td>
				 	  	<td></td>
						<td>${processInstanceDetails.startedBy}</td>
				</tr>
				<tr>
				  <td colspan="4">
				     <div class="xform">Loading activity details...please wait...</div>
				  </td>
				</tr>
				
				
			</c:if>
			</tbody>
		</table>    
	</p>
    
  </c:otherwise>
 </c:choose>
