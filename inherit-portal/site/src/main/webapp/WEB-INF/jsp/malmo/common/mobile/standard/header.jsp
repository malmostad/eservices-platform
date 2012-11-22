<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>

<c:choose>
  <c:when test="${empty userName}">
	Ej inloggad  <a href="/site/login/form">Logga in</a>
  </c:when>
  <c:when test="${userName eq 'eva_extern'}">
	Inloggad som: ${userName} (automatiskt för test) <a href="/site/login/form">Logga in</a>
  </c:when>
  <c:otherwise>
	Inloggad som: ${userName} <a href="/site/login/logout">Logga ut</a>
  </c:otherwise>
</c:choose>

