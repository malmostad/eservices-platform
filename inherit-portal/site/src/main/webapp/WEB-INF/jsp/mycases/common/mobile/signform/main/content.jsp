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
 

<%@ include file="/WEB-INF/jspf/htmlTags.jspf"%>
<%--@elvariable id="document" type="org.inheritsource.portal.beans.TextDocument"--%>

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

		<div class="row-fluid">
			<div class="span12">

				<c:if test="${not empty guide}">
					<h1>${guide.title}</h1>
					<p>${guide.summary}</p>
					<hst:html hippohtml="${guide.html}" />
				</c:if>

				<c:if test="${not empty document}">
					<h1>${document.title}</h1>
					<p>${document.summary}</p>
					<hst:html hippohtml="${document.html}" />
				</c:if>

				<table>
				  <thead>
				    <tr>
				      <th>Dokument</th>
				      <th>Kontrollsumma</th>
				    </tr>
				  </thead>
				  <tbody>
				    <tr>
				    	<td><a href="${pdfUrl}">${docNo}</a></td>
				    	<td>${pdfChecksum}</td>
				    </tr>
				  </tbody>
				</table>					    

<!--
				<form method="post" action="https://idp.test.malmo.se/wa/auth?authmech=e-id-signer">
					<input type="hidden" name="text" value="${signText}" />
					<input type="hidden" name="location" value="${responseUrl}" /> <input
						type="submit" value="G&aring; till signering"/>
				</form>
-->
<p>
<b>Obs!</b> Mobilt bankid &auml;r inte aktiverat i Malm&ouml; stad. Signering m&aring;ste d&auml;rf&ouml;r  utf&auml;ras vid dator med BankID. 
</p>
			</div>
		</div>

	</c:otherwise>
</c:choose>
