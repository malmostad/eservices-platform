<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>

<%@ attribute name="timelineItems" type="java.util.List<org.inheritsource.service.common.domain.TimelineItem>" rtexprvalue="true" required="true"%>
<%@ attribute name="viewMode" required="true"%>
<!--  
	timelineItems can be generated by Timeline getItems method
	viewMode values
	         full - view all task forms and which user that performed a task
	         limit - view my own task forms, hide which user that performed the task
-->

<ul class="actlist">
<c:forEach var="timelineItem" items="${timelineItems}">
	<c:if test="${not empty timelineItem.actUri}">
		<li><a href="${timelineItem.actUri}">${timelineItem.briefDescription}</a></li> 
	</c:if>
</c:forEach>
</ul>