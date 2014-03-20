<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="hst" uri="http://www.hippoecm.org/jsp/hst/core" %>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>

<%@ attribute name="logItem" type="org.inheritsource.service.common.domain.TimelineItem" rtexprvalue="true" required="true"%>
<%@ attribute name="viewMode" required="true"%>
<!--  
	logItem is a timeline item
	viewMode values
	         full - view all task forms and which user that performed a task
	         limit - view my own task forms, hide which user that performed the task
-->

	<c:set var="orbeonform" value="${logItem.typeId eq 1}" />
	<c:set var="signform" value="${logItem.typeId eq 2 or logItem.typeId eq 3}" />
	<c:set var="payform" value="${logItem.typeId eq 4}" />
	<c:set var="notifyform" value="${logItem.typeId eq 5}" />
	<c:set var="noform" value="${logItem.typeId eq 6}" />
	
<li>
  <h4><fmt:formatDate value="${logItem.timestamp}" type="Both" dateStyle="short" timeStyle="short"/>&nbsp;${logItem.briefDescription}&nbsp;<c:if test="${viewMode eq full}">(${logItem.user})</c:if></h4>
  <span class="exp">+ visa mer...</span>
  <div class="panel komin-xform">
    <p>
    
    <c:choose>
    
    	<c:when test="${viewMode eq 'full'}">
			<c:choose>
				<c:when test="${not empty logItem.description}">
					<p>${logItem.description}</p>
				</c:when>
				<c:when test="${not empty logItem.actUri}">
				
					<c:choose>
						<c:when test="${signform eq true}">
							<p> <fmt:message key="mycases.signeddocument"/>&nbsp;
						  	<a href="${logItem.actUri}"><fmt:message key="mycases.signeddocumentlink"/></a>
							</p>
						</c:when>
						<c:when test="${payform eq true}">
							<p> <fmt:message key="mycases.receipt"/>&nbsp;
						  	<a href="${logItem.actUri}"><fmt:message key="mycases.receiptlink"/></a>
							</p>
						</c:when>
						<c:when test="${not empty logItem.viewUrlExternal and orbeonform eq true}">
							<!-- orbeon log item with act uri i.e. view both html form and link to act -->
							<iframe class="iframe-orbeon-panel" scrolling="no" frameborder="0" width="100%" height="100"></iframe>
							<p> <fmt:message key="mycases.actdoc"/>&nbsp;
						  	<a href="${logItem.actUri}"><fmt:message key="mycases.actdocLink"/></a>
							</p>
						</c:when>
						<c:otherwise>
							<p> <fmt:message key="mycases.actdoc"/>&nbsp;
						  	<a href="${logItem.actUri}"><fmt:message key="mycases.actdocLink"/></a>
							</p>									
						</c:otherwise>
					</c:choose>
				    
				</c:when>
				<c:when test="${not empty logItem.viewUrlExternal and orbeonform eq true}">
					<!-- orbeon log item -->
					<iframe class="iframe-orbeon-panel" scrolling="no" frameborder="0" width="100%" height="100"></iframe>
				</c:when>
				<c:otherwise>
					<fmt:message key="mycases.nomoredetails"/>
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<fmt:message key="mycases.activityPerformed"/>
		</c:otherwise>
	</c:choose>
	</p>
  </div>
  <c:if test="${viewMode eq 'full' and not empty logItem.viewUrlExternal and orbeonform eq true}">
  	<a class="view-url" href="${logItem.viewUrlExternal}"></a>
  </c:if>
</li>
					
