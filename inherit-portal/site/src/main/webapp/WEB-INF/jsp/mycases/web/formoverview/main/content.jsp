<%@ include file="/WEB-INF/jspf/htmlTags.jspf"%>
<%--@elvariable id="crPage" type="java.lang.Integer"--%>
<%--@elvariable id="info" type="se.inherit.portal.componentsinfo.GeneralListInfo"--%>
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
<table class="e-services">
  <thead>
    <tr>
      <th id="case-name"><fmt:message key="mycases.service.lbl"/></th>
      <th id="case-description" colspan="3"><fmt:message key="mycases.select.lbl"/></th>      
    </tr>
  </thead>
  <tbody>
    <tr>
    	<th colspan="4">TODO Katalognamn från JCR som namn på f&ouml;rvaltningen</th>
    </tr>

   <c:forEach var="item" items="${result.hippoBeans}">

		<tr>
	      <td headers="case-name">
	         ${item.title}
	      </td>
	      <td class="select">
	      	 <c:if test="${not empty item.pdfUrl}">
	      	   <a href="${item.pdfUrl}" title="öppnas i nytt fönster)" rel="external">PDF
	      	    <c:if test="${not empty pdfIcon and not empty pdfIcon.original}"> 
                    <hst:link var="pdfIconSrc" hippobean="${pdfIcon.original}"/>
                    <h1><img src="${pdfIconSrc}" width="${pdfIcon.original.width}" height="${pdfIcon.original.height}" />
                 </c:if>
	      	 </c:if>
	      </td>
	      <td>
	         <c:if test="${not empty item.wordUrl}">
	      	   <a href="${item.wordUrl}" title="öppnas i nytt fönster)" rel="external">Word
 				  <c:if test="${not empty wordIcon and not empty wordIcon.original}"> 
                    <hst:link var="wordIconSrc" hippobean="${wordIcon.original}"/>
                    <h1><img src="${wordIconSrc}" width="${wordIcon.original.width}" height="${wordIcon.original.height}" />
                  </c:if>	      	   
               </a>
	      	 </c:if>
	      </td>
	      <td>
	         <c:if test="${not empty item.formPath}">
	            <hst:link var="link" hippobean="${item}" />
				<a href="${link}">e-tj&auml;nst</a>
			 </c:if>
	      </td>
	    </tr>
        <!-- tr>
          <td colspan=4>
             <p>${item.summary}</p>
          </td>
        </tr-->
	</c:forEach>          
</tbody>
</table>
</c:otherwise>
</c:choose>
