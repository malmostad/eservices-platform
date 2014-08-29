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
 
 <aside>
	<section class="box" contextmenu="task-guide-menu" id="task-guide">
	  <h1 class="box-title"><fmt:message key="handlaggningsguide.lbl" /></h1>
	  <div class="box-instructions">
	    <p><fmt:message key="handlaggningsguide.instructions" /></p>
	  </div>
	  <div class="box-content body-copy">
	  	<c:if test="${not empty guide}">
		    <h1>${guide.title}</h1>
			<p>${guide.summary}</p>
			<hst:html hippohtml="${guide.html}" />
		</c:if>
	  </div>
	</section>

	<section class="box" contextmenu="workflow-menu" id="workflow-box">
	  <h1 class="box-title"><fmt:message key="mycases.workflowoverview.lbl" /></h1>
	  <div class="box-instructions">
	    <p>TODO HELP!</p>
	  </div>
	  <div class="box-content body-copy motrice-activity-workflow">
	     <input type="hidden" name="motrice-activity-instance-uuid" value="${activity.activityInstanceUuid}"/>
						
	    <p  class="motrice-activity-priority">
							<label><fmt:message key="mycases.priority.lbl" /></label> <select
								class="motrice-activity-priority-select">
								<option value="0">
									<fmt:message key="mycases.prioritynormal.lbl" />
								</option>
								<option value="1">
									<fmt:message key="mycases.priorityhigh.lbl" />
								</option>
								<option value="2">
									<fmt:message key="mycases.priorityurgent.lbl" />
								</option>
							</select>
						</p>
						<p class="motrice-activity-candidates">
						  Loading candidates...<button id="add-candidates" class="btn btn-primary motrice-add-candidates"><fmt:message	key="mycases.editcandidates.lbl" /><input type="hidden" name="motrice-activity-instance-uuid" value="${activity.activityInstanceUuid}"/></button>
						</p>
						<p><button class="btn btn-danger motrice-unassign-user"><fmt:message key="mycases.unassign.lbl"/><input type="hidden" name="motrice-unassign-user" value="${user.uuid}"/><input type="hidden" name="motrice-activity-instance-uuid" value="${activity.activityInstanceUuid}"/></button></p>
						<p><button class="btn btn-primary motrice-assign-to"><fmt:message key="mycases.assigntome.lbl"/><input type="hidden" name="motrice-assign-to" value="${user.uuid}"/><input type="hidden" name="motrice-activity-instance-uuid" value="${activity.activityInstanceUuid}"/></button></p>
	  </div>
	  <a href="#" class="toggle-instructions" title="Show instructions">?</a>
	  <div class="dropdown box-menu pull-right">
	    <a class="dropdown-toggle" data-toggle="dropdown" href="#" id="feeds-news-menu" role="button" title="Adapt this box">
	      <span class="icon-caret-down icon-large"></span>
	    </a>
	    <menu aria-labelledby="feeds-news-menu" class="dropdown-menu" role="menu">
	      <li><a href="#">TODO</a></li>
	      <li><a href="#" data-confirm="Reset all feeds?" data-method="put">Reset feeds</a></li>
	      <li><a href="#">Configure...</a></li>
	    </menu>
	  </div>
	</section>

</aside>
