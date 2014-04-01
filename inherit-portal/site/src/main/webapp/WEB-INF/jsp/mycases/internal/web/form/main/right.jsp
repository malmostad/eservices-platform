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
 
<%@ include file="/WEB-INF/jspf/htmlTags.jspf"%>
<!-- Workflow add candidate -->
<div>
	<div id="svid10_2842d8c1129693185ff800017435">
		<!-- Page content starts here -->
		<div class="pagecontent" id="svid94_1d8b012512b3897f473800038727">
			<div id="Hogerspalt">
				<!-- HÃ¶gerspalt -->
			</div>
			<div id="svid10_1d8b012512b3897f473800038728">
				<div id="Text-1">
					<!-- Text -->
				</div>
				<div id="h-Tips">
					<!-- Tips! -->
				</div>
				<h2>
					<fmt:message key="mycases.workflowoverview.lbl" />
				</h2>
			</div>
			<div class="tips">
				<div>
					<div id="svid12_1d8b012512b3897f473800038729">
						<div id="Text2">
							<!-- Text 2 -->
						</div>
						<p>
							<label><fmt:message key="mycases.priority.lbl" /></label> <select
								id="activity-priority">
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
						<p>
							<span id="activity-candidates">Loading candidates...</span> 
							  <!-- removed edit candidates temporary... a href="#" id="edit-candidates"><fmt:message	key="mycases.editcandidates.lbl" /></a--> 
							  <a href="#" id="unassign"><fmt:message key="mycases.unassign.lbl" /></a>
						</p>
						<p>
							<a href="#" id="assign-to-me"><fmt:message
									key="mycases.assigntome.lbl" /></a>
						</p>
						<p>
							<a href="#" id="add-comment">Kommentera ärendet</a>
						</p>
					</div>
				</div>
			</div>
			<div id="svid12_1d8b012512b3897f473800038730">
				<div id="Text3-0">
					<!-- Text 3 -->
				</div>
				<div id="h-Omduvill">
					<!-- Om du vill: -->
				</div>
				<h2>
					<fmt:message key="mycases.tags.lbl" />
				</h2>

				<form>
					<ul id="myTags">
				     <c:if test="${not empty tags}">
			           <c:forEach var="tag" items="${tags}">
						<li>${tag.value}</li>
					   </c:forEach>
					 </c:if>
					</ul>
				</form>


			</div>

			<c:if test="${not empty guide}">

				<div id="svid12_1d8b012512b3897f473800038732">
					<div id="Text4-0">
						<!-- Text 4 -->
					</div>
					<div id="h-Verktygampsystem">
						<!-- Verktyg &amp; system -->
					</div>
					<h2>Handl&auml;ggningsguide</h2>
					<h1>${guide.title}</h1>
					<p>${guide.summary}</p>
					<hst:html hippohtml="${guide.html}" />

				</div>
			</c:if>
		</div>
		<!-- Page content stops here -->
	</div>
	<div id="svid10_2842d8c1129693185ff800018821">
		<!-- Page content starts here -->
		<div class="pagecontent" id="svid94_1d8b012512b3897f473800038737">
			<div id="hhutgar2011">
				<!-- hh-utgÃ¥r-2011 -->
			</div>
		</div>
		<!-- Page content stops here -->
	</div>
</div>

<!-- Modal dialog to comment the case-->
<div id="dialog-comment-case"
	title="<fmt:message key="mycases.addcomment.lbl"/>">
	<p class="validateTips">
		<fmt:message key="mycases.addcommentdialoghint.lbl" />
	</p>
	<form action="/site/restservices/site-ajax/addComment"
		id="addCommentForm" class="siteAjaxForm">
		<input type="hidden" name="activityInstanceUuid"
			value="${activity.activityInstanceUuid}" /> <input type="text"
			name="comment"
			placeholder="<fmt:message key="mycases.message.column.lbl"/>"
			class="text ui-widget-content ui-corner-all" />
	</form>
</div>

