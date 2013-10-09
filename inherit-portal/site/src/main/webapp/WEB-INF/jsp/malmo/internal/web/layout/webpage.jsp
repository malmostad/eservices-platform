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
    mail: Inherit S AB, LÃ¥ngsjÃ¶vÃ¤gen 8, SE-131 33 NACKA, SWEDEN 
    phone: +46 8 641 64 14 
 --%> 
 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
    <hst:headContributions categoryExcludes="scripts" />
    <hst:link var="link" path="/css/style.css"/>
    <link rel="stylesheet" href="${link}" type="text/css"/>

	<!--  Malmö internal services assets-2.0 begin -->    
    <link href="https://www.malmo.se/assets-2.0/css/internal-core.css" rel="stylesheet" type="text/css" media="all" />
	<link href="https://www.malmo.se/assets-2.0/jquery/malmo-theme.css" rel="stylesheet" type="text/css" media="all" />
	<link href="https://www.malmo.se/assets-2.0/css/malmo-print.css" rel="stylesheet" type="text/css" media="print" />
	<!--[if lt IE 7]><link href="http://www.malmo.se/assets-2.0/css/malmo-ie-css-fix.css" rel="stylesheet" type="text/css" media="all" /><![endif]-->
	<!--[if IE 7]><link href="http://www.malmo.se/assets-2.0/css/malmo-ie7-css-fix.css" rel="stylesheet" type="text/css" media="all" /><![endif]-->
	<link rel="shortcut icon" href="http://www.malmo.se/assets-2.0/img/malmo-favicon.ico" type="image/x-icon" />
	<script src="https://www.malmo.se/assets-2.0/jquery/jquery.js" type="text/javascript"></script>
	<script src="https://www.malmo.se/assets-2.0/js/malmo.js" type="text/javascript"></script>
	<script src="https://www.malmo.se/assets-2.0/js/internal.js" type="text/javascript"></script>
	<!--  Malmö assets-2.0 end -->    
    <script language="javascript" type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
    <script language="javascript" type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/jquery-ui.min.js"></script>
    
    <link rel="stylesheet" href="<hst:link path="/css/platform/inherit-platform.css"/>" type="text/css"/>
    <script type="text/javascript" src="<hst:link path="/js/platform/inherit-platform.js"/>"></script>
    
    <link rel="stylesheet" href="/site/js/tag-it/css/jquery.tagit.css" type="text/css"/>
    <link href=""/site/js/tag-it/css/tagit.ui-zendesk.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/site/js/tag-it/js/tag-it.min.js"></script>
    
  </head>
  <body>
  	<div class="wrap-all">
    <hst:include ref="header"/>
    <hst:include ref="main"/>
    <hst:headContributions categoryIncludes="scripts"/>
    </div>
  </body>
</html>
