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
<%--@elvariable id="menu" type="org.hippoecm.hst.core.sitemenu.HstSiteMenu"--%>

<c:if test="${not empty topnavigation and not empty topnavigation.items}"> 
<section class="box" contextmenu="nav-menu" id="nav-box">
  <h1 class="box-title">Navigering</h1>
  <ul class="box-content">
   <c:forEach var="item" items="${topnavigation.items}">	    
    <li><a href='<hst:link link="${item.link}"/>'><c:out value="${item.title}"/></a></li>
   </c:forEach>
  </ul>
</section>
</c:if>

<c:if test="${not empty subnavigation and not empty subnavigation.items}"> 
<section class="box" contextmenu="nav-menu" id="nav-box">
  <h1 class="box-title">H&auml;r hittar du</h1>
  <ul class="box-content">
   <c:forEach var="item" items="${subnavigation.items}">	    
    <li><a href='<hst:link link="${item.link}"/>'><c:out value="${item.title}"/></a></li>
   </c:forEach>
  </ul>
</section>
</c:if>
