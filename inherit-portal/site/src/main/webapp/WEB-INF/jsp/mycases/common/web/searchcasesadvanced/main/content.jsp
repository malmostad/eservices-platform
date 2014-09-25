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
<%--@elvariable id="document" type="org.inheritsource.portal.beans.NewsDocument"--%>

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

<form method="post" id="searchPanelForm" class="basic">
  <p class="help-block">
    Some instructions to the form may, or may not, be necessary. Lorem lipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod
    tempor.
  </p>

  <div class="form-group">
    <label for="person-name" class="control-label">Started By user:</label>
    <div class="controls">
      <input type="text" id="searchStr" name="searchStr"   value="${searchStr}" class="form-control" placeholder=""/>
    </div>
  </div>
  <div class="form-group">
    <label for="involvedUserId" class="control-label">Involved user:</label>
    <div class="controls">
      <input type="text" id="involvedUserId" name="involvedUserId"   value="${involvedUserId}" class="form-control" placeholder=""/>
    </div>

<div class="input-group date" id="start-date">
  <input type="text" class="form-control">
  <span class="input-group-addon">
    <span class="glyphicon glyphicon-calendar"></span>
  </span>
</div>
<%-- 
--%>  
  <div class="form-group">
    <label for="startDate" class="control-label">Start date:</label>
    <div class="controls">
      <input type="text" id="startDate" name="startDate"   value="${startDate}" class="form-control" placeholder=""/>
    </div>
  </div>
  <div class="form-group">
    <label for="tolDays" class="control-label">Tolerance days:</label>
    <div class="controls">
      <input type="text" id="tolDays" name="tolDays"   value="${tolDays}" class="form-control" placeholder=""/>
    </div>
  </div>	
		<c:choose>
			  <c:when test="${editablefilter eq 'false'}">
			  	<input type="hidden" name="filter" value="${filter}"/>
			  </c:when>
			  <c:otherwise>
					<c:choose>		
				      <c:when test="${filter eq 'STARTED'}">	
						<input type="radio" name="filter" value="STARTED" checked/>P&aring;g&aring;ende<br/>
					  </c:when>
					  <c:otherwise>
					    <input type="radio" name="filter" value="STARTED"/>P&aring;g&aring;ende<br/>
					  </c:otherwise>
					</c:choose>
					<c:choose>		
				      <c:when test="${filter eq 'FINISHED'}">	
						<input type="radio" name="filter" value="FINISHED" checked/> Avslutade<br/>
					  </c:when>
					  <c:otherwise>
					    <input type="radio" name="filter" value="FINISHED"/> Avslutade<br/>
					  </c:otherwise>
					</c:choose>
		    	</c:otherwise>
		    </c:choose>
	
			<button id="search-btn" href="#">S&ouml;k</button>
</form>

		<table class="display dataTable" width="100%">
			<thead>
				<tr>
					<th><fmt:message key="mycases.process.column.lbl" /></th>
					<th><fmt:message key="mycases.status.column.lbl" /></th>
					<th><fmt:message key="mycases.startDate.column.lbl" /></th>
					<th><fmt:message key="mycases.endDate.column.lbl" /></th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${not empty searchResult and not empty searchResult.hits}">
						<c:forEach var="item" items="${searchResult.hits}">
							<tr>
								<td><a
									href="../../processinstancedetail?processInstanceUuid=${item.processInstanceUuid}">${item.processLabel}</a></td>
<c:choose>             
  <c:when test="${item.status == 1}">                                  
                                                       <td><fmt:message key="mycases.processStatusPending"/><br/>
                                                       <c:forEach var="activity" items="${item.activities}">
                                                          ${activity.activityLabel}<br/>
                                                       </c:forEach>
                                                       </td>
  </c:when>
  <c:when test="${item.status == 2}">                                  
                                                       <td><fmt:message key="mycases.processStatusFinished"/></td>
  </c:when>
  <c:when test="${item.status == 3}">                                  
                                                       <td><fmt:message key="mycases.processStatusCancelled"/></td>
  </c:when>
  <c:when test="${item.status == 4}">                                  
                                                       <td><fmt:message key="mycases.processStatusAborted"/></td>
  </c:when>
  <c:otherwise>
                               <td></td>
  </c:otherwise>
</c:choose>
								<td><fmt:formatDate value="${item.startDate}" type="Date" dateStyle="short" timeStyle="short"/></td>
								<td><fmt:formatDate value="${item.endDate}" type="Date" dateStyle="short" timeStyle="short"/></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="4"><fmt:message key="mycases.noProcessInstance.lbl" /></td>
						</tr>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
		
		<c:if test="${pageCount>1}">
			<div class="pagination">
			  <ul id="hitlist_pagination">
			  <c:if test="${page>1}">
			      <li><a href='${page-1}' rel='prev' class='prev_page hitlist_goto_page'>F&ouml;reg&aring;ende</a></li>
			  </c:if>
			  <c:forEach var="i" begin="${pageLinkLb}" end="${pageLinkUb}" step="1" varStatus="status">
			    <c:choose>
			                <c:when test="${i == page}">
			                        <li><span class="current">${i}</span></li>              
			                </c:when>
			                <c:otherwise>
			                         <li><a href="${i}" class="hitlist_goto_page">${i}</a></li>
			                </c:otherwise>
			        </c:choose>
			  </c:forEach>
			  <c:if test="${page<pageCount}">
			      <li><a href='${page+1}' rel='next' class='next_page hitlist_goto_page'>N&auml;sta</a></li>
			  </c:if>
			  </ul>
			</div>
		</c:if>
		
		
	</c:otherwise>
</c:choose>






