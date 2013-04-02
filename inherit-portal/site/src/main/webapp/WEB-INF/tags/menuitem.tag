<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>

<%@ attribute name="siteMenuItem" type="org.hippoecm.hst.core.sitemenu.HstSiteMenuItem" rtexprvalue="true" required="true"%>
<!--  modifierad tag för malmö (baserat på hippo genererad) -->
<c:set var="link">
   	<c:choose>
     		<c:when test="${not empty siteMenuItem.externalLink}">${siteMenuItem.externalLink}</c:when>
     		<c:otherwise><hst:link link="${siteMenuItem.hstLink}"/></c:otherwise>
   	</c:choose>
</c:set>
<c:choose>
  <c:when test="${siteMenuItem.selected}">
  	
  		<c:choose>
  			<c:when test="${siteMenuItem.expanded and not empty siteMenuItem.childMenuItems}">
  			<li class="inbranch selected active"><a href="${link}"><c:out value="${siteMenuItem.name}"/></a>
			  <ul>
			    <c:forEach var="child" items="${siteMenuItem.childMenuItems}">
			        <tag:menuitem siteMenuItem="${child}"/>
			    </c:forEach>
			  </ul>
			</li>
			</c:when>
			<c:otherwise>
				<li class="current selected no-children active"><a href="${link}"><c:out value="${siteMenuItem.name}"/></a></li>
			</c:otherwise>
		</c:choose>	
  </c:when>
  <c:otherwise>
  	<li><a href="${link}"><c:out value="${siteMenuItem.name}"/></a></li>
  </c:otherwise>
</c:choose>