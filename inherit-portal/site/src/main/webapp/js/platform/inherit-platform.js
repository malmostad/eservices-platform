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
	 */
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
	
	/*
	 * todo move? take care of result...
	 */
	$(".siteAjaxForm").submit(function(event) {
	   /* stop form from submitting normally */
	   event.preventDefault();
	   /* get some values from elements on the page: */
	   var $form = $( this );
	   var url = $form.attr( 'action' );
	   alert($form.serialize());
	   /* Send the data using post and put the results in a div */
	   $.post( url, $form.serialize(),
	      function( data ) {
	         var content = $( data ).find( '#content' );
		 $( "#result" ).empty().append( content );
	      },
	      "json"
	   );
	});

});