<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>
<%--@elvariable id="document" type="se.inherit.portal.beans.NewsDocument"--%>

<c:if test="${not empty bonitaFormUrl}">
	<iframe id="bonita_form" frameborder="0" scrolling="auto" src="${bonitaFormUrl}" width="100%"> </iframe>
</c:if>
