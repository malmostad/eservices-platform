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
			<h2>100% &Ouml;ppen k&auml;llkod! Inga hemligheter</h2>
			<p>Vår BPMN drivna plattform för e-tjänster Powered Portal Solution from Inherit - based on multi
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
					<h2>&Ouml;ppen k&auml;llkod</h2>
				</div>
				<p>Vi &auml;r &ouml;vertygade om att kraften i ...</p>

			</div>

			<div class="span4">
				<div class="page-header span4">
					<h2>Prova</h2>
				</div>
				<p>Detta är en standardinstallerad server med vår lösning. Registrera dig för ett demo konto.</p>

			</div>

			<div class="span4">
				<div class="page-header span4">
					<h2>Offentlig sektor</h2>
				</div>
				<p>Vi har ramavtal med kammarkollegiet tillsammans med vår partner Pro4u. Det innebär att offentlig sektor kan börja använda vår öppna plattform utan upphandling och avropa våra kvalificerade tjänster på lösningen efter en förnyad konkurrensutsättning.</p>

			</div>

		</div>
		<div class="row-fluid">

			<div class="span4">
				<p>
					<a class="btn btn-primary" href="#">Gå med</a>
				</p>
			</div>
			<div class="span4">
				<p>
					<a class="btn btn-primary" href="#"><i
						class="icon-pencil icon-white"></i>&nbsp;Registrera konto&nbsp;up</a>
				</p>
			</div>
			<div class="span4">
				<p>
					<a class="btn btn-primary" href="#"><i
						class="icon-info-sign icon-white"></i>&nbsp;Mer&nbsp;info</a>
				</p>
			</div>

		</div>

	</c:otherwise>
</c:choose>