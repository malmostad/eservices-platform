<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>
<%--@elvariable id="document" type="se.inherit.portal.beans.NewsDocument"--%>

<c:choose>
  <c:when test="${empty document}">
    <tag:pagenotfound/>
  </c:when>
  <c:otherwise>
    <c:if test="${not empty document.title}">
      <hst:element var="headTitle" name="title">
        <c:out value="${document.title}"/>
      </hst:element>
      <hst:headContribution keyHint="headTitle" element="${headTitle}"/>
    </c:if>


	<script type="text/javascript" charset="utf-8">
		        jQuery.noConflict();
		        var $j = jQuery;

		        $j(document).ready(function () {
		             $j("#xform").load("<fmt:message key="orbeonbase.portal.url"/>${formUrl}", function(data) {
		                if (typeof ORBEON != "undefined") { 
		                    if (!document.all) {
		                        ORBEON.xforms.Init.document(); 
		                    } 
		                } 
		        	    }); 
					});
				
				
				</script>
				
	<div class="row-fluid">
		<div class="span12">
    		<div id="xform">Loading form...please wait...</div>
		</div>
	</div>    
    
  </c:otherwise>  
</c:choose>

<!--  process instance details begin  -->

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
				 	  	<td><fmt:formatDate value="${pendingTask.lastStateUpdate}" type="Both" /></td>
				 	  	<td><fmt:formatDate value="${pendingTask.expectedEndDate}" type="Date" /></td> 
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
						<td>${pendingTask.assignedUserId}</td>
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
				 	  	<td><fmt:formatDate value="${comment.timeStamp}" type="Both" /></td>
				 	  	</td>
						<td>${comment.userId}</td>
						<td>${comment.message}</td>
					</tr>
				</c:forEach>
			</c:if>
			</tbody>
		</table>
	</p>    
	
  	 <h2><fmt:message key="mycases.activityLog.lbl"/></h2>   
  	 <p>
		 <table class="display dataTable">
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
				 	  	<td>${logItem.activityLabel}</td>
				 	  	<td><fmt:formatDate value="${logItem.startDate}" type="Both" /></td>
				 	  	<td><fmt:formatDate value="${logItem.endDate}" type="Both" /></td>
						<td>${logItem.performedByUserId}</td>
					</tr>
				</c:forEach>
				<tr>
				 	  	<td><fmt:message key="mycases.caseStarted.lbl"/></td>
				 	  	<td><fmt:formatDate value="${processInstanceDetails.startDate}" type="Both" /></td>
				 	  	<td></td>
						<td>${processInstanceDetails.startedBy}</td>
				</tr>
				
			</c:if>
			</tbody>
		</table>    
	</p>
    
  </c:otherwise>
 </c:choose>
