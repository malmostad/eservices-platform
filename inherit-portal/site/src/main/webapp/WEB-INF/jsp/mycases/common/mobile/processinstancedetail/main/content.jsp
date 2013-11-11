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
 
<%@ include file="/WEB-INF/jspf/htmlTags.jspf"%>

<c:choose>
  <c:when test="${not empty processInstanceDetails}">
    <ul data-role="listview" data-inset="true" data-divider-theme="a">
    <c:if test="${not empty processInstanceDetails.pending}">
      <li data-role="list-divider"><fmt:message key="mycases.pendingActivities.lbl"/></li>
      <c:forEach var="pendingTask" items="${processInstanceDetails.pending}">
         <li>
           <h2>${pendingTask.activityLabel}</h2>
           <p class="ui-li-aside"><strong><fmt:formatDate value="${pendingTask.lastStateUpdate}" type="Both" dateStyle="short" timeStyle="short"/></strong></p>
         </li>
      </c:forEach>
    </c:if>


   <c:if test="${not empty timelineByDay}">
      <c:forEach var="dayEntry" items="${timelineByDay}">
      <li data-role="list-divider"><fmt:message key="mycases.performedActivities.lbl"/>&nbsp;<fmt:formatDate value="${dayEntry.key}" type="Date" dateStyle="long"/></li>

        <c:forEach var="logItem" items="${dayEntry.value}">

          <c:if test="${logItem.type != 5}"> <!-- 5=comment -->
            <li data-icon="info">
            <c:choose>
              <c:when test="${logItem.user.uuid == userInfo.uuid and not empty logItem.viewUrl}">
	           <a href="viewform?processActivityFormInstanceId=${logItem.processActivityFormInstanceId}">
              </c:when>
              <c:otherwise>
              </c:otherwise>           
           </c:choose>

           <h2>${logItem.briefDescription}</h2><p>${logItem.description}</p><p class="ui-li-aside"><strong><fmt:formatDate value="${logItem.timestamp}" type="Both" dateStyle="short" timeStyle="short"/></strong></p>
           
	   <c:choose>
             <c:when test="${logItem.user.uuid == userInfo.uuid}">
              </a>
             </c:when>
             <c:otherwise>
             </c:otherwise>
           </c:choose>		
 
           </li>

	 </c:if>
        </c:forEach>
      </c:forEach>
   </c:if>


        </ul>
  </c:when>
  <c:otherwise>
    <fmt:message key="mycases.noProcessInstanceDetails.lbl"/>
  </c:otherwise>
</c:choose>

<a data-role="button" data-rel="back" data-direction="reverse" data-icon="arrow-l"><fmt:message key="mycases.back.label"/></a>
