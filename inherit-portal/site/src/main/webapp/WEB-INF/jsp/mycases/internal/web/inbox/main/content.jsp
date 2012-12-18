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
    
    <hst:cmseditlink hippobean="${document}"/>
    <h2>${document.title}</h2>
    <p>${document.summary}</p>
    <hst:html hippohtml="${document.html}"/>
    
    		<table width="100%" cellpadding="0" cellspacing="0" border="0" class="display dataTable">
				<thead>
					<tr>
					   <th><fmt:message key="mycases.process.column.lbl"/></th>
					   <th><fmt:message key="mycases.startDate.column.lbl"/></th>
					   <th><fmt:message key="mycases.expectedEndDate.column.lbl"/></th>
					   <th><fmt:message key="mycases.activity.column.lbl"/></th>
					</tr>
				</thead>
				<tbody>
			    <c:if test="${not empty tasks}">
					<c:forEach var="task" items="${tasks}">
						<tr>
					 	  	<td>${task.processLabel}</td>
					 	  	<td><fmt:formatDate value="${task.activityCreated}" type="Date" /></td> <!-- href="form?taskUuid={task.taskUuid}&activityDefinitionUUID={task.activityDefinitionUUID}">{task.activityLabel} -->
					 	  	<td><fmt:formatDate value="${task.expectedEndDate}" type="Date" /></td>
							<td><a href="${task.editFormUrl}">${task.activityLabel}</a></td>
						</tr>
					</c:forEach>
				</c:if>
				</tbody>
			</table>    
  </c:otherwise>  
</c:choose>
