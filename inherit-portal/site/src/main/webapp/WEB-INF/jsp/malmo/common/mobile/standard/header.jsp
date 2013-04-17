<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>

<c:choose>
  <c:when test="${empty user}">
	Ej inloggad  <a href="/site/login/form">Logga in</a>
  </c:when>
  <c:otherwise>
	Inloggad som: ${user.label} <a href="/site/logout">Logga ut</a>
  </c:otherwise>
</c:choose>

