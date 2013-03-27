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

	
	<c:if test="${not empty activity}">
    	<script type="text/javascript" charset="utf-8">
		        jQuery.noConflict();
		        var $j = jQuery;
		        $j(document).ready(function () {
		             $j("#xform").load("<fmt:message key="orbeonbase.portal.url"/>${activity.formUrl}", function(data) {
		                if (typeof ORBEON != "undefined") { 
		                    if (!document.all) {
		                        ORBEON.xforms.Init.document(); 
		                    } 
		                } 
		        	    }); 
					});
		</script>
    </c:if>
    
    <c:if test="${not empty guide}">
		<h1>${guide.title}</h1>
		<p>${guide.summary}</p>
		<hst:html hippohtml="${guide.html}"/>
	</c:if>
				
	<div class="row-fluid">
		<div class="span12">
    		<div id="xform" class="public-xform">Loading form...please wait...</div>
		</div>
	</div>    
    
  </c:otherwise>  
</c:choose>
