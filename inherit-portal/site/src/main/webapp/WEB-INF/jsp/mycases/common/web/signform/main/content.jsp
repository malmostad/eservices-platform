<%-- 
    Process Aware Web Application Platform 
 
    Copyright (C) 2011-2013 Inherit S AB 
 
    This program is free software: you can redistribute it and/or modify 
    it under the terms of the GNU Affero General Public License as published by 
    the Free Software Foundation, either version 3 of the License, or 
    (at your option) any later version. 
 
    This program is distributed in the hope that it will be useful, 
    but WITHOUT ANY WARRANTY; without even the implied warranty of 
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
    GNU Affero General Public License for more details. 
 
    You should have received a copy of the GNU Affero General Public License 
    along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 
    e-mail: info _at_ inherit.se 
    mail: Inherit S AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN 
    phone: +46 8 641 64 14 
 --%> 
 
<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>
<%--@elvariable id="document" type="org.inheritsource.portal.beans.TextDocument"--%>
s 
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
					
	<div class="row-fluid">
		<div class="span12">
			<form method="post" action="https://idp.test.malmo.se/wa/auth?authmech=e-id-signer">
			    Dokument: <a href="${pdfUrl}">${docNo}</a>
			    Kontrollsumma: ${pdfChecksum}
				<input type="hidden" name="text" value="${signText}"/>
				<input type="hidden" name="location" value="${responseUrl}"/>
				<input type="submit"/>
			</form>
		</div>
	</div>    
    
  </c:otherwise>  
</c:choose>
