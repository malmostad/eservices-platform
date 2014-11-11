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
  mail: Motrice AB, LÃ¥ngsjÃ¶vÃ¤gen 8, SE-131 33 NACKA, SWEDEN 
  phone: +46 8 641 64 14 
 
--%> 
 
 
<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset='utf-8'/>
    <meta content='width=device-width, initial-scale=1.0' name='viewport'/>
    
    <!--[if IE]><meta content='IE=edge' http-equiv='X-UA-Compatible'/><![endif]-->
    <!--[if lte IE 8]><script src='//assets.malmo.se/external/v4/html5shiv-printshiv.js' type='text/javascript'></script><![endif]-->
    <link href='//assets.malmo.se/external/v4/malmo.css' media='all' rel='stylesheet' type='text/css'/>
	
	<!-- motrice css -->
	<hst:headContributions categoryExcludes="scripts" />
    <hst:link var='link' path='/css/style.css'/>
    <link rel='stylesheet' href='${link}' type='text/css'/>
    <link rel='stylesheet' href='<hst:link path="/css/platform/inherit-platform.css"/>' type='text/css'/>
    <link rel='stylesheet' href='<hst:link path="/css/motrice-public.css"/>' type='text/css'/>

    <!--[if lte IE 8]><link href='//assets.malmo.se/external/v4/legacy/ie8.css' media='all' rel='stylesheet' type='text/css'/><![endif]-->
    <noscript><link href="//assets.malmo.se/external/v4/icons.fallback.css" rel="stylesheet"></noscript>
    <link rel='icon' type='image/x-icon' href='//assets.malmo.se/external/v4/favicon.ico'/>
  </head>
  <body class="mf-v4 development">

  	<div class="container motrice-grid">
	    <hst:include ref="header"/>
	    <hst:include ref="main"/>    
	</div>
  
  <script src='//assets.malmo.se/external/v4/malmo.js'></script>  
 
  <script type='text/javascript' src='<hst:link path="/js/jquery-ui-1.11.1.custom/jquery-ui.min.js"/>'></script>
  <script type='text/javascript' src='<hst:link path="/js/platform/inherit-platform.js"/>'></script>
  <hst:headContributions categoryIncludes="scripts"/>
  
 </body>
</html>
