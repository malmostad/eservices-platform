<%@ include file="/WEB-INF/jspf/htmlTags.jspf"%>
<%--@elvariable id="document" type="se.inherit.portal.beans.NewsDocument"--%>

<c:choose>
	<c:when test="${empty document}">
		<tag:pagenotfound />
	</c:when>
	<c:otherwise>
		<c:if test="${not empty document.title}">
			<hst:element var="headTitle" name="title">
				<c:out value="${document.title}" />
			</hst:element>
			<hst:headContribution keyHint="headTitle" element="${headTitle}" />
		</c:if>
		<hst:cmseditlink hippobean="${document}" />
		<h2>${document.title}</h2>
		<p>${document.summary}</p>
		<hst:html hippohtml="${document.html}" />
		
		<form id="searchPanelForm" action="${submitUri}" method="POST">
			<input type="hidden" name="page" value="${page}"/>
			<input type="hidden" name="pageSize" value="${pageSize}"/>
			<input type="hidden" name="sortBy" value="${sortBy}"/>
			<input type="hidden" name="sortOrder" value="${sortOrder}"/>
			<input type="hidden"  name="searchStr" value="${searchStr}"><br/>
			<input type="hidden" name="editablefilter" value="${editablefilter}"/>
			<input type="hidden" name="filter" value="${filter}"/>
		</form>
		
		<table class="display dataTable" width="100%">
			<thead>
				<tr>
					<th><fmt:message key="mycases.process.column.lbl" /></th>
					<th><fmt:message key="mycases.status.column.lbl" /></th>
					<th><fmt:message key="mycases.startDate.column.lbl" /></th>
					<th><fmt:message key="mycases.endDate.column.lbl" /></th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${not empty searchResult and not empty searchResult.hits}">
						<c:forEach var="item" items="${searchResult.hits}">
							<tr>
								<td><a
									href="../../processinstancedetail?processInstanceUuid=${item.processInstanceUuid}">${item.processLabel}</a></td>
<c:choose>             
  <c:when test="${item.status == 1}">                                  
                                                       <td><fmt:message key="mycases.processStatusPending"/><br/>
                                                       <c:forEach var="activity" items="${item.activities}">
                                                          ${activity.activityLabel}<br/>
                                                       </c:forEach>
                                                       </td>
  </c:when>
  <c:when test="${item.status == 2}">                                  
                                                       <td><fmt:message key="mycases.processStatusFinished"/></td>
  </c:when>
  <c:when test="${item.status == 3}">                                  
                                                       <td><fmt:message key="mycases.processStatusCancelled"/></td>
  </c:when>
  <c:when test="${item.status == 4}">                                  
                                                       <td><fmt:message key="mycases.processStatusAborted"/></td>
  </c:when>
  <c:otherwise>
                               <td></td>
  </c:otherwise>
</c:choose>
								<td><fmt:formatDate value="${item.startDate}" type="Date" dateStyle="short" timeStyle="short"/></td>
								<td><fmt:formatDate value="${item.endDate}" type="Date" dateStyle="short" timeStyle="short"/></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="4"><fmt:message key="mycases.noProcessInstance.lbl" /></td>
						</tr>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
		<c:if test="${pageCount>1}">
			<div class="pagination">
			  <ul id="hitlist_pagination">
			  <c:if test="${page>1}">
			      <li><a href='${page-1}' rel='prev' class='prev_page hitlist_goto_page'>F&ouml;reg&aring;ende</a></li>
			  </c:if>
			  <c:forEach var="i" begin="${pageLinkLb}" end="${pageLinkUb}" step="1" varStatus="status">
			    <c:choose>
			                <c:when test="${i == page}">
			                        <li><span class="current">${i}</span></li>              
			                </c:when>
			                <c:otherwise>
			                         <li><a href="${i}" class="hitlist_goto_page">${i}</a></li>
			                </c:otherwise>
			        </c:choose>
			  </c:forEach>
			  <c:if test="${page<pageCount}">
			      <li><a href='${page+1}' rel='next' class='next_page hitlist_goto_page'>N&auml;sta</a></li>
			  </c:if>
			  </ul>
			</div>
		</c:if>
		
		<script type="text/javascript">
		          $(".hitlist_goto_page").click(function(event) {
		           event.preventDefault();
		           var hrefPage = $(this).attr("href");
		           
		           $("#searchPanelForm  > input[name=page]").prop("value", hrefPage);
		
		            $("#searchPanelForm").submit();
		          });
		</script>
		
	</c:otherwise>
</c:choose>


