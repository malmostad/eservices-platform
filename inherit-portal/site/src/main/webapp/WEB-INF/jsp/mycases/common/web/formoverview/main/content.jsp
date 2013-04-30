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
    mail: Inherit S AB, LÃ¥ngsjÃ¶vÃ¤gen 8, SE-131 33 NACKA, SWEDEN 
    phone: +46 8 641 64 14 
 --%> 
 
<%@ include file="/WEB-INF/jspf/htmlTags.jspf"%>
<%--@elvariable id="crPage" type="java.lang.Integer"--%>
<%--@elvariable id="info" type="org.inheritsource.portal.componentsinfo.GeneralListInfo"--%>
<%--@elvariable id="pages" type="java.util.Collection<java.lang.Integer>"--%>
<%--@elvariable id="query" type="java.lang.String"--%>
<%--@elvariable id="result" type="org.hippoecm.hst.content.beans.query.HstQueryResult"--%>

<c:choose>
	<c:when test="${empty info}">
		<tag:pagenotfound />
	</c:when>
	<c:otherwise>
		<c:if test="${not empty info.title}">
			<hst:element var="headTitle" name="title">
				<c:out value="${info.title}" />
			</hst:element>
			<hst:headContribution keyHint="headTitle" element="${headTitle}" />
		</c:if>
		
<div id="tab-element-container">
    <ul>
       <li><a href="#tab-element-1">Blanketter & tj&auml;nster</a></li>
       <li><a href="#tab-element-2">Hj&auml;lp & information</a></li>
    </ul>
	<div id="tab-element-1">
		<div class="tab-content">
		
			<table class="e-services">
			  <thead>
			    <tr>
			      <th id="case-name"><fmt:message key="mycases.service.lbl"/></th>
			      <th id="case-description" colspan="3"><fmt:message key="mycases.select.lbl"/></th>      
			    </tr>
			  </thead>
			  <tbody>
			
			   <c:forEach var="item" items="${result.hippoBeans}">
			
					<tr>
				      <td headers="case-name">
				         ${item.title}
				      </td>
				      <td class="select">
				      	 <c:if test="${not empty item.pdfUrl}">
				      	   <a href="${item.pdfUrl}" title="öppnas i nytt fönster)" rel="external">PDF
				      	    <c:if test="${not empty pdfIcon and not empty pdfIcon.original}"> 
			                    <hst:link var="pdfIconSrc" hippobean="${pdfIcon.original}"/>
			                    <h1><img src="${pdfIconSrc}" width="${pdfIcon.original.width}" height="${pdfIcon.original.height}" />
			                 </c:if>
				      	 </c:if>
				      </td>
				      <td>
				         <c:if test="${not empty item.wordUrl}">
				      	   <a href="${item.wordUrl}" title="öppnas i nytt fönster)" rel="external">Word
			 				  <c:if test="${not empty wordIcon and not empty wordIcon.original}"> 
			                    <hst:link var="wordIconSrc" hippobean="${wordIcon.original}"/>
			                    <h1><img src="${wordIconSrc}" width="${wordIcon.original.width}" height="${wordIcon.original.height}" />
			                  </c:if>	      	   
			               </a>
				      	 </c:if>
				      </td>
				      <td>
				         <c:if test="${not empty item.formPath}">
				            <hst:link var="link" hippobean="${item}" />
							<a href="${link}">e-tj&auml;nst</a>
						 </c:if>
				      </td>
				    </tr>
			        <!-- tr>
			          <td colspan=4>
			             <p>${item.summary}</p>
			          </td>
			        </tr-->
				</c:forEach>          
			</tbody>
			</table>
			
		</div>
	</div>

	<div id="tab-element-2">
		<div class="tab-content">
		
		</div>
	</div>

</div>

</c:otherwise>
</c:choose>
