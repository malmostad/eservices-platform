<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>

<c:choose>
  <c:when test="${empty user}">
	Ej inloggad  <a href="/site/login/form">Logga in</a>
  </c:when>
  <c:otherwise>
	Inloggad som: ${user.label} <a href="/site/login/logout">Logga ut</a>
  </c:otherwise>
</c:choose>

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