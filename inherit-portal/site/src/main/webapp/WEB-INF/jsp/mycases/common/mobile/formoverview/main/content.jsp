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

<ul id="services-overview" data-role="listview">
    
                <c:forEach var="item" items="${result.hippoBeans}">
                        
                <hst:link var="link" hippobean="${item}" />     
                
                <li><a href="${link}">
                        <h3>${item.title}</h3>
                        </a>
                        </li>


                                
                        </c:forEach>
</ul>
                        <!--if there are pages on the request, they will be printed by the tag:pages -->
                        <tag:pages pages="${pages}" page="${page}" />
        </c:otherwise>
</c:choose>


