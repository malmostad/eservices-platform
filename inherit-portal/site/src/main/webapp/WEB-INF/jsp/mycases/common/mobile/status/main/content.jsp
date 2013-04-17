<%@ include file="/WEB-INF/jspf/htmlTags.jspf"%>
<%--@elvariable id="document" type="se.inherit.portal.beans.NewsDocument"--%>

<c:if test="${not empty processInstances}">
        <ul data-role="listview">
                <c:forEach var="item" items="${processInstances}">
                        <li>
                        	<a href="../../processinstancedetail?processInstanceUuid=${item.processInstanceUuid}">
                                <h3>${item.processLabel}</h3>
                                <p>
                                        <fmt:message key="mycases.startDate.column.lbl"/>:&nbsp;<fmt:formatDate value="${item.startDate}" type="Date" dateStyle="short" timeStyle="short"/>
                                </p>
                                <p>
                                        <fmt:message key="mycases.status.column.lbl"/>:&nbsp;${item.status}&nbsp;<fmt:formatDate value="${item.endDate}" type="Date" dateStyle="short" timeStyle="short"/>
                                </p>
                             </a>
                        </li>
                </c:forEach>
        </ul>
</c:if>


