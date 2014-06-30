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
 
 
<%@ include file="/WEB-INF/jspf/htmlTags.jspf"%>
<!-- Workflow add candidate -->
<div>
	<div id="svid10_2842d8c1129693185ff800017435">
		<!-- Page content starts here -->
		<div class="pagecontent" id="svid94_1d8b012512b3897f473800038727">
			<div id="Hogerspalt">
				<!-- HÃ¶gerspalt -->
			</div>
			
			<div id="svid12_1d8b012512b3897f473800038730">
				<div id="Text3-0">
					<!-- Text 3 -->
				</div>
				<div id="h-Omduvill">
					<!-- Om du vill: -->
				</div>
				<h2>
					<fmt:message key="mycases.actlist.lbl" />
				</h2>

				<tag:actlist timelineItems="${processInstanceDetails.timeline.items}" viewMode="full"/>

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
							  <a href="#" id="add-candidates"><fmt:message	key="mycases.editcandidates.lbl" /></a> 
							  <a href="#" id="unassign"><fmt:message key="mycases.unassign.lbl" /></a>
						</p>
						<p>
							<a href="#" id="assign-to-me"><fmt:message
									key="mycases.assigntome.lbl" /></a>
						</p>
						<p>
							<a href="#" id="add-comment"><fmt:message key="mycases.addcomment.lbl" /></a>
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
					<h2><fmt:message key="handlaggningsguide.lbl" /></h2>
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
				<th><fmt:message key="mycases.cn.lbl" /></th>
				<th><fmt:message key="mycases.email.lbl" /></th>
				<th></th>
				<th></th>
			</tr>
		</thead>
	<tbody id="output2" >
	  <tr>
	    <td></td>
	    <td></td>
        <td></td>
	    <td><button class="remove-candidate-btn" href="#"></button></td>
	    <td><button class="lock-candidate-btn" href="#"></button></td>
	  </tr>
	</tbody>
	</table>

	<h3></h3>

<!-- 
			<label for="name"><fmt:message key="mycases.name.lbl" /></label> <input
				type="text" name="name" id="name"
				class="text ui-widget-content ui-corner-all" /> <label for="email"><fmt:message
					key="mycases.email.lbl" /></label> <input type="text" name="email"
				id="email" value="" class="text ui-widget-content ui-corner-all" />
			<button id="search-button" class="search-button">
				<fmt:message key="mycases.search.lbl" />
			</button>
-->
	
	<form id="search-users-form" action="/site/restservices/site-ajax/dirSearchUserEntries" method="post"> 
	<!-- uid -->        
	<label for="cn"><fmt:message key="mycases.cn.lbl" /></label>
	<input type="text" name="cn" id="cn"
	       class="text ui-widget-content ui-corner-all" />
	<br/>

	<!-- sn -->        
	<label for="sn"><fmt:message key="mycases.sn.lbl" /></label>
	<input type="text" name="sn" id="sn"
	       class="text ui-widget-content ui-corner-all" />
	<br/>

	<!-- mail -->      
	<label for="mail"><fmt:message key="mycases.email.lbl" /></label>
	<input type="text" name="mail"
	       id="mail" value=""
	       class="text ui-widget-content ui-corner-all" />
	<br/>

	<!-- givenName --> 
	<label for="givenName"><fmt:message key="mycases.givenname.lbl" /></label>
	<input type="text" name="givenName"
	       id="givenName" value=""
	       class="text ui-widget-content ui-corner-all" />
	<br/>

	<!-- department -->
	<label for="department"><fmt:message key="mycases.department.lbl" /></label>
	<input type="text" name="department"
	       id="department" value=""
	       class="text ui-widget-content ui-corner-all" />
	<br/>

	<!-- company -->
	<label for="company"><fmt:message key="mycases.company.lbl" /></label>
	<input type="text" name="company"
	       id="company" value=""
	       class="text ui-widget-content ui-corner-all" />
	<br/>
    <div >
       <input type="submit" class="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" value="<fmt:message key="search.submit.text" />" /> 
    </div>
      </form>

	<h3>
		<fmt:message key="mycases.searchresult.column.lbl" />
	</h3>

	<table>
		<thead>
			<tr>
				<th><fmt:message key="mycases.name.lbl" /></th>
				<th><fmt:message key="mycases.cn.lbl" /></th>
				<th><fmt:message key="mycases.email.lbl" /></th>
				<th></th>
			</tr>
		</thead>
		<tbody id="output1">
			<tr>
				<td></td>
				<td></td>
				<td></td>
