<%@ include file="/WEB-INF/jspf/htmlTags.jspf"%>
<%--@elvariable id="document" type="se.inherit.portal.beans.NewsDocument"--%>

<c:choose>
	<c:when test="${empty document}">
		<tag:pagenotfound />
	</c:when>
	<c:otherwise>
		<c:if test="${not empty document.title}">
			<hst:element var="headTitle" name="title">
				<c:out value="${document.title}" />
			</hst:element>
			<hst:headContribution keyHint="headTitle" element="${headTitle}" />
		</c:if>
		<hst:cmseditlink hippobean="${document}" />
		<h2>${document.title}</h2>
		<p>${document.summary}</p>
		<hst:html hippohtml="${document.html}" />

		<c:if test="${not empty processInstances}">
			<table class="display dataTable">
				<thead>
					<tr>
						<th><fmt:message key="mycases.process.column.lbl" /></th>
						<th><fmt:message key="mycases.status.column.lbl" /></th>
						<th><fmt:message key="mycases.startDate.column.lbl" /></th>
						<th><fmt:message key="mycases.endDate.column.lbl" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="item" items="${processInstances}">
						<tr>
							<td>${item.processLabel}</td>
							<td>${item.status}</td>
							<td><fmt:formatDate value="${item.startDate}" type="Date" /></td>
							<td><fmt:formatDate value="${item.endDate}" type="Date" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>

	</c:otherwise>
</c:choose>


