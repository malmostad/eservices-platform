<%@ page language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<%@ taglib uri="http://www.hippoecm.org/jsp/hst/core" prefix='hst'%>
<%@ include file="/WEB-INF/jspf/htmlTags.jspf"%>
<%--@elvariable id="menu" type="org.hippoecm.hst.core.sitemenu.HstSiteMenu"--%>


<c:if test="${not empty breadcrumb}">
	<ul class="breadcrumb">
	<c:forEach var="item" items="${breadcrumb}">
	    <li>
	    	<hst:link var="link" link="${item.hstLink}"/>
	    	<c:if test="${empty link}">
				<c:set var="link" value="${subitem.externalLink}" />
			</c:if>
    		<a href="${link}">${item.name}</a> <span class="divider">/</span>
    	</li>
	</c:forEach>
		<c:if test="${not empty document}">
			<c:if test="${hst:isReadable(document, 'title')}">
				 <li class="active">${document.title}</li>
			</c:if>
		</c:if>
    </ul>	
</c:if>
