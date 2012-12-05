<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>
<%--@elvariable id="document" type="se.inherit.portal.beans.NewsDocument"--%>

<script type="text/javascript" charset="utf-8">
		        jQuery.noConflict();
		        var $j = jQuery;
		        $j(document).ready(function () {
		            
		            $j("li.logItemDetails").click(function(){
			           	var url = $j(this).children("a.viewForm").attr('href');
			           	if (typeof(url) !== 'undefined' && url.length>0) {
				            var divItemLi = $j(this).next();
							var divElem = $j(divItemLi).find("div");
							$j(divElem).load("<fmt:message key="orbeonbase.portal.url"/>" + url, function(data) {
						                if (typeof ORBEON != "undefined") { 
						                    if (!document.all) {
						                        ORBEON.xforms.Init.document(); 
						                    } 
						                } 
						        	}
								); 
				        	
				               
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
	
  	    
  	 <h2><fmt:message key="mycases.timeline.lbl"/></h2>
  	 <ul>
	 <c:if test="${not empty timelineByDay}">
		<c:forEach var="dayEntry" items="${timelineByDay}">
			<h3><fmt:formatDate value="${dayEntry.key}" type="Date"/></h3>
			<c:forEach var="logItem" items="${dayEntry.value}">
					<li class="logItemDetails"><fmt:formatDate value="${logItem.timestamp}" type="Both"/>&nbsp;<a class="viewForm" href="${logItem.viewUrl}"></a>${logItem.briefDescription}&nbsp;(${logItem.userId})</li>
					<li><div class="xform"></div></li>
			</c:forEach>
		</c:forEach>
	 </c:if>
	 </ul>
    
  </c:otherwise>
 </c:choose>
