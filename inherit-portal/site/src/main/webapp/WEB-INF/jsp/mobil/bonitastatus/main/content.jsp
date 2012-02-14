<%@ include file="/WEB-INF/jspf/htmlTags.jspf"%>
<%--@elvariable id="document" type="se.inherit.portal.beans.NewsDocument"--%>

<c:if test="${not empty processInstances}">
	<ul data-role="listview">
		<c:forEach var="item" items="${processInstances}">
			<li>
				<h3>${item.processLabel}</h3>
				<p>
					<fmt:message key="bonita.startDate.column.lbl"/>:&nbsp;<fmt:formatDate value="${item.startDate}" type="Date" />
				</p>
				<p>
					<fmt:message key="bonita.status.column.lbl"/>:&nbsp;${item.status}&nbsp;<fmt:formatDate value="${item.endDate}" type="Date" />
				</p>
			</li>
		</c:forEach>
	</ul>
</c:if>


