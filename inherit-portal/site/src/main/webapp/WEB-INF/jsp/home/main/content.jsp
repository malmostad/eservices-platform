<%@ include file="/WEB-INF/jspf/htmlTags.jspf"%>
<%--@elvariable id="document" type="se.inherit.portal.beans.TextDocument"--%>
<%--@elvariable id="headTitle" type="java.lang.String"--%>

<c:choose>
	<c:when test="${empty document}">
		<tag:pagenotfound />
	</c:when>
	<c:otherwise>
		<c:if test="${not empty document.title}">
			<hst:element var="headTitle" name="title">
				<c:out value="${document.title}" />
			</hst:element>
			<hst:headContribution keyHint="headTitle" element="${headTitle}" />
		</c:if>

		<div class="hero-unit">
			<h2>Open Source? Yes box no joke!</h2>
			<p>The BPMN Powered Portal Solution from Inherit - based on multi
				channel, multi language HippoCMS and Bonita Open Solution</p>
			<!--  p> <img alt="osi_standard_logo" src="/site/images/osi_standard_logo.png" /></p -->
			<p>
				<a class="btn btn-primary" href="#"><i
					class="icon-info-sign icon-white"></i>&nbsp;More&nbsp;info</a>
			</p>
		</div>

		<div class="page-header">
			<h1>${document.title}</h1>
		</div>
		<p>${document.summary}</p>
		<p>
			<hst:html hippohtml="${document.html}" />
		</p>

		<div class="row-fluid">

			<div class="span4">
				<div class="page-header span4">
					<h2>Open Source</h2>
				</div>
				<p>We belive bla bla bla bla</p>

			</div>

			<div class="span4">
				<div class="page-header span4">
					<h2>Try it</h2>
				</div>
				<p>This is a default installed demo server. Sign up for a login
					and explore the solution.</p>

			</div>

			<div class="span4">
				<div class="page-header span4">
					<h2>Try us</h2>
				</div>
				<p>In the cloud or at your own server - we will support - no
					lock in</p>

			</div>

		</div>
		<div class="row-fluid">

			<div class="span4">
				<p>
					<a class="btn btn-primary" href="#">Join&nbsp;movement</a>
				</p>
			</div>
			<div class="span4">
				<p>
					<a class="btn btn-primary" href="#"><i
						class="icon-pencil icon-white"></i>&nbsp;Sign&nbsp;up</a>
				</p>
			</div>
			<div class="span4">
				<p>
					<a class="btn btn-primary" href="#"><i
						class="icon-info-sign icon-white"></i>&nbsp;More&nbsp;info</a>
				</p>
			</div>

		</div>

	</c:otherwise>
</c:choose>