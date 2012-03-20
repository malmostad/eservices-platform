<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ attribute name="pages" required="true" type="java.util.List"
	rtexprvalue="true"%>
<%@ attribute name="page" required="true" type="java.lang.Integer"
	rtexprvalue="true"%>
<%@ attribute name="query" required="false" type="java.lang.String"
	rtexprvalue="true"%>
<c:if test="${fn:length(pages) gt 0}">
	<ul id="paging-nav" class="pager">
		<c:choose>
			<c:when test="${crPage gt 1}">
				<hst:renderURL var="prevlink">
					<hst:param name="page" value="${crPage-1}" />
					<hst:param name="query" value="${query}" />
				</hst:renderURL>
				<li class="previous"><a href="${prevlink}" title="previous">Previous</a></li>
			</c:when>
			<c:otherwise>
				<li class="previous disabled"><a href="#" title="previous">Previous</a></li>
			</c:otherwise>
		</c:choose>
		
		<c:forEach var="page" items="${pages}">
			<hst:renderURL var="pagelink">
				<hst:param name="page" value="${page}" />
				<hst:param name="query" value="${query}" />
			</hst:renderURL>

			<c:set var="active" value="" />
			<c:choose>
				<c:when test="${crPage == page}">
					<li class="active disabled"><a href="${pagelink}" title="${page}">${page}</a></li>
				</c:when>
				<c:otherwise>
					<li><a href="${pagelink}" title="${page}">${page}</a></li>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		
		<c:choose>
			<c:when test="${crPage lt fn:length(pages)}">
				<hst:renderURL var="nextlink">
					<hst:param name="page" value="${crPage-1}" />
					<hst:param name="query" value="${query}" />
				</hst:renderURL>
				<li class="next"><a href="${nextlink}" title="next">Next</a></li>
			</c:when>
			<c:otherwise>
				<li class="next disabled"><a href="#" title="next">Next</a></li>
			</c:otherwise>
		</c:choose>
		
	</ul>
</c:if>
