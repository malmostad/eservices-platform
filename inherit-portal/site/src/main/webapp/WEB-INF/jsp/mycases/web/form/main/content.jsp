<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>
<%--@elvariable id="document" type="se.inherit.portal.beans.NewsDocument"--%>

<c:choose>
  <c:when test="${empty document}">
    <tag:pagenotfound/>
  </c:when>
  <c:otherwise>
    <c:if test="${not empty document.title}">
      <hst:element var="headTitle" name="title">
        <c:out value="${document.title}"/>
      </hst:element>
      <hst:headContribution keyHint="headTitle" element="${headTitle}"/>
    </c:if>


	<script type="text/javascript" charset="utf-8">
		        jQuery.noConflict();
		        var $j = jQuery;

		        $j(document).ready(function () {
		             $j("#xform").load("<fmt:message key="orbeonbase.portal.url"/>${formUrl}", function(data) {
		                if (typeof ORBEON != "undefined") { 
		                    if (!document.all) {
		                        ORBEON.xforms.Init.document(); 
		                    } 
		                } 
		        	    }); 
					});
				
				
				</script>
				
	<div class="row-fluid">
		<div class="span12">
    		<div id="xform">Loading form...please wait...</div>
		</div>
	</div>    
    
  </c:otherwise>  
</c:choose>

<c:choose>
  <c:when test="${empty processInstanceDetails}">
  	Ingen historia
  </c:when>
  <c:otherwise>
    <h1>${processInstanceDetails.processInstanceUuid}</h1>
  </c:otherwise>
 </c:choose>
