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
<%--@elvariable id="info" type="org.inheritsource.portal.componentsinfo.ListInfo"--%>
<%--@elvariable id="result" type="org.hippoecm.hst.content.beans.query.HstQueryResult"--%>

<c:choose>
  <c:when test="${empty info}">
    <tag:pagenotfound/>
  </c:when>
  <c:otherwise>
    <div class=${info.cssClass}>
      <p>${info.title}</p>
    
      <ul>
        <c:forEach var="item" items="${result.hippoBeans}" varStatus="counter">
          <c:if test="${counter.index == 0}">
            <hst:link var="link" hippobean="${item}"/>
            <li style="background-color:${info.bgColor};">
              <div>
                <c:if test="${hst:isReadable(item, 'image.thumbnail')}">
                  <hst:link var="img" hippobean="${item.image.thumbnail}"/>
                  <div style="float:left;margin-right:10px;">
                    <img src="${img}" title="${item.image.fileName}"
                      alt="${item.image.fileName}"/>
                  </div>
                </c:if>
                <div>
                  <p>
                    <c:if test="${hst:isReadable(item, 'date.time')}">
                      <fmt:formatDate value="${item.date.time}" type="Date" pattern="MMMM d, yyyy"/> -
                    </c:if>
                    <a href="${link}">${item.title}</a>
                  </p>
                  <p>${item.summary}</p>
                </div>
              </div>
            </li>
          </c:if>
        </c:forEach>
      </ul>
    
      <ul>
        <c:forEach var="item" items="${result.hippoBeans}" varStatus="counter">
          <c:if test="${counter.index > 0}">
            <hst:link var="link" hippobean="${item}"/>
            <li style="background-color:${info.bgColor};">
              <c:if test="${hst:isReadable(item, 'date')}">
                <fmt:formatDate value="${item.date.time}" type="Date" pattern="MMMM d, yyyy"/> -
              </c:if>
              <a href="${link}">${item.title}</a>
            </li>
          </c:if>
        </c:forEach>
    
      </ul>
    </div>
  </c:otherwise>
</c:choose>
