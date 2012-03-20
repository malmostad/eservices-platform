<%@ page language="java"%>
<%@ include file="/WEB-INF/jspf/htmlTags.jspf"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<%@ taglib uri="http://www.hippoecm.org/jsp/hst/core" prefix='hst'%>

<div class="navbar">
	<div class="navbar-inner">
		<div class="container">
			<a class="btn btn-navbar" data-toggle="collapse"
				data-target=".nav-collapse"> <span class="icon-bar"></span> <span
				class="icon-bar"></span> <span class="icon-bar"></span>
			</a> 
			
			<div class="nav-collapse">
				<a class="brand" href="#"> <fmt:message key="brand.name" />
				</a>
			</div>

			<ul class="nav">
				<c:forEach var="item" items="${menu.siteMenuItems}">

					<c:choose>

						<c:when test="${item.selected or item.expanded}">
							<li class="active"><a href="#">${item.name}</a></li>
						</c:when>

						<c:otherwise>

							<hst:link var="link" link="${item.hstLink}" />
							<c:if test="${empty link}">
								<c:set var="link" value="${item.externalLink}" />
							</c:if>
							<li><a href="${link}"> ${item.name} </a></li>
						</c:otherwise>

					</c:choose>

				</c:forEach>
			</ul>

			<div class="nav-collapse">
				<hst:link var="link" path="/search" />
				<form action="${link}" method="POST"
					class="navbar-search pull-right">
					<input type="text" class="search-query" placeholder="Search"
						name="query">
				</form>
			</div>
		</div>
	</div>
</div>

