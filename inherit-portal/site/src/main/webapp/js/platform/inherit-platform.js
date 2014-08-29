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
});