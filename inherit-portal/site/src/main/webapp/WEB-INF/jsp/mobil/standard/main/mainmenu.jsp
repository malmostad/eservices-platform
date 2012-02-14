<%@ page language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<%@ taglib uri="http://www.hippoecm.org/jsp/hst/core" prefix='hst'%>


<div data-role="navbar">
	<ul>
		<c:forEach var="item" items="${menu.siteMenuItems}">
			<hst:link var="link" link="${item.hstLink}" />
			<c:if test="${empty link}">
				<c:set var="link" value="${item.externalLink}" />
			</c:if>
			<c:choose>
				<c:when test="${item.selected or item.expanded}">
					<li><a class="ui-btn-active" href="${link}">${item.name}</a></li>
				</c:when>
				<c:otherwise>
					<li><a href="${link}"> ${item.name} </a></li>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</ul>
</div>