<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
    <hst:headContributions categoryExcludes="scripts" />
    <hst:link var="link" path="/css/style.css"/>
    <link rel="stylesheet" href="${link}" type="text/css"/>

	<!--  Malmö external services assets-2.0 begin -->    
    <link href="http://www.malmo.se/assets-2.0/css/external-core.css" rel="stylesheet" type="text/css" media="all" />
	<link href="http://www.malmo.se/assets-2.0/jquery/malmo-theme.css" rel="stylesheet" type="text/css" media="all" />
	<link href="http://www.malmo.se/assets-2.0/css/malmo-print.css" rel="stylesheet" type="text/css" media="print" />
	<!--[if lt IE 7]><link href="http://www.malmo.se/assets-2.0/css/malmo-ie-css-fix.css" rel="stylesheet" type="text/css" media="all" /><![endif]-->
	<!--[if IE 7]><link href="http://www.malmo.se/assets-2.0/css/malmo-ie7-css-fix.css" rel="stylesheet" type="text/css" media="all" /><![endif]-->
	<link rel="shortcut icon" href="http://www.malmo.se/assets-2.0/img/malmo-favicon.ico" type="image/x-icon" />
	<script src="http://www.malmo.se/assets-2.0/jquery/jquery.js" type="text/javascript"></script>
	<script src="http://www.malmo.se/assets-2.0/js/malmo.js" type="text/javascript"></script>
	<script src="http://www.malmo.se/assets-2.0/js/external.js" type="text/javascript"></script>
	<script src="http://s7.addthis.com/js/250/addthis_widget.js" type="text/javascript"></script> 
	<!--  Malmö assets-2.0 end -->    
    
  </head>
  <body>
  	<div class="wrap-all-expanding">
    
    <hst:include ref="header"/>
    <hst:include ref="main"/>
    <hst:headContributions categoryIncludes="scripts"/>
    </div>
  </body>
</html>
