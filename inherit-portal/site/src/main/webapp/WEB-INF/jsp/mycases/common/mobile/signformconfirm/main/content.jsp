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
    
   
  </c:otherwise>  
</c:choose>

	<div class="row-fluid">
			<div class="span12">
			
		    <c:if test="${not empty guide}">
				<h1>${guide.title}</h1>
				<p>${guide.summary}</p>
				<hst:html hippohtml="${guide.html}"/>
			</c:if>		
 
 
 <c:choose>
				  <c:when test="${empty pdfUrl or empty docNo}">
				  	<fmt:message key="mycases.noformtoload.lbl"/>
				  </c:when>
				  <c:otherwise>
						<table>
						  <thead>
						    <tr>
						      <th>Signerade dokument</th>
						    </tr>
						  </thead>
						  <tbody>
						    <tr>
						    	<td><a href="${pdfUrl}">${docNo}</a></td>
						    </tr>
						  </tbody>
						</table>		
				  </c:otherwise>
				</c:choose>
				
				
				<c:if test="${not empty nextTask}">
	    		<p>
	    			<fmt:message key="mycases.nextactivity.lbl"/>&nbsp; <a href="../${nextTask.editFormUrl}">${nextTask.activityLabel}</a>&nbsp;<fmt:message key="mycases.in"/>&nbsp; ${nextTask.processLabel}. 
	    		</p>
    		    </c:if>			    

			</div>
	</div>