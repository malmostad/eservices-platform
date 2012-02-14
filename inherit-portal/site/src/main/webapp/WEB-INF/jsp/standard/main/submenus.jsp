<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<%@ taglib uri="http://www.hippoecm.org/jsp/hst/core" prefix='hst'%>
<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>
<%--@elvariable id="menu" type="org.hippoecm.hst.core.sitemenu.HstSiteMenu"--%>


  <c:forEach var="item" items="${menu.siteMenuItems}">
          <c:if test="${item.expanded and not empty item.childMenuItems}">
          <ul class="sitenav_submenu">
              <c:forEach var="subitem" items="${item.childMenuItems}">
                  <c:choose >
                  <c:when test="${subitem.selected}">
                      <li class="selected">
                      <b>${subitem.name}</b>
                      </li>
                  </c:when>
                  <c:otherwise>
                      <hst:link var="link" link="${subitem.hstLink}"/>
                      <li>
                      <c:if test="${empty link}">
                        <c:set var="link" value="${subitem.externalLink}"/>
                     </c:if>
                      <a href="${link}">
                          ${subitem.name}
                      </a>
                      </li>
                  </c:otherwise>
                  </c:choose>
                      <c:if test="${subitem.expanded and not empty subitem.childMenuItems}">
                      <ul>
                      <c:forEach var="subsubitem" items="${subitem.childMenuItems}">
                          <c:choose >
                          <c:when test="${subsubitem.selected}">
                              <li class="selected">
                              <b>${subsubitem.name}</b>
                              </li>
                          </c:when>
                          <c:otherwise>
                              <hst:link var="link" link="${subsubitem.hstLink}"/>
                              <li>
                              <c:if test="${empty link}">
                                <c:set var="link" value="${subsubitem.externalLink}"/>
                              </c:if>
                              <a href="${link}">
                                  ${subsubitem.name}
                              </a>
                              </li>
                          </c:otherwise>
                          </c:choose>
                      </c:forEach>
                      </ul>
                      </c:if>
              </c:forEach>
          </ul>
          </c:if>
  </c:forEach>
