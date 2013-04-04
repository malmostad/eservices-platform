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
		<table class="display dataTable" width="100%">
			<thead>
				<tr>
					<th><fmt:message key="mycases.process.column.lbl" /></th>
					<th><fmt:message key="mycases.status.column.lbl" /></th>
					<th><fmt:message key="mycases.startDate.column.lbl" /></th>
					<th><fmt:message key="mycases.endDate.column.lbl" /></th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${not empty processInstances}">
						<c:forEach var="item" items="${processInstances}">
							<tr>
								<td><a
									href="processinstancedetail?processInstanceUuid=${item.processInstanceUuid}">${item.processLabel}</a></td>
								<td>${item.status}</td>
								<td><fmt:formatDate value="${item.startDate}" type="Date" /></td>
								<td><fmt:formatDate value="${item.endDate}" type="Date" /></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="4"><fmt:message key="mycases.noProcessInstance.lbl" /></td>
						</tr>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>

	</c:otherwise>
</c:choose>


