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
 
<!DOCTYPE html>
<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>
<html>
  <head>
    <meta charset='utf-8'/>
    <meta content='width=device-width, initial-scale=1.0' name='viewport'/>
    <meta content='IE=edge' http-equiv='X-UA-Compatible'/>
    <!--[if lte IE 8]><script src='//assets.malmo.se/internal/3.0/html5shiv-printshiv.js' type='text/javascript'></script><![endif]-->
    <link href='//assets.malmo.se/internal/3.0/malmo.css' media='all' rel='stylesheet' type='text/css'/>
    <!--[if lte IE 7]><link href='//assets.malmo.se/internal/3.0/legacy/ie7.css' media='all' rel='stylesheet' type='text/css'/><![endif]-->

    <!-- motrice css -->
    <hst:headContributions categoryExcludes="scripts" />
    <hst:link var="link" path="/css/motrice-komin.css"/>
    <link rel="stylesheet" href="${link}" type="text/css"/>
    
     <link rel="stylesheet" href="/site/js/jquery-ui-1.11.1.custom/jquery-ui.css" type="text/css"/>
    <link rel='stylesheet' href='<hst:link path="/css/platform/inherit-platform.css"/>' type='text/css'/>

    <!-- end of motrice css -->

    <link rel='icon' type='image/x-icon' href='//assets.malmo.se/internal/3.0/favicon.ico'/>
  </head>

  <body class='malmo-masthead-dashboard development'>

  <div class='app-title'><fmt:message key="mycases.motriceapp.lbl"/></div>

    <div class="motrice-grid">
      <hst:include ref="header"/>
      <hst:include ref="main"/>
    </div>

    <script src='//assets.malmo.se/internal/3.0/malmo.js'></script>
   
<!-- start motrice javascript --> 

     <hst:headContributions categoryIncludes="scripts"/>

    <script type='text/javascript' src='/site/js/jquery-ui-1.11.1.custom/jquery-ui.min.js'></script>
    <script type='text/javascript' src='<hst:link path="/js/platform/inherit-platform.js"/>'></script>
    
    <link rel='stylesheet' href='/site/js/tag-it/css/jquery.tagit.css' type='text/css'/>
    <link href='/site/js/tag-it/css/tagit.ui-zendesk.css' rel='stylesheet' type='text/css'>
    <script type='text/javascript' src='/site/js/tag-it/js/tag-it.min.js'></script>

    <script type='text/javascript' src='<hst:link path="/js/form/jquery.form.min.js"/>'></script>
    <script type='text/javascript' src='<hst:link path="/js/platform/motrice-internal.js"/>'></script>
     <!-- end of motrice javascript --> 

  </body>
</html>

