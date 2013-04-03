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
		<c:choose>
			<c:when test="${not empty processInstances}">
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
						<c:forEach var="item" items="${processInstances}">
							<tr>
								<td><a
									href="processinstancedetail?processInstanceUuid=${item.processInstanceUuid}">${item.processLabel}</a></td>
								<td>${item.status}</td>
								<td><fmt:formatDate value="${item.startDate}" type="Date" /></td>
								<td><fmt:formatDate value="${item.endDate}" type="Date" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:when>
			<c:otherwise>
				<p>
					<fmt:message key="mycases.noProcessInstance.lbl" />
				</p>
			</c:otherwise>
		</c:choose>

	</c:otherwise>
</c:choose>


