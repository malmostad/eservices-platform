<%@ include file="/WEB-INF/jspf/htmlTags.jspf"%>
<%--@elvariable id="document" type="se.inherit.portal.beans.NewsDocument"--%>

<c:if test="${not empty tasks}">

	<c:forEach var="task" items="${tasks}">
		<ul data-role="listview">
			<li><a href="form?taskUuid=${task.taskUUID}&activityDefinitionUUID=${task.activityDefinitionUUID}">
					<h3>${task.activityLabel}</h3>
					<p>
						${task.processLabel} - <fmt:formatDate value="${task.createdDate}" type="Date" />
					</p>
			</a></li>
		</ul>

	</c:forEach>
	</table>
</c:if>
