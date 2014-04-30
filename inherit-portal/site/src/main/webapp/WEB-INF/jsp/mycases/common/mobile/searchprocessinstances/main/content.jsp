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
 
 
<%@ include file="/WEB-INF/jspf/htmlTags.jspf"%>

<c:choose>
	<c:when test="${empty document}">
		<tag:pagenotfound />
	</c:when>
	<c:otherwise>
		<c:if test="${not empty document.title}">
			<hst:element var="headTitle" name="title">
				<c:out value="${document.title}" />
			</hst:element>
			<hst:headContribution keyHint="headTitle" element="${headTitle}" />
		</c:if>
		<hst:cmseditlink hippobean="${document}" />
		<h2>${document.title}</h2>
		<p>${document.summary}</p>
		<hst:html hippohtml="${document.html}" />
		
		<form id="searchPanelForm" action="${submitUri}" method="POST">
			<input type="hidden" name="page" value="${page}"/>
			<input type="hidden" name="pageSize" value="${pageSize}"/>
			<input type="hidden" name="sortBy" value="${sortBy}"/>
			<input type="hidden" name="sortOrder" value="${sortOrder}"/>
			<input type="search"  name="searchStr" value="${searchStr}"><br/>
			<input type="hidden" name="editablefilter" value="${editablefilter}"/>
			
			<c:choose>
			  <c:when test="${editablefilter eq 'false'}">
			  	<input type="hidden" name="filter" value="${filter}"/>
			  </c:when>
			  <c:otherwise>
			 <fieldset data-role="controlgroup">
 			         <legend>Filtrera på status:</legend>
				  <c:choose>		
				      <c:when test="${filter eq 'STARTED'}">	
					<input id="radio-choice-started" type="radio" name="filter" value="STARTED" checked/>
				      </c:when>
				      <c:otherwise>
				        <input id="radio-choice-started" type="radio" name="filter" value="STARTED"/>
				      </c:otherwise>
				  </c:choose>
				  <label for="radio-choice-started">P&aring;g&aring;ende</label>
				  <c:choose>		
				      <c:when test="${filter eq 'FINISHED'}">	
					<input id="radio-choice-finished" type="radio" name="filter" value="FINISHED" checked/> 
				      </c:when>
				      <c:otherwise>
				        <input id="radio-choice-finished" type="radio" name="filter" value="FINISHED"/> 
				      </c:otherwise>
				</c:choose>
				<label for="radio-choice-finished">Avslutade</label>
                             </fieldset>
		    	</c:otherwise>
		    </c:choose>
			
			<button id="search-btn" href="#">S&ouml;k</button>
		</form>
		
		<script type="text/javascript">
			  $jq("#search-btn").click(function(event) {
			   $jq("#searchPanelForm  > input[name=page]").prop("value", 1);
			  });
		</script>
       </div>		
       <div data-role="content">



<c:choose>
  <c:when test="${not empty searchResult and not empty searchResult.hits}">
        <ul data-role="listview" data-inset="true">
                <c:forEach var="item" items="${searchResult.hits}">
                        <li>
                        	<a href="../../processinstancedetail?processInstanceUuid=${item.processInstanceUuid}">
                                <h3>${item.processLabel}</h3>
                                <p>
                                        <fmt:message key="mycases.startDate.column.lbl"/>:&nbsp;<fmt:formatDate value="${item.startDate}" type="Date" dateStyle="short" timeStyle="short"/>
                                </p>
                                <p>
				<c:choose>             
				  <c:when test="${item.status == 1}">                                  
                                        <b><fmt:message key="mycases.processStatusPending"/></b><br/>
                                        <c:forEach var="activity" items="${item.activities}">
                                              ${activity.activityLabel}<br/>
                                        </c:forEach>
                                                       
			          </c:when>
				  <c:when test="${item.status == 2}">                                  
                                  	<fmt:message key="mycases.processStatusFinished"/>
  			          </c:when>
                                  <c:when test="${item.status == 3}">                                  
                                        <fmt:message key="mycases.processStatusCancelled"/>
  				  </c:when>
				  <c:when test="${item.status == 4}">                                  
                                        <fmt:message key="mycases.processStatusAborted"/>
  			          </c:when>
				  <c:otherwise>
                              
			          </c:otherwise>
                                 </c:choose>
&nbsp;<fmt:formatDate value="${item.endDate}" type="Date" dateStyle="short" timeStyle="short"/>
                                </p>
                             </a>
                        </li>
                </c:forEach>
        </ul>

  </c:when>
  <c:otherwise>
    <fmt:message key="mycases.noProcessInstance.lbl" />
  </c:otherwise>
</c:choose>


  </c:otherwise>
</c:choose>

