
// http://stackoverflow.com/questions/11219731/trim-function-doesnt-work-in-ie8 
if(typeof String.prototype.trim !== 'function') {
  String.prototype.trim = function() {
    return this.replace(/^\s+|\s+$/g, ''); 
  };
}

$(document).ready(function() {

	   $(".motrice-accordion" ).accordion();	    
	    
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

        $(".motrice-unassign-user-btn").click(function() {
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


 $('#startDate').datepicker({ 
 firstDay: 1 ,
  dateFormat: "yy-mm-dd" ,
  autoclose: true ,
   weekStart: 1   , 
   language: "sv"   , 
   todayHighlight: true
  }
);


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

$(".motrice-activity-candidates-details").click(function(event) {
	event.preventDefault();
	
	var instanceUuid = $(this).siblings("input[name='motrice-activity-instance-uuid']").val();
	
	console.log("instanceUuid: " + instanceUuid);
	
	var userUuid = $(event.target).siblings("input[name='user-uuid']").val();
	
	if ($.isEmptyObject(userUuid)) {
	    // work-around if sub element to button is clicked
		userUuid = $(event.target).parent().siblings("input[name='user-uuid']").val();
	}
	
	console.log("targetUserId: " + userUuid + " event.target: " + event.target);
	
	if ($(event.target).hasClass("motrice-task-assign-to-btn")) {
	    siteAjaxPost("/site/restservices/site-ajax/assignTask",
	       "activityInstanceUuid=" + instanceUuid + "&action=assign&targetUserId=" + userUuid,
	       function(data) {
	          // if success, refreshActualCandidates(), else leave as is
	          refreshActivityWorkflowInfo(data);
	       }	
		); 
	}
	else if ($(event.target).hasClass("motrice-task-remove-candidate-btn")) {
		siteAjaxPost("/site/restservices/site-ajax/assignTask",
                               "activityInstanceUuid=" + instanceUuid + "&action=removecandidate&targetUserId=" + userUuid,
            function(data) {
               // if success, refreshActualCandidates(), else leave as is
               refreshActivityWorkflowInfo(data);
            });
	}
	else if ($(event.target).hasClass("motrice-task-unassign-btn")) {            
        siteAjaxPost("/site/restservices/site-ajax/assignTask",
                  "activityInstanceUuid=" + instanceUuid + "&action=unassign&targetUserId=" + userUuid,
                   function(data) {
                     // if success, refreshActualCandidates(), else leave as is
		       			refreshActivityWorkflowInfo(data);
                   }
		); 
	}
	
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
	
	$('a.toggle-view-div').click(function(event) {
	    event.preventDefault();
		var divNode = $(this).siblings('div.toggle-view-div');
		if (divNode.is(':hidden')) {
			divNode.slideDown('200');
			$(this).siblings("a.toggle-view-div").show();
			$(this).hide();
		} else {
			divNode.slideUp('200');
			$(this).siblings("a.toggle-view-div").show();
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

$(".motrice-task-add-candidate-btn-cancel").click(function(event) {
	event.preventDefault();
	$(this).siblings("input[name='motrice-task-add-candidate']").val("");
	$(this).siblings(".motrice-task-add-candidate-field-controls").hide();
	$(this).hide();
});

$(".motrice-task-add-candidate").focus(function() {
	$(this).siblings(".motrice-task-add-candidate-field-controls").show();
});

$(".motrice-task-add-candidate-btn-ok").click(function(event) {
         // console.log("add button clicked...:" + event.target.nodename );
         // first, siteAjaxPost addcandidate message  with uid and 
         // activityinstanceuuid of current activity
         event.preventDefault();
         
         
         var entry    = $(this).siblings("input[name='motrice-task-add-candidate']").val();
         var instanceUuid = $(this).siblings("input[name='motrice-activity-instance-uuid']").val();
         console.log("targetUserId: " + entry);
         siteAjaxPost("/site/restservices/site-ajax/assignTask",
           "activityInstanceUuid=" + instanceUuid + "&action=addcandidate&targetUserId=" + entry,
           function(data) {
             // if success remove table row entry from page
             console.log("add candidate: " + entry + ", removing from input field and hide buttons");
			 $(this).siblings("input[name='motrice-task-add-candidate']").val("");
			 $(this).siblings(".motrice-task-add-candidate-field-controls").hide();
	         $(this).hide();
             refreshActivityWorkflowInfo(data);
	   }); 
});

	$(".motrice-task-remove-candidate-btn").click(function(event){ 
	    console.log("remove button clicked...:" + event.target.nodename );
	    // first, siteAjaxPost with uid and activityinstanceuuid of current
	    //   activity 
		event.preventDefault();
		var entry    = $(this).parent().parent().siblings("input[name='motrice-task-add-candidate']").val();
		var instanceUuid = $(this).parent().parent().siblings("input[name='motrice-activity-instance-uuid']").val();
	         
	    console.log("targetUserId: " + entry);
	    siteAjaxPost("/site/restservices/site-ajax/assignTask",
	        "activityInstanceUuid=" + instanceUuid + "&action=removecandidate&targetUserId=" + entry,
	        function(data) {
	           // if success, refreshActualCandidates(), else leave as is
	           refreshActivityWorkflowInfo(data);
	        }
	    );
	});

	$(".motrice-task-assign-to-candidate-btn").click(function(event) { 
		console.log("Locking/Unlocking row...");
		event.preventDefault();
	
	    var entry    = $(this).parent().parent().siblings("input[name='motrice-task-add-candidate']").val();
	    var instanceUuid = $(this).parent().parent().siblings("input[name='motrice-activity-instance-uuid']").val();
	
	    console.log("targetUserId: " + entry);
	
	    if ( assignedUserUid.trim() ) {
		    siteAjaxPost("/site/restservices/site-ajax/assignTask",
		      "activityInstanceUuid=" + instanceUuid + "&action=unassign&targetUserId=" + entry,
		      function(data) {
		        // if success, refreshActualCandidates(), else leave as is
		        refreshActivityWorkflowInfo(data);
	          }); 
	    } 
	    else {
		    siteAjaxPost("/site/restservices/site-ajax/assignTask",
		      "activityInstanceUuid=" + instanceUuid + "&action=assign&targetUserId=" + entry,
		      function(data) {
		         // if success, refreshActualCandidates(), else leave as is
		         refreshActivityWorkflowInfo(data);
		      }
		    ); 
		}
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
				$(".motrice-assigned-to-other-user").hide();
				$("#edit-candidates").hide();
			} else {
				/* Assigned to someone else */
				str = info.assignedUser.label
						+ " är tilldelad att utföra aktiviteten";
				$(".motrice-assign-to").hide();
				$(".motrice-unassign-user").hide();
				$(".motrice-assigned-to-other-user").show();
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
			$(".motrice-assigned-to-other-user").hide();
			$("#edit-candidates").show();
		}

	    $(".motrice-activity-workflow").each(function( i ) {
		var instanceUuid = $(this).children("input[name='motrice-activity-instance-uuid']").attr("value");
		if ( !$.isEmptyObject(info) && instanceUuid == info.taskId ) {
		    $(this).children(".motrice-activity-priority").children("select.motrice-activity-priority-select").val(info.priority);
		    $(this).children(".motrice-activity-candidates").text(str);
		    $(this).children("ul.motrice-activity-candidates-details").empty();
			for (var i=0;i<info.candidates.length;i++) {
			    var item = info.candidates[i];
			    if (!$.isEmptyObject(info.assignedUser) && !$.isEmptyObject(info.assignedUser.uuid)) {
			    	if (info.assignedUser.uuid  == item.uuid) {
						$(this).children("ul.motrice-activity-candidates-details").append("<li>" + item.label + "(" + item.labelShort + ") <button class='btn btn-danger motrice-task-unassign-btn'><i class='fa fa-unlock-alt'></i>&nbsp;Sl&auml;pp l&aring;s</button><input name='user-uuid' type='hidden' value='" + item.uuid + "'/></li>");			    	
			    	}
			    	else {
		//	    		$(this).children("ul.motrice-activity-candidates-details").append("<li>" + (item.type=="group" ? "G" : "U") + item.label + "<button class='motrice-task-remove-candidate-btn'><i class='fa fa-times'></i></button><input name='user-uuid' type='hidden' value='" + item.uuid + "'/></li>");
			    		if (item.type=="group") {
			    	 	  $(this).children("ul.motrice-activity-candidates-details").append("<li><i class='fa fa-users'></i>&nbsp;" + item.label + "<input name='user-uuid' type='hidden' value='" + item.uuid + "'/></li>");
			    		}
			    		else {   
							$(this).children("ul.motrice-activity-candidates-details").append("<li><i class='fa fa-user'></i>&nbsp;" + item.label + "<button class='motrice-task-remove-candidate-btn'><i class='fa fa-times'></i></button><input name='user-uuid' type='hidden' value='" + item.uuid + "'/></li>");
						}
			    	}
			    }
			    else {
			    	if (item.type=="group") {
			    	   $(this).children("ul.motrice-activity-candidates-details").append("<li><i class='fa fa-users'></i>&nbsp;" + item.label + "<input name='user-uuid' type='hidden' value='" + item.uuid + "'/></li>");
			    	}
			    	else {   
						$(this).children("ul.motrice-activity-candidates-details").append("<li><i class='fa fa-user'></i>&nbsp;" + item.label + "<button class='motrice-task-remove-candidate-btn'><i class='fa fa-times'></i></button>&nbsp;<button class='motrice-task-assign-to-btn'><i class='fa fa-lock'></i></button><input name='user-uuid' type='hidden' value='" + item.uuid + "'/></li>");
					}
				} 
			}
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


 $( ".motrice-add-dirusername" ).autocomplete({
      source: function( request, response ) {
        $.ajax({
          url: "/site/restservices/site-ajax/dirSearchUserEntries",
          dataType: "json",
          type: "POST",
          data: {
            cn: request.term
          },
          success: function( data ) {
            response( data );
          }
        }); 
      },
      minLength: 3,
      select: function( event, ui ) {
        $(this).val( ui.item.cn	);
        return false;
      },
      focus: function( event, ui ) {
        $(this).val( ui.item.cn);
        return false;
      },
      open: function() {
        $( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
      },
      close: function() {
        $( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
      }
    })/*.each(function() {
         $(this).data("ui-autocomplete")._renderItem = function( ul, item ) {
         return  $("<li>")
         .attr("data-value", item.cn )
        .append( "<a><strong>" + item.cn + "</strong> - " + item.label + "</a>" )
        .appendTo( ul );
      };
    })*/;
	
