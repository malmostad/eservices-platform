function siteAjaxPost(url, postdata, processSuccess ) {
	  /* Send the data using post and put the results in a div */
	  $.ajax({
	    type: 'POST',
	    url: url,
	    data: postdata,
	    success: function( data ) {
                processSuccess(data);

	    },
	    error : function() {
	    },
	    complete : function( data ) {

	    },
	    dataType:"json"
	   });
	};

$(document).ready(function() {
	
	/*
	 * generic toggle view content with list items and jquery
	 */
	$('.toggle-view li').click(function() {
		var text = $(this).children('div.panel');
		if (text.is(':hidden')) {
			text.slideDown('200');
			$(this).children('span.exp').html('- g&ouml;m'); //TODO multi lingual...

		} else {
			text.slideUp('200');
			$(this).children('span.exp').html('+ visa mer...'); //TODO multi lingual...
		}
	});

         $(".hitlist_goto_page").click(function(event) {
		           event.preventDefault();
		           var hrefPage = $(this).attr("href");
		           $("#searchPanelForm  > input[name=page]").prop("value", hrefPage);
		            $("#searchPanelForm").submit();
		          });
 
	  $("#search-btn").click(function(event) {
		   $("#searchPanelForm  > input[name=page]").prop("value", 1);
	  });

	  $(".toggle-sortorder").click(function(event) {
	           var hrefDat = $(this).attr("href");	                   
                   if( hrefDat=="#startDate" ){
	              $("#searchPanelForm  > input[name=sortBy]").prop("value", "started");
                   } 
                   if( hrefDat=="#endDate" ) {
	              $("#searchPanelForm  > input[name=sortBy]").prop("value", "ended");
                   } 
		

                   sortOrder = $("#searchPanelForm  > input[name=sortOrder]").prop("value") 
                   if (sortOrder == 'desc'   ) {
                       sortOrder = 'asc' ; 
                         } else { 
                             if  (sortOrder == 'asc'   ) { 
                               sortOrder = 'desc' ; } }
                 $("#searchPanelForm  > input[name=sortOrder]").prop("value", sortOrder);
		 $("#searchPanelForm  > input[name=page]").prop("value", 1);
	  });
	
	/*
	 * load url into div tag of class panel in a toggle view
	 * 
	 * temporary removed, use iframe instead. workaround 
	 * multiple orbeon forms at the same page problem...
	 *  
	
	$('.timeline li').click(function() {
		var url = $(this).children("a.view-url").attr('href');
		if (typeof (url) !== 'undefined' && url.length > 0) {
			var divPanel = $(this).find("div.panel");
			// var xformPanel = $(divPanel).find("div.xform");
			$(divPanel).load(url, function(data) {
				if (typeof ORBEON != "undefined") {
					if (!document.all) {
						ORBEON.xforms.Init.document();
					}
				}
			});
		}
	});
	*/
	
	/*
	 * load url into iframe in a toggle view
	 */
	$('.timeline li').click(function() {
		var url = $(this).children("a.view-url").attr('href');
		if (typeof (url) !== 'undefined' && url.length > 0) {
			var divPanel = $(this).find("div.panel");
	        var i = $(divPanel).find("iframe.iframe-orbeon-panel");
	        $(i).attr("src", url);
	    }
	});
	
	$('.iframe-orbeon-panel').load(function() {
		this.style.height =
		this.contentWindow.document.body.offsetHeight + 15 +  'px';
	});

      // load xform div if there is one
      var url = $("#xform").children("a.view-url").attr('href');
      if (typeof (url) !== 'undefined' && url.length > 0) {
        $("#xform").load(url, function(data) {
          if (typeof ORBEON != "undefined") { 
            if (!document.all) {
              ORBEON.xforms.Init.document(); 
            } 
          } 
        }); 
      }
/* Swedish initialisation for the jQuery UI date picker plugin. */
/* Written by Anders Ekdahl ( anders@nomadiz.se). */
    $.datepicker.regional['sv'] = {
		closeText: 'Stäng',
        prevText: '&laquo;Förra',
		nextText: 'Nästa&raquo;',
		currentText: 'Idag',
        monthNames: ['Januari','Februari','Mars','April','Maj','Juni',
        'Juli','Augusti','September','Oktober','November','December'],
        monthNamesShort: ['Jan','Feb','Mar','Apr','Maj','Jun',
        'Jul','Aug','Sep','Okt','Nov','Dec'],
		dayNamesShort: ['Sön','Mån','Tis','Ons','Tor','Fre','Lör'],
		dayNames: ['Söndag','Måndag','Tisdag','Onsdag','Torsdag','Fredag','Lördag'],
		dayNamesMin: ['Sö','Må','Ti','On','To','Fr','Lö'],
		weekHeader: 'Ve',
        dateFormat: 'yy-mm-dd',
		firstDay: 1,
		isRTL: false,
		showMonthAfterYear: false,
		yearSuffix: ''};
    $.datepicker.setDefaults($.datepicker.regional['sv']);
});


 $( ".dirusername" ).autocomplete({
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
        $(this).val( ui.item.cn);
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
    }).each(function() {
         $(this).data("ui-autocomplete")._renderItem = function( ul, item ) {
         return  $( "<li>" )
	.attr( "data-value", item.cn )
        .append( "<a><strong>" + item.cn + "</strong> - " + item.label + "</a>" )
        .appendTo( ul );
      };
    });