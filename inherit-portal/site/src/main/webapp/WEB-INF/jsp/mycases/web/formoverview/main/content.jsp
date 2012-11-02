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
 
		<section id="news-overview">

			<c:forEach var="item" items="${result.hippoBeans}">

				<div class="page-header">
					<h3>
						${item.title}
					</h3>
				</div>
				<div class="row-fluid">
					<div class="span8">
						<p>${item.summary}</p>
					</div>
					<div class="span4">
						<hst:link var="link" hippobean="${item}" />
						<a href="${link}">e-tj&auml;nst <i class="icon-chevron-right"></i></a>
					</div>
				</div>

			</c:forEach>

			<!--if there are pages on the request, they will be printed by the tag:pages -->
			<tag:pages pages="${pages}" page="${page}" />
	</c:otherwise>
</c:choose>