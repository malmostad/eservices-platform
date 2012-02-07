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
    
	<c:if test="${not empty processInstances}">
		<table>
		<tr>
		   <td><fmt:message key="bonita.process.column.lbl"/></td>
		   <td><fmt:message key="bonita.status.column.lbl"/></td>
		   <td><fmt:message key="bonita.startDate.column.lbl"/></td>
		   <td><fmt:message key="bonita.endDate.column.lbl"/></td>
		</tr>
	 	<c:forEach var="item" items="${processInstances}">
	 	  <tr>
	 	  	<td>${item.processLabel}</td>
	 	  	<td>${item.status}</td>
	 	  	<td>${item.startDate}</td>
	 	  	<td>${item.endDate}</td>
	 	  </tr>
		</c:forEach>
		</table>
	</c:if>
    
  </c:otherwise>  
</c:choose>


