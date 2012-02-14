<%@ include file="/WEB-INF/jspf/htmlTags.jspf"%>

<div data-role="page">

	<div data-role="header" data-position="fixed">
		<h1>Inherit portal</h1>
		<hst:include ref="mainmenu" />
	</div>
	<!-- /header -->

	<div data-role="content">
		<hst:include ref="content" />
	</div>
	<div data-role="footer" data-position="fixed">
		<hst:include ref="submenus" />
	</div>
	<!-- /footer -->
</div>
