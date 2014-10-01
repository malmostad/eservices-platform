
// http://stackoverflow.com/questions/11219731/trim-function-doesnt-work-in-ie8 
if(typeof String.prototype.trim !== 'function') {
  String.prototype.trim = function() {
    return this.replace(/^\s+|\s+$/g, ''); 
  };
}

$(document).ready(function() {

	    
	    
        $(".motrice-assign-to").click(function() {
	    var assignTo = $(this).children("input[name='motrice-assign-to']").attr("value");
	    var instanceUuid = $(this).children("input[name='motrice-activity-instance-uuid']").attr("value");
	    siteAjaxPost("/site/restservices/site-ajax/assignTask",
	                 "activityInstanceUuid=" + instanceUuid + "&action=assign&targetUserId=" + assignTo,
	                   function(data) {
	                     // if success, refreshActualCandidates(), else leave as is
			     refreshActivityWorkflowInfo(data);
	                   }	
		); 
        });

        $(".motrice-unassign-user").click(function() {
	    var unassignUser = $(this).children("input[name='motrice-unassign-user']").attr("value");
	    var instanceUuid = $(this).children("input[name='motrice-activity-instance-uuid']").attr("value");
	     siteAjaxPost("/site/restservices/site-ajax/assignTask",
	                  "activityInstanceUuid=" + instanceUuid + "&action=unassign&targetUserId=" + unassignUser,
	                   function(data) {
	                     // if success, refreshActualCandidates(), else leave as is
			       refreshActivityWorkflowInfo(data);
	                   }
			); 
        });

        $(".motrice-activity-workflow").each(function( i ) {
	    var instanceUuid = $(this).children("input[name='motrice-activity-instance-uuid']").attr("value");
	    siteAjaxPost("/site/restservices/site-ajax/getActivityWorkflowInfo", {
		activityInstanceUuid : instanceUuid
	    }, function(data) {
		refreshActivityWorkflowInfo(data);
	    });
	});

    $(".commentfeed").each(function( i ) {	
	    var instanceUuid = $(this).siblings("input[name='activityInstanceUuid']").attr("value");
		siteAjaxPost("/site/restservices/site-ajax/getCommentFeed", 
            {activityInstanceUuid: instanceUuid, language:'sv' , country:'SE' },
            function(data) {
	          refreshCommentFeed(data);
	        } 
	    );
	
	});

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

        console.log("In document.ready, before call for activity");

        // bind 'search-users-form' and provide a callback function 

        //$('#search-users-form').ajaxForm(function(options) { 
        //  alert("Thank you for your input!"+ JSON.stringify(options, null, 4));  
        //});

 //       console.log("before binding #search-users-form, options: " + JSON.stringify(options, null, 4)); 
 //       $('#search-users-form').ajaxForm(options);
 	   $("#search-users-form").ajaxForm(options);
 	    console.log("In document.ready, after  call for activity");

});
	
    // pre-submit callback cd in	po	
    function showRequest(formData, jqForm, options) { 
	  // formData is an array; here we use $jq.param to convert it to a string to display it 
	  // but the form plugin does this for you automatically when it submits the data 
	  var queryString = $.param(formData);

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

        //$('#output1').hide().html(JSON.stringify(responseText, null, 4)).fadeIn('slow');

        var htmlfragment = "";
        $.each(responseText, function() {
          htmlfragment += 
            '<tr>' + 
            '<td>' + this.label + '</td>' +
            '<td>' + this.cn    + '</td>' +
            '<td>' + this.mail  + '</td>' +
	      '<td><button class="add-candidate-btn" href="#"></button></td>' +
            '</tr>' ;
        });
    
       $('#output1').html(htmlfragment);

	 $(".add-candidate-btn").button({
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
         var entry    = $(this).closest('tr').children().first().next().text();
         var entryrow = $(this).closest('tr');
         // fix id ref to dialog later....
         var instanceUuid = $("#dialog-edit-candidates").children("input[name='motrice-activity-instance-uuid']").attr("value");
         console.log("targetUserId: " + entry);
         siteAjaxPost("/site/restservices/site-ajax/assignTask",
           "activityInstanceUuid=" + instanceUuid + "&action=addcandidate&targetUserId=" + entry,
           function(data) {
             // if success remove table row entry from page
             console.log("add candidate: " + entry + ", removing row from search results");
//             event.preventDefault();
//             $(this).closest("tr").remove();
             $(entryrow).remove();
             refreshActualCandidates();
             refreshActivityWorkflowInfo(data);
	   }); 
       });
    }

    function refreshActualCandidates(data) {
        console.log("refreshActualCandidates for activity: activity.activityInstanceUuid");
        var instanceUuid = $("#dialog-edit-candidates").children("input[name='motrice-activity-instance-uuid']").attr("value");
         
        siteAjaxPost(
          "/site/restservices/site-ajax/getActivityWorkflowInfo",
          "activityInstanceUuid=" + instanceUuid,
          function(data) {
              var assignedUserUid = data.assignedUser.uuid ;
              if ( assignedUserUid.trim() ) { // not null, not empty and not only WS
                // first check if assignedUserUid is present in candidate array
                // if not, add it to the candidate array
                resultArray = $.grep(data.candidates, 
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
            $.each(data.candidates, 
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
                     $.each(data, function() {
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
                     $.each(data, function() {
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
		   $('#output2').html(htmlfragment);
		   $(".remove-candidate-btn").button({
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
                  var entry = $(this).closest('tr').children().first().next().text();
                  var instanceUuid = $("#dialog-edit-candidates").children("input[name='motrice-activity-instance-uuid']").attr("value");
         
                  console.log("targetUserId: " + entry);
                  siteAjaxPost("/site/restservices/site-ajax/assignTask",
                               "activityInstanceUuid=" + instanceUuid + "&action=removecandidate&targetUserId=" + entry,
                    function(data) {
                      // if success, refreshActualCandidates(), else leave as is
                      refreshActualCandidates();
                      refreshActivityWorkflowInfo(data);
                    });
		   });

		   $(".lock-candidate-btn").button({
			     create: function (event,ui) {
	                       console.log("lock button create event2...");
	                       //$(this).parent().closest('tr').css({backgroundColor:'yellow'});
	                     },
			     color  :'red',
			     icons : {
			       primary : ( (assignedUserUid.trim() ) ? 'ui-icon-locked' : 'ui-icon-unlocked' ),
			       secondary : null
			     }
			   }).click(function(event){ 
	                       console.log("Locking/Unlocking row...");
	                       event.preventDefault();
	                       // get targetUid, i.e. uid of current row
	                       var entry = $(this).closest('tr').children().first().next().text();
	                       var instanceUuid = $("#dialog-edit-candidates").children("input[name='motrice-activity-instance-uuid']").attr("value");
         
	                       console.log("targetUserId: " + entry);
//	                       $(this).closest('tr').css({backgroundColor:'yellow'});
	                       console.log($(this).parent().html());
//	                       $(this).closest('button.assigned')....;
		             if ( assignedUserUid.trim() ) {
	                     siteAjaxPost("/site/restservices/site-ajax/assignTask",
	                                  "activityInstanceUuid=" + instanceUuid + "&action=unassign&targetUserId=" + entry,
	                       function(data) {
	                         // if success, refreshActualCandidates(), else leave as is
	                         refreshActualCandidates();
	                         refreshActivityWorkflowInfo(data);
	                       }); } else {
	                        siteAjaxPost("/site/restservices/site-ajax/assignTask",
	                                     "activityInstanceUuid=" + instanceUuid + "&action=assign&targetUserId=" + entry,
	                       function(data) {
	                         // if success, refreshActualCandidates(), else leave as is
	                         refreshActualCandidates();
	                         refreshActivityWorkflowInfo(data);
	                       }); 
	                      }
			   });
              });
            } else {
		   $('#output2').html("<tr />");
            }                
          }); 
      }

var editCandidatesDialog = $("#dialog-edit-candidates").dialog({
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
         //$("#search-users-form").clearForm();
         //$("#output1").html('<tr />');
        }
        /*
        ,
	  buttons : {
	    "St\u00e4ng" : function() {
	      $(this).dialog("close");
	    }
	}
	*/
	});

$(".motrice-add-candidates").click(function() {
		editCandidatesDialog.dialog("open");
});


$(".motrice-comment-btn-ok").click(function(event) {
	console.log("Publicera kommentar");
	event.preventDefault();
    var addCommentForm = $(this).parent();
    var instanceUuid = addCommentForm.children("input[name='motrice-activity-instance-uuid']").attr("value");
    
    console.log("Publicera kommentar för " + instanceUuid);
	siteAjaxPost(addCommentForm.attr('action'), addCommentForm.serialize(), function(data) {
		refreshCommentFeed(data);
	});
	
	$(this).siblings("input[name='comment']").attr("value", "");
	$(this).siblings(".motrice-comment-field-controls").hide();
	$(this).hide();
});

$(".motrice-comment-btn-cancel").click(function(event) {
	event.preventDefault();
	$(this).siblings("input[name='comment']").attr("value", "");
	$(this).siblings(".motrice-comment-field-controls").hide();
	$(this).hide();
});

$(".motrice-comment-field").focus(function() {
	$(this).siblings(".motrice-comment-field-controls").show();
});


	$('a.toggle-view-list').click(function(event) {
	    event.preventDefault();
		var list = $(this).siblings('ul.toggle-view-list');
		if (list.is(':hidden')) {
			list.slideDown('200');
			$(this).siblings("a.toggle-view-list").show();
			$(this).hide();
		} else {
			list.slideUp('200');
			$(this).siblings("a.toggle-view-list").show();
			$(this).hide();
		}
	});
	
	/*
	 * Refresh tags list
	 */
	 
	 
	function refreshTags(info) {
		console.log("refreshTags TODO, asynkront anrop lÃ¤gger till taggar sÃ¥ tÃ¤nk ut vettigaste sÃ¤ttet att Ã¥terkoppla");
	}

	function refreshDiaryTags(info) {
		console.log("refreshTags TODO, asynkront anrop lÃ¤gger till taggar sÃ¥ tÃ¤nk ut vettigaste sÃ¤ttet att Ã¥terkoppla");
	}

	$("#myTags").tagit({
	       onTagClicked: function(event, ui) {
             // do something special
             $("#searchTagForm input[name='searchStr']").val(ui.tagLabel);
             $("#searchTagForm").submit();
           },
           beforeTagRemoved: function(event, ui) {
	       var procInstId = $("#myTags").siblings("input[name='motrice-process-instance-uuid']").attr("value");
	         siteAjaxPost("/site/restservices/site-ajax/deleteTag", {
		     processInstanceUuid : procInstId,
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
		 var taskId = $("#myTags").siblings("input[name='motrice-activity-instance-uuid']").attr("value");
  	            siteAjaxPost("/site/restservices/site-ajax/addTag", {
		              actinstId : taskId,
                              tagTypeId : '10000',
		              value: ui.tagLabel
	               }, function(data) {
		              refreshTags(data);
                   }
	            );
              }
           }
        });


	$("#diaryNo").tagit({
	       onTagClicked: function(event, ui) {
             // do something special
             $("#searchTagForm input[name='searchStr']").val(ui.tagLabel);
             $("#searchTagForm").submit();
           },
           beforeTagRemoved: function(event, ui) {
	       var procInstId = $("#diaryNo").siblings("input[name='motrice-process-instance-uuid']").attr("value");
	         siteAjaxPost("/site/restservices/site-ajax/deleteTag", {
		     processInstanceUuid : procInstId,
		     value: ui.tagLabel
	           }, function(data) {
		         refreshDiaryTags(data);
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
		 var taskId = $("#diaryNo").siblings("input[name='motrice-activity-instance-uuid']").attr("value");
  	            siteAjaxPost("/site/restservices/site-ajax/addTag", {
		              actinstId : taskId,
                      tagTypeId : '1',
		              value: ui.tagLabel
	               }, function(data) {
		              refreshDiaryTags(data);
                   }
	            );
              }
           }
        });

	$("#dialog-comment-case").dialog(
			{
				autoOpen : false,
				height : 300,
				width : 350,
				modal : true,
				buttons : {
					"Kommentera" : function(data) {
						siteAjaxPost($("#addCommentForm").attr('action'), $(
								"#addCommentForm").serialize(), function() {
							siteAjaxPost("/site/restservices/site-ajax/getCommentFeed", {activityInstanceUuid: '112'}, function(data) {
							    refreshCommentFeed(data);
							});
							$("#dialog-comment-case").dialog("close");
						});
					},
					Cancel : function() {
						$(this).dialog("close");
					}
				},
				open: function() {
				    $("#dialog-comment-case").keypress(function(e) {
				      if (e.keyCode == $.ui.keyCode.ENTER) {
				        $(this).parent().find("button:eq(0)").trigger("click");
				        return false;
				      }
				    });
				},
				close : function() {
					$("#addCommentForm input[name=comment]").val('');
				}
			});


//	 $("#search-users-form input:last").button({
//         create: function (event,ui) {console.log("#search-users-form input:last button create event...");},
//	     color : 'green'
//	}
//})
	
//$(function() {
//
//$("#dialog-comment-case").dialog("open");
//  });	

	$("#add-comment").click(function() {
		$("#dialog-comment-case").dialog("open");
	});

	$(".add-tag-btn").button({
		icons : {
			primary : 'ui-icon-circle-plus',
			secondary : null
		}
	});

	$(".edit-tag-btn").button();

	$(".remove-tag-btn").button({
		icons : {
			primary : 'ui-icon-trash',
			secondary : null
		}
	});

	/*
	 * Button and links to act on in page
	 */

	$(".motrice-activity-priority-select").change(function() {
	    var instanceUuid = $(this).parent().siblings("input[name='motrice-activity-instance-uuid']").attr("value");
		siteAjaxPost("/site/restservices/site-ajax/setActivityPriority", {
			activityInstanceUuid : instanceUuid,
			priority : $("select option:selected").val()
		}, function(data) {
			refreshActivityWorkflowInfo(data);
		});
	});

	/*
	 * Refresh work flow info in page i.e. activity candidates and assigned 
	 */
	function refreshActivityWorkflowInfo(info) {
		var str = "";
	        
	        var authUserUuid = $("#motrice-auth-user-uuid").attr("value");

	    if (!$.isEmptyObject(info) && !$.isEmptyObject(info.assignedUser) && !$.isEmptyObject(info.assignedUser.uuid) && !$.isEmptyObject(authUserUuid)) {
			/* Assigned task */
			if (info.assignedUser.uuid == authUserUuid) {
				/* Assigned to me */
				str = "Jag är tilldelad att utföra aktiviteten";
				$(".motrice-assign-to").hide();
				$(".motrice-unassign-user").show();
				$("#edit-candidates").hide();
			} else {
				/* Assigned to someone else */
				str = info.assignedUser.label
						+ " är tilldelad att utföra aktiviteten";
				$(".motrice-assign-to").hide();
				$(".motrice-unassign-user").show();
				$("#edit-candidates").hide();
			}
		} else {
			/* not assigned i.e. list candidates */
			if ($.isEmptyObject(info) || $.isEmptyObject(info.candidates)) {
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
			$(".motrice-assign-to").show();
			$(".motrice-unassign-user").hide();
			$("#edit-candidates").show();
		}

	    $(".motrice-activity-workflow").each(function( i ) {
		var instanceUuid = $(this).children("input[name='motrice-activity-instance-uuid']").attr("value");
		if ( !$.isEmptyObject(info) && instanceUuid == info.taskId ) {
		    $(this).children(".motrice-activity-priority").children("select.motrice-activity-priority-select").val(info.priority);
		    $(this).children(".motrice-activity-candidates").text(str);
		}
	    });

	}

	function refreshCommentFeed(comments) {
	   var N=1; // one comment in lastcomments
	   $("#commentfeed").empty();
	   $("#lastcomments").empty();
       for (var i=0;i<comments.length && i<N;i++) {
		$("#lastcomments").append("<li><h4><i class='fa fa-comment-o'></i> " + comments[i].user.label + " (" + comments[i].activityLabel + ") <small>" + comments[i].timeStampStr + "</small></h4><p>" + comments[i].message + "</p></li>");
	   }
       for (var i=N;i<comments.length;i++) {
    	  $("#commentfeed").append("<li><h4><i class='fa fa-comment-o'></i> " + comments[i].user.label + " (" + comments[i].activityLabel + ") <small>" + comments[i].timeStampStr + "</small></h4><p>" + comments[i].message + "</p></li>");
	   }
	   $("#commentfeed").siblings("a.toggle-view-list-show").attr("href", comments.length);
	   $("#commentfeed").siblings("a.toggle-view-list-show").html(comments.length + " kommentarer <i class='fa fa-chevron-circle-down'></i>"); //TODO multi lingual...
	}

	
