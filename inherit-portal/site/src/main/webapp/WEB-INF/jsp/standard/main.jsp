<%@ include file="/WEB-INF/jspf/htmlTags.jspf"%>



<div class="row-fluid">
	<div class="span12">
		<hst:include ref="mainmenu" />
	</div>
</div>

<div class="container-fluid">

	<div id="main">
		<div class="row-fluid">
			<div class="span3">
				<hst:include ref="submenus" />
			</div>
			<div id="content" class="span6">
				<hst:include ref="content" />
				<!-- the lists is a general 'slot' where items can be dropped in -->
				<hst:include ref="lists" />
			</div>
			<div id="right" class="span3">
				<hst:include ref="right" />
			</div>
		</div>
	</div>

</div>