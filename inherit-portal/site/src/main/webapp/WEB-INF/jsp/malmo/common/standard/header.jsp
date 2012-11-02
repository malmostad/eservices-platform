<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>

<c:if test="${not empty mastHead}">
${mastHead}
</c:if>

<!-- removed search field because there is a search in masthead, how to integrate??? 
<fmt:message var="submitText" key="search.submit.text"/>
<hst:link var="link" path="/search"/>
<form action="${link}" method="POST">
 <input type="text" name="query"/>
 <input type="submit" value="${submitText}"/>
</form>
 -->