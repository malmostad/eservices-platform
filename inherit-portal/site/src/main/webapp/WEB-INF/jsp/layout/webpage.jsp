<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file="/WEB-INF/jspf/htmlTags.jspf"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta charset="utf-8">
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="description" content="Inherit Portal Demo site">
			<meta name="author" content="Björn Molin">
				<hst:headContributions categoryExcludes="scripts" />
				<link rel="stylesheet"
					href="<hst:link path="/css/bootstrap.min.css"/>" type="text/css" />
				<link rel="stylesheet"
					href="<hst:link path="/css/bootstrap-responsive.min.css"/>"
					type="text/css" />
				<link rel="stylesheet"
					href="<hst:link path="/css/jquery.dataTables.css"/>"
					type="text/css" />
				<link rel="stylesheet"
					href="<hst:link path="css/cupertino/jquery-ui-1.8.18.custom.css"/>"
					type="text/css" />
				<script type="text/javascript"
					src="<hst:link path="/js/bootstrap.js"/>"></script>
				<script type="text/javascript"
					src="<hst:link path="/js/jquery-1.7.1.min.js"/>"></script>
				<script type="text/javascript"
					src="<hst:link path="/js/jquery-ui-1.8.18.custom.min.js"/>"></script>
				<script
					src="http://datatables.net/download/build/jquery.dataTables.min.js"></script>

				<script type="text/javascript" charset="utf-8">
					$(document).ready(function() {
						$('.dataTable').dataTable({
							"bPaginate" : false,
							"bLengthChange" : false,
							"bFilter" : true,
							"bSort" : true,
							"bInfo" : false,
							"bAutoWidth" : false
						});
					});
				</script>
				<link rel="stylesheet"
					href="<hst:link path="/css/style.css"/>" type="text/css" />
</head>
<body>
	<div class="container-fluid">
		<hst:include ref="header" />
		<hst:include ref="main" />
	</div>
	<hst:headContributions categoryIncludes="scripts" />
</body>
</html>
