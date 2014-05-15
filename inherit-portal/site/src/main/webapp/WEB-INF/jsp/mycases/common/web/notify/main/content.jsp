<%-- == Motrice Copyright Notice == 
 
  Motrice Service Platform 
 
  Copyright (C) 2011-2014 Motrice AB 
 
  This program is free software: you can redistribute it and/or modify 
  it under the terms of the GNU Affero General Public License as published by 
  the Free Software Foundation, either version 3 of the License, or 
  (at your option) any later version. 
 
  This program is distributed in the hope that it will be useful, 
  but WITHOUT ANY WARRANTY; without even the implied warranty of 
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
  GNU Affero General Public License for more details. 
 
  You should have received a copy of the GNU Affero General Public License 
  along with this program. If not, see <http://www.gnu.org/licenses/>. 
 
  e-mail: info _at_ motrice.se 
  mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN 
  phone: +46 8 641 64 14 
 
--%> 
 
 
<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>
<%--@elvariable id="document" type="org.inheritsource.portal.beans.NewsDocument"--%>


	<div class="row-fluid">
		<div class="span12">


<c:choose>
  <c:when test="${empty document}">
    <tag:pagenotfound/>
  </c:when>
  <c:otherwise>
    <c:if test="${not empty document.title}">
      <hst:element var="headTitle" name="title">
        <c:out value="${document.title}"/>
      </hst:element>
      <hst:headContribution keyHint="headTitle" element="${headTitle}"/>
    </c:if>
    
    <c:if test="${not empty guide}">
		<h1>${guide.title}</h1>
		<p>${guide.summary}</p>
		<hst:html hippohtml="${guide.html}"/>
	</c:if>
				    
  </c:otherwise>  
</c:choose>

		    <c:if test="${not empty activity}">
		       <c:choose>
		         <c:when test="${activity.typeId eq 5}">
		          <c:if test="${not empty notifyItem}">
		          	<p> <fmt:message key="mycases.actdoc"/>&nbsp;
						<a href="${notifyItem.actUri}"><fmt:message key="mycases.actdocLink"/></a>
					</p>
		          </c:if>
			    		<form method="post" action="notify/confirm">
								<input type="hidden" name="document" value="${activity.instanceId}" />
								<input type="submit" value="<fmt:message key="mycases.notify.okbtn.label"/>"/>
						</form>
					
				</c:when>
			  	<c:otherwise>
			  	   <fmt:message key="mycases.noform.cannothandle"/>
			  	</c:otherwise>
			  </c:choose>
			</c:if>
		</div>
	</div>    

