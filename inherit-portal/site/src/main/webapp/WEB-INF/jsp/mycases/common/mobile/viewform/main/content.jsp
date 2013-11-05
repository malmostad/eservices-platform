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
 
<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>

<c:choose>
  <c:when test="${empty logItem}">
    <p>Denna aktivitet &auml;r inte utf&ouml;rd och kan inte visas.</p>
  </c:when>
  <c:otherwise>
    <c:if test="${not empty logItem and not empty activityLabel.activityLabel}">
      <hst:element var="headTitle" name="title">
        <c:out value="${activityLabel.activityLabel}"/>
      </hst:element>
      <hst:headContribution keyHint="headTitle" element="${headTitle}"/>
    </c:if>

    <c:choose>		     
    	<c:when test="${fn:startsWith(logItem.formUrl, '/docbox/doc/ref')}">	
	   <p> <fmt:message key="mycases.signeddocument"/>&nbsp;<a data-ajax="false" href="${logItem.formUrl}"><fmt:message key="mycases.signeddocumentlink"/></a>
	   </p>
       </c:when>
       <c:when test="${not fn:startsWith(logItem.formUrl, 'none/') and not fn:startsWith(logItem.formUrl, '/docbox/doc/ref')}">
          <script type="text/javascript" charset="utf-8">
	     jQuery.noConflict();
	     var $j = jQuery;
	     $j(document).ready(function () {
	       $j("#xform").load("<fmt:message key="orbeonbase.portal.url"/>${logItem.formUrl}orbeon-embeddable=true", function(data) {
	         if (typeof ORBEON != "undefined") { 
	           if (!document.all) {
	             ORBEON.xforms.Init.document(); 
	           } 
	         } 
	       }); 
	     });
  	 </script>
	 <div id="xform">Loading form...please wait...</div>
       </c:when>
       <c:otherwise>
         <p> <fmt:message key="mycases.nomoredetails"/></p>
       </c:otherwise>
    </c:choose>
    
  </c:otherwise>  
</c:choose>
 
<a data-role="button" data-rel="back" data-direction="reverse" data-icon="arrow-l"><fmt:message key="mycases.back.label"/></a>