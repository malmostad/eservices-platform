<%@ include file="/WEB-INF/jspf/htmlTags.jspf"%>
<%--@elvariable id="crPage" type="java.lang.Integer"--%>
<%--@elvariable id="info" type="se.inherit.portal.componentsinfo.GeneralListInfo"--%>
<%--@elvariable id="pages" type="java.util.Collection<java.lang.Integer>"--%>
<%--@elvariable id="query" type="java.lang.String"--%>
<%--@elvariable id="result" type="org.hippoecm.hst.content.beans.query.HstQueryResult"--%>

<c:choose>
	<c:when test="${empty info}">
		<tag:pagenotfound />
	</c:when>
	<c:otherwise>
		<c:if test="${not empty info.title}">
			<hst:element var="headTitle" name="title">
				<c:out value="${info.title}" />
			</hst:element>
			<hst:headContribution keyHint="headTitle" element="${headTitle}" />
		</c:if>

<ul id="news-overview" data-role="listview">
    
    		<c:forEach var="item" items="${result.hippoBeans}">
			
		<hst:link var="link" hippobean="${item}" />	
		
		<li><a href="${link}">
		 	<h3>${item.title}</h3>
			</a>
			</li>


		                
			</c:forEach>
</ul>
			<!--if there are pages on the request, they will be printed by the tag:pages -->
			<tag:pages pages="${pages}" page="${page}" />
	</c:otherwise>
</c:choose>