<!-- Modal dialog to edit candidates to perform the activity-->
<div id="dialog-edit-candidates"
	title="<fmt:message key="mycases.editcandidatesdlg.title"/>">
	<p class="validateTips">
		<fmt:message key="mycases.editcandidatesdlg.hint" />
	</p>

	<h3>
		<fmt:message key="mycases.candidates.column.lbl" />
	</h3>
	<table>
		<thead>
			<tr>
				<th><fmt:message key="mycases.name.lbl" /></th>
				<th><fmt:message key="mycases.username.lbl" /></th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>Bj&ouml;rn Molin</td>
				<td>bjmo</td>
				<td><button class="remove-candidate-btn" href="#"></button></td>
			</tr>
		</tbody>
	</table>
	<h3>SÃ¶k anvÃ¤ndare</h3>
	<form>
		<fieldset>
			<label for="name"><fmt:message key="mycases.name.lbl" /></label> <input
				type="text" name="name" id="name"
				class="text ui-widget-content ui-corner-all" /> <label for="email"><fmt:message
					key="mycases.email.lbl" /></label> <input type="text" name="email"
				id="email" value="" class="text ui-widget-content ui-corner-all" />
			<button id="search-button" class="search-button">
				<fmt:message key="mycases.search.lbl" />
			</button>

			<table>
				<thead>
					<tr>
						<th><fmt:message key="mycases.name.lbl" /></th>
						<th><fmt:message key="mycases.username.lbl" /></th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>John Doe</td>
						<td>john</td>
						<td><button class="add-candidate-btn" href="#"></button></td>
					</tr>
					<tr>
						<td>Jack</td>
						<td>jack</td>
						<td><button class="add-candidate-btn" href="#"></button></td>
					</tr>
					<tr>
						<td>James</td>
						<td>james</td>
						<td><button class="add-candidate-btn" href="#"></button></td>
					</tr>
				</tbody>
			</table>
		</fieldset>
	</form>

	<form action="/site/restservices/site-ajax/assignTask"
		id="addCandidate" class="siteAjaxForm">
		<input type="hidden" name="activityInstanceUuid"
			value="${activity.activityInstanceUuid}" /> <input type="hidden"
			name="action" value="addcandidate" /> <input type="hidden" size="8"
			name="targetUserId" placeholder="" />
	</form>

</div>


<form action="/site/restservices/site-ajax/assignTask" id="assignToMe"
	class="siteAjaxForm">
	<input type="hidden" name="activityInstanceUuid"
		value="${activity.activityInstanceUuid}" /> <input type="hidden"
		name="action" value="assign" /> <input type="hidden" size="8"
		name="targetUserId" value="${user.uuid}" />
</form>
<form action="/site/restservices/site-ajax/assignTask" id="unassign"
	class="siteAjaxForm">
	<input type="hidden" name="activityInstanceUuid"
		value="${activity.activityInstanceUuid}" /> <input type="hidden"
		name="action" value="unassign" /> <input type="hidden" size="8"
		name="targetUserId" value="${user.uuid}" />
</form>

<form action="./search/by-tag/casesbytagvalue" method="POST" id="searchTagForm">
	<input type="hidden" name="searchStr" value=""/>
</form>


