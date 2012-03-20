<%@ include file="/WEB-INF/jspf/htmlTags.jspf"%>

<div class="row-fluid">
	<div class="span12">
		<hst:include ref="mainmenu" />
	</div>
</div>

<div id="main" class="row-fluid">
	<div class="span3">
		<hst:include ref="submenus" />
	</div>
	<div class="span9">

		<hst:include ref="breadcrumb" />

		<div class="row-fluid">
		
			<div id="content" class="span12">
				<hst:include ref="content" />
				<!-- the lists is a general 'slot' where items can be dropped in -->
				<hst:include ref="lists" />
			</div>
			<!-- 
			<div id="right" class="span4">
				<hst:include ref="right" />
			</div>
 			-->
		</div>
	</div>
</div>

