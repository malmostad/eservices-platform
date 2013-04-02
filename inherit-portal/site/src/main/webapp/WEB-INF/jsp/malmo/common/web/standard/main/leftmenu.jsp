<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>
<%--@elvariable id="menu" type="org.hippoecm.hst.core.sitemenu.HstSiteMenu"--%>

<!--eri-no-index-->                     
<div>
  <div>
	<ul class="sub-menu-light">
	  <c:forEach var="item" items="${menu.siteMenuItems}">
      <tag:menuitem siteMenuItem="${item}"/>
	  </c:forEach>
	</ul>
  </div>
</div>
<!--/eri-no-index-->