<!--				<td><button class="add-candidate-btn" href="#"></button></td> -->
				<td></td>
			</tr>
		</tbody>
	</table>

	<form action="/site/restservices/site-ajax/assignTask"
		id="addCandidate" class="siteAjaxForm">
		<input type="hidden" name="activityInstanceUuid"
			value="${activity.activityInstanceUuid}" />
		<input type="hidden" name="action"
		    value="addcandidate" />
	    <input type="hidden" size="8"name="targetUserId" placeholder="" />
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
<script type="text/javascript" src="/site/js/form/jquery.form.min.js"></script>
<script type="text/javascript" charset="utf-8">
<!-- http://stackoverflow.com/questions/11219731/trim-function-doesnt-work-in-ie8 -->
if(typeof String.prototype.trim !== 'function') {
  String.prototype.trim = function() {
    return this.replace(/^\s+|\s+$/g, ''); 
  };
}

  var $jq = jQuery.noConflict();

	$jq(document).ready(function() {
		siteAjaxPost("/site/restservices/site-ajax/getActivityWorkflowInfo", {
			activityInstanceUuid : '${activity.activityInstanceUuid}'
		}, function(data) {
			refreshActivityWorkflowInfo(data);
		});
		
		siteAjaxPost("/site/restservices/site-ajax/getCommentFeed", 
                             {activityInstanceUuid:
                             '${activity.activityInstanceUuid}'},
                             function(data) {
		                refreshCommentFeed(data);
		             } );
	
        var options = { 
          target:       '#output1',      // target element(s) to be updated with server response 
          dataType:     'json',          // 'xml', 'script', or 'json' (expected server response type) 
          clearForm:    false,            // clear all form fields after successful submit 
          resetForm:    false,            // reset the form after successful submit 
          beforeSubmit: showRequest,     // pre-submit callback 
          success:      showResponse     // post-submit callback 
 
          // other available options: 
          //url:       url         // override for form's 'action' attribute 
          //type:      type        // 'get' or 'post', override for form's 'method' attribute 
 
          // $jq.ajax options can be used here too, for example: 
          //timeout:   3000 
        }; 

        console.log("In document.ready, before call to jq(#search-users-form) for activity: " +
	      "${activity.activityInstanceUuid}");

        // bind 'search-users-form' and provide a callback function 

        //$jq('#search-users-form').ajaxForm(function(options) { 
        //  alert("Thank you for your input!"+ JSON.stringify(options, null, 4));  
        //});

        console.log("before binding #search-users-form, options: " + JSON.stringify(options, null, 4)); 
        $jq('#search-users-form').ajaxForm(options);

	});
	
    // pre-submit callback 
    function showRequest(formData, jqForm, options) { 
	  // formData is an array; here we use $jq.param to convert it to a string to display it 
	  // but the form plugin does this for you automatically when it submits the data 
	  var queryString = $jq.param(formData);

	  // jqForm is a jQuery object encapsulating the form element. To access the 
	  // DOM element for the form do this: 
	  // var formElement = jqForm[0]; 

        console.log("in showRequest...About to submit: " + queryString); 

	  // here we could return false to prevent the form from being submitted; 
	  // returning anything other than false will allow the form submit to continue 
	  return true; 
    } 

    // post-submit callback 
    function showResponse(responseText, statusText, xhr, $form)  { 
	  // for normal html responses, the first argument to the success callback 
	  // is the XMLHttpRequest object's responseText property 

	  // if the ajaxForm method was passed an Options Object with the dataType 
	  // property set to 'xml' then the first argument to the success callback 
	  // is the XMLHttpRequest object's responseXML property 

	  // if the ajaxForm method was passed an Options Object with the dataType 
	  // property set to 'json' then the first argument to the success callback 
	  // is the json data object returned by the server 

	  //alert('status: ' + statusText + '\n\nresponseText: \n' + 
        //      JSON.stringify(responseText, null, 4) + 
	  //      '\n\nThe output div should have already been updated with the responseText.');

        //$jq('#output1').hide().html(JSON.stringify(responseText, null, 4)).fadeIn('slow');

        var htmlfragment = "";
        $jq.each(responseText, function() {
          htmlfragment += 
            '<tr>' + 
            '<td>' + this.label + '</td>' +
            '<td>' + this.cn    + '</td>' +
            '<td>' + this.mail  + '</td>' +
	      '<td><button class="add-candidate-btn" href="#"></button></td>' +
            '</tr>' ;
        });
    
       $jq('#output1').html(htmlfragment);

	 $jq(".add-candidate-btn").button({
              create: function (event,ui) {console.log("add candidate button create event...");},
		color : 'green',
		icons : {
			primary : 'ui-icon-circle-plus',
			secondary : null
		}
	 }).click(function(event){ 
         // console.log("add button clicked...:" + event.target.nodename );
         // first, siteAjaxPost addcandidate message  with uid and 
         // activityinstanceuuid of current activity
         event.preventDefault();
         var entry    = $jq(this).closest('tr').children().first().next().text();
         var entryrow = $jq(this).closest('tr');
         console.log("targetUserId: " + entry);
         siteAjaxPost("/site/restservices/site-ajax/assignTask",
           "activityInstanceUuid=${activity.activityInstanceUuid}&action=addcandidate&targetUserId=" + entry,
           function(data) {
             // if success remove table row entry from page
             console.log("add candidate: " + entry + ", removing row from search results");
//             event.preventDefault();
//             $jq(this).closest("tr").remove();
             $jq(entryrow).remove();
             refreshActualCandidates();
	   }); 
       });
    }

    function refreshActualCandidates() {
        console.log("refreshActualCandidates for activity: ${activity.activityInstanceUuid}");
        siteAjaxPost(
          "/site/restservices/site-ajax/getActivityWorkflowInfo",
          "activityInstanceUuid=${activity.activityInstanceUuid}",
          function(data) {
              var assignedUserUid = data.assignedUser.uuid ;
              if ( assignedUserUid.trim() ) { // not null, not empty and not only WS
                // first check if assignedUserUid is present in candidate array
                // if not, add it to the candidate array
                resultArray = $jq.grep(data.candidates, 
                   function(n,i) {
//                     console.log( ((n.uuid === assignedUserUid)? 'found' : 'not found') + ': ' + n.uuid  + ' ' + assignedUserUid);
                     return (n.uuid === assignedUserUid);
                   }
                );
                if ( resultArray.length === 0 ) { // add assigned user to candidate list
                   data.candidates.push(data.assignedUser);
                }
              }
  
            var cnListQueryStr = '';
            $jq.each(data.candidates, 
              function() {
                cnListQueryStr += 'cnList=' + this.uuid + '&' ;
              }
            );
            console.log("refreshActualCandidates, cnListQueryStr: " + cnListQueryStr);
            if (cnListQueryStr.trim()) {
 	       siteAjaxPost(
		 "/site/restservices/site-ajax/dirLookupUserEntries",
		 cnListQueryStr,
		 function(data) {
		   var htmlfragment = '';
           var assigned = false;
		   console.log('Assigned User: "' + assignedUserUid + '"');
                   if (assignedUserUid.trim()) { // there is an assigned user in the list
                     $jq.each(data, function() {
                       assigned = (this.cn == assignedUserUid);
                       htmlfragment += 
                         ( assigned ? '<tr class="assigned">' : '<tr class="unassigned">' ) +
			 ( assigned ? '<td class="assigned">' : '<td class="unassigned">' ) + this.label + '</td>' +
			 ( assigned ? '<td class="assigned">' : '<td class="unassigned">' ) + this.cn    + '</td>' +
			 ( assigned ? '<td class="assigned">' : '<td class="unassigned">' ) + this.mail  + '</td>' +
                          '<td />' +
             ( assigned ? '<td><button class="lock-candidate-btn" href="#"></button></td>' : '<td />' ) +
			 '</tr>' ;
                     });
                   } else {
                     $jq.each(data, function() {
                       htmlfragment += 
                         '<tr>' +
               			 '<td>' + this.label + '</td>' +
			             '<td>' + this.cn    + '</td>' +
			             '<td>' + this.mail  + '</td>' +
                         '<td><button class="remove-candidate-btn" href="#"></button></td>' +
                         '<td><button class="lock-candidate-btn" href="#"></button></td>' +
			             '</tr>' ;
                     });
                   }

                   //console.log("dirlookup htmlfragment: " + htmlfragment);
		   $jq('#output2').html(htmlfragment);
		   $jq(".remove-candidate-btn").button({
		     create: function (event,ui) {console.log("remove candidate button create event...");},
		     color : 'red',
		     icons : {
		       primary : 'ui-icon-circle-minus',
		       secondary : null
		     }
		   }).click(function(event){ 
                  // console.log("remove button clicked...:" + event.target.nodename );
                  // first, siteAjaxPost with uid and activityinstanceuuid of current
                  //   activity 
                  event.preventDefault();
                  var entry = $jq(this).closest('tr').children().first().next().text();
                  console.log("targetUserId: " + entry);
                  siteAjaxPost("/site/restservices/site-ajax/assignTask",
                               "activityInstanceUuid=${activity.activityInstanceUuid}&action=removecandidate&targetUserId=" + entry,
                    function(data) {
                      // if success, refreshActualCandidates(), else leave as is
                      refreshActualCandidates();
                    });
		   });

		   $jq(".lock-candidate-btn").button({
			     create: function (event,ui) {
	                       console.log("lock button create event2...");
	                       //$jq(this).parent().closest('tr').css({backgroundColor:'yellow'});
	                     },
			     color : 'red',
			     icons : {
			       primary : ( (assignedUserUid.trim() ) ? 'ui-icon-locked' : 'ui-icon-unlocked' ),
			       secondary : null
			     }
			   }).click(function(event){ 
	                       console.log("Locking/Unlocking row...");
	                       event.preventDefault();
	                       // get targetUid, i.e. uid of current row
	                       var entry = $jq(this).closest('tr').children().first().next().text();
	                       console.log("targetUserId: " + entry);
//	                       $jq(this).closest('tr').css({backgroundColor:'yellow'});
	                       console.log($jq(this).parent().html());
//	                       $jq(this).closest('button.assigned')....;
		             if ( assignedUserUid.trim() ) {
	                     siteAjaxPost("/site/restservices/site-ajax/assignTask",
	                                  "activityInstanceUuid=${activity.activityInstanceUuid}&action=unassign&targetUserId=" + entry,
	                       function(data) {
	                         // if success, refreshActualCandidates(), else leave as is
	                         refreshActualCandidates();
	                       }); } else {
	                        siteAjaxPost("/site/restservices/site-ajax/assignTask",
	                                     "activityInstanceUuid=${activity.activityInstanceUuid}&action=assign&targetUserId=" + entry,
	                       function(data) {
	                         // if success, refreshActualCandidates(), else leave as is
	                         refreshActualCandidates();
	                       }); 
	                      }
			   });
              });
            } else {
		   $jq('#output2').html("<tr />");
            }                
          }); 
      }


$jq("#dialog-edit-candidates").dialog({
	  autoOpen : false,
        resizable: false,
	  //height : 500,
	  height : 'auto',
	  //width : 400,
        width:'auto',
	  modal : true,
	  open: function( event, ui ) {
	          console.log("#dialog-edit-candidates: open event fired...");
                refreshActualCandidates();
        },
	  beforeClose: function( event, ui ) {
	    console.log("#dialog-edit-candidates: beforeclose event fired...");
          $jq('#search-users-form').clearForm();
	    $jq('#output1').html('<tr />');
        },
	  buttons : {
	    "St\u00e4ng" : function() {
	      $jq(this).dialog("close");
	    }
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

	$jq("#add-candidates").click(function() {
		$jq("#dialog-edit-candidates").dialog("open");
	});

//	 $jq("#search-users-form input:last").button({
//         create: function (event,ui) {console.log("#search-users-form input:last button create event...");},
//	     color : 'green'
//	}
//})
	
	
	$jq("#add-comment").click(function() {
		$jq("#dialog-comment-case").dialog("open");
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
	
</script>
