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

  <nav class="breadcrumbs">
    <ol>
	<li><a href='https://webapps06.malmo.se/dashboard/'><span class="glyphicon glyphicon-home"></span>KOMIN Min sida</a></li>
    <li><a href="/site/komin"><i class="fa fa-chevron-right"></i>&nbsp;E-tjänster</a></li>
	<c:if test="${not empty breadcrumb}"> 
	  <c:forEach var="item" items="${breadcrumb.items}">	    
	       <li><a href='<hst:link link="${item.link}"/>'><i class="fa fa-chevron-right"></i>&nbsp;<c:out value="${item.title}"/></a></li>
	  </c:forEach>
	</c:if>
    </ol>
  </nav>

<c:choose>
  <c:when test="${empty user}">
	Ej inloggad  <a href="/site/login/form">Logga in</a>
  </c:when>
  <c:otherwise>
	Inloggad som: ${user.label} <a href="/site/logout">Logga ut</a>
	<input id="motrice-auth-user-uuid" type="hidden" name="motrice-auth-user-uuid" value="${user.uuid}"/>
  </c:otherwise>
</c:choose>