<!-- jquery scripts for dialogs etc TODO move to separate js file -->
<script type="text/javascript" charset="utf-8">
	
	
	$jq(".add-button").button({
		color : 'green',
		icons : {
			primary : 'ui-icon-circle-plus',
			secondary : null
		}
	});

	/*
	 * Refresh tags list
	 */
	function refreshTags(info) {
		console.log("refreshTags TODO, asynkront anrop lÃ¤gger till taggar sÃ¥ tÃ¤nk ut vettigaste sÃ¤ttet att Ã¥terkoppla");
	}

	$jq("#myTags").tagit({
	       onTagClicked: function(event, ui) {
             // do something special
             $jq("#searchTagForm input[name='searchStr']").val(ui.tagLabel);
             $jq("#searchTagForm").submit();
           },
           beforeTagRemoved: function(event, ui) {
             // do something special
	         siteAjaxPost("/site/restservices/site-ajax/deleteTag", {
		          processInstanceUuid : '${activity.processInstanceUuid}',
		          value: ui.tagLabel
                  
	           }, function(data) {
		         refreshTags(data);
               }
	         );
           },
           beforeTagAdded: function(event, ui) {
             // do something special
		     if (ui.duringInitialization) {
		       //console.log("init tag " + ui.tagLabel);
		     }
             else {
                //console.log("added " + ui.tagLabel);
  	            siteAjaxPost("/site/restservices/site-ajax/addTag", {
		              actinstId : '${activity.actinstId}',
                      tagTypeId : '10000',
		              value: ui.tagLabel
	               }, function(data) {
		              refreshTags(data);
                   }
	            );
              }
           }
        });

	$jq("#dialog-comment-case").dialog(
			{
				autoOpen : false,
				height : 300,
				width : 350,
				modal : true,
				buttons : {
					"Kommentera" : function(data) {
						siteAjaxPost($jq("#addCommentForm").attr('action'), $jq(
								"#addCommentForm").serialize(), function() {
							siteAjaxPost("/site/restservices/site-ajax/getCommentFeed", {activityInstanceUuid: '${activity.activityInstanceUuid}'}, function(data) {
							    refreshCommentFeed(data);
							});
							$jq("#dialog-comment-case").dialog("close");
						});
					},
					Cancel : function() {
						$jq(this).dialog("close");
					}
				},
				open: function() {
				    $jq("#dialog-comment-case").keypress(function(e) {
				      if (e.keyCode == $jq.ui.keyCode.ENTER) {
				        $jq(this).parent().find("button:eq(0)").trigger("click");
				        return false;
				      }
				    });
				},
				close : function() {
					$jq("#addCommentForm input[name=comment]").val('');
				}
			});

	$jq("#dialog-edit-candidates").dialog({
		autoOpen : false,
		height : 500,
		width : 400,
		modal : true,
		buttons : {
			"Ok" : function() {
				var bValid = true;
				allFields.removeClass("ui-state-error");
				bValid = bValid && checkLength(name, "Namn", 1, 255);
				if (bValid) {

					$jq(this).dialog("close");
				}
			},
			Cancel : function() {
				$jq(this).dialog("close");
			}
		},
		close : function() {
			allFields.val("").removeClass("ui-state-error");
		}
	});

	$jq("#add-candidate").click(function() {
		$jq("#dialog-edit-candidates").dialog("open");
	});

	$jq("#add-comment").click(function() {
		$jq("#dialog-comment-case").dialog("open");
	});

	$jq(".remove-candidate-btn").button({
		color : 'red',
		icons : {
			primary : 'ui-icon-trash',
			secondary : null
		}
	});

	$jq(".add-candidate-btn").button({
		color : 'green',
		icons : {
			primary : 'ui-icon-circle-plus',
			secondary : null
		}
	});

	$jq(".add-tag-btn").button({
		icons : {
			primary : 'ui-icon-circle-plus',
			secondary : null
		}
	});
	$jq(".edit-tag-btn").button();
	$jq(".remove-tag-btn").button({
		icons : {
			primary : 'ui-icon-trash',
			secondary : null
		}
	});

	/*
	 * Button and links to act on in page
	 */
	$jq("#assign-to-me").click(function() {
		siteAjaxPost("/site/restservices/site-ajax/assignTask", {
			activityInstanceUuid : '${activity.activityInstanceUuid}',
			action : 'assign',
			targetUserId : '${user.uuid}'
		}, function(data) {
			refreshActivityWorkflowInfo(data);
		});
	});
	$jq("#unassign").click(function() {
		siteAjaxPost("/site/restservices/site-ajax/assignTask", {
			activityInstanceUuid : '${activity.activityInstanceUuid}',
			action : 'unassign',
			targetUserId : '${user.uuid}'
		}, function(data) {
			refreshActivityWorkflowInfo(data);
		});
	});

	$jq("#activity-priority").change(function() {
		siteAjaxPost("/site/restservices/site-ajax/setActivityPriority", {
			activityInstanceUuid : '${activity.activityInstanceUuid}',
			priority : $jq("select option:selected").val()
		}, function(data) {
			refreshActivityWorkflowInfo(data);
		});
	});

	/*
	 * Refresh work flow info in page i.e. activity candidates and assigned 
	 */
	function refreshActivityWorkflowInfo(info) {
		var str = "";

		if (!$jq.isEmptyObject(info) && !$jq.isEmptyObject(info.assignedUser) && !$jq.isEmptyObject(info.assignedUser.uuid)) {
			/* Assigned task */
			if (info.assignedUser.uuid == '${user.uuid}') {
				/* Assigned to me */
				str = "Jag är tilldelad att utföra aktiviteten";
				$jq("#assign-to-me").hide();
				$jq("#unassign").show();
				$jq("#edit-candidates").hide();
			} else {
				/* Assigned to someone else */
				str = info.assignedUser.label
						+ " är tilldelad att utföra aktiviteten";
				$jq("#assign-to-me").show();
				$jq("#unassign").show();
				$jq("#edit-candidates").hide();
			}
		} else {
			/* not assigned i.e. list candidates */
			if ($jq.isEmptyObject(info) || $jq.isEmptyObject(info.candidates)) {
				str = "Ingen kandidat till att utföra aktiviteten";
			} else {
				if (info.candidates.length>0 && info.candidates.length < 4) {
					str = info.candidates[0].labelShort;
					for (var i=1;i<info.candidates.length;i++) {
						str += ", ";
						str += info.candidates[i].labelShort; 
					}
				} else {
					str = info.candidates[0].labelShort + ", " + info.candidates[1].labelShort + ", "
							+ info.candidates[2].labelShort + " och "
							+ (info.candidates.length - 3) + " till";
				}
				str += " är kandidater till att utföra aktiviteten";
			}
			$jq("#assign-to-me").show();
			$jq("#unassign").hide();
			$jq("#edit-candidates").show();
		}

		$jq("#activity-candidates").text(str);
		$jq("#activity-priority").val(info.priority);
	}
	
	function refreshCommentFeed(comments) {
	   $jq("#commentfeed").empty();
       for (var i=0;i<comments.length;i++) {
    	   var commentDate = new Date(comments[i].timestamp);
	       $jq("#commentfeed").append("<li><b>" + $jq.datepicker.formatDate('yy-mm-dd', commentDate) + " (" + comments[i].activityLabel + ") " + comments[i].user.label + ": </b><br/>" + comments[i].message + "</li>");
	   }
	}
	
	$jq(document).ready(function() {
	
		siteAjaxPost("/site/restservices/site-ajax/getActivityWorkflowInfo", {
			activityInstanceUuid : '${activity.activityInstanceUuid}'
		}, function(data) {
			refreshActivityWorkflowInfo(data);
		});
		
		siteAjaxPost("/site/restservices/site-ajax/getCommentFeed", {activityInstanceUuid: '${activity.activityInstanceUuid}'}, function(data) {
		    refreshCommentFeed(data);
		} );
	
	});
</script>
