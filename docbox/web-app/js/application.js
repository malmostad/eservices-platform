/* == Motrice Copyright Notice ==
 * Motrice Service Platform. Copyright (C) 2011-2014 Motrice AB
 */
if (typeof jQuery !== 'undefined') {
	(function($) {
		$('#spinner').ajaxStart(function() {
			$(this).fadeIn();
		}).ajaxStop(function() {
			$(this).fadeOut();
		});
	})(jQuery);
}
