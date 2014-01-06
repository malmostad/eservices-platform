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

	<c:if test="${not empty activity}">
    	<script type="text/javascript" charset="utf-8">
		        $jq(document).ready(function () {
		             $jq("#xform").load("<fmt:message key="orbeonbase.portal.url"/>${activity.formUrl}", function(data) {
		                if (typeof ORBEON != "undefined") { 
		                    if (!document.all) {
		                        ORBEON.xforms.Init.document(); 
		                    } 
		                } 
		        	    }); 
					});
		</script>
    </c:if>
			
    <h1><fmt:message key="mycases.activity.column.lbl"/></h1>	   

    <p> ${activity.activityLabel} i ${processInstanceDetails.processLabel}</p>

	<!--  activity form (ajax load after page is loaded) -->
	<div class="row-fluid">
		<div class="span12">
    		<div id="xform" class="komin-xform">Loading form...please wait...</div>
		</div>
	</div>    
    
    
    
  </c:otherwise>  
</c:choose>
	
<p></p>
<h1><fmt:message key="mycases.processInstanceDetails.lbl"/></h1>

<h2><fmt:message key="mycases.commentFeed.lbl"/></h2>
<ul id="commentfeed">
</ul>

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
  	 <c:if test="${not empty timelineByDay}">
		<c:forEach var="dayEntry" items="${timelineByDay}">
			<h3><fmt:formatDate value="${dayEntry.key}" type="Date" dateStyle="long" timeStyle="short"/></h3>
			
			<ul class="toggle-view timeline">
				<c:forEach var="logItem" items="${dayEntry.value}">
					<li>
					  <h4><fmt:formatDate value="${logItem.timestamp}" type="Both" dateStyle="short" timeStyle="short"/>&nbsp;${logItem.briefDescription}&nbsp;(${logItem.user})</h4>
					  <span class="exp">+ visa mer...</span>
					  <div class="panel komin-xform">
						<c:choose>
							<c:when test="${not empty logItem.description}">
								<p>${logItem.description}</p>
							</c:when>
							<c:when test="${not empty logItem.viewUrl and fn:startsWith(logItem.viewUrl, '/docbox/doc/ref')}">
							    <p> <fmt:message key="mycases.signeddocument"/>&nbsp;
								  <a href="${logItem.viewUrl}"><fmt:message key="mycases.signeddocumentlink"/></a>
								</p>
							</c:when>
							<c:when test="${not empty logItem.viewUrl and not fn:startsWith(logItem.viewUrl, 'none/') and not fn:startsWith(logItem.viewUrl, 'notify/') and not fn:startsWith(logItem.viewUrl, '/docbox/doc/ref')}">
								<iframe class="iframe-orbeon-panel" scrolling="no" frameborder="0" width="100%" height="100"></iframe>
							</c:when>
							<c:otherwise>
								<p><fmt:message key="mycases.nomoredetails"/></p>
							</c:otherwise>
						</c:choose>
					  </div>
					  <c:if test="${not empty logItem.viewUrl and not fn:startsWith(logItem.viewUrl, 'none/') and not fn:startsWith(logItem.viewUrl, 'notify/') and not fn:startsWith(logItem.viewUrl, '/docbox/doc/ref')}">
					  	<a class="view-url" href="<fmt:message key="orbeonbase.portal.url"/>${logItem.viewUrl}"></a>
					  </c:if>
					</li>
				</c:forEach>
			</ul>
		</c:forEach>
	 </c:if>
    
  </c:otherwise>
 </c:choose>