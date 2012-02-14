<%@ page language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<%@ taglib uri="http://www.hippoecm.org/jsp/hst/core" prefix='hst'%>
<%@ include file="/WEB-INF/jspf/htmlTags.jspf"%>
<%--@elvariable id="menu" type="org.hippoecm.hst.core.sitemenu.HstSiteMenu"--%>

<c:forEach var="item" items="${menu.siteMenuItems}">
	<c:if test="${item.expanded and not empty item.childMenuItems}">
		<div data-role="navbar">
			<ul>
				<c:forEach var="subitem" items="${item.childMenuItems}">
					<hst:link var="link" link="${subitem.hstLink}" />
					<c:if test="${empty link}">
						<c:set var="link" value="${subitem.externalLink}" />
					</c:if>

					<c:choose>
						<c:when test="${subitem.selected}">
							<li><a class="ui-btn-active" href="${link}">${subitem.name}</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="${link}">${subitem.name}</a></li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</ul>
		</div>
	</c:if>
</c:forEach>