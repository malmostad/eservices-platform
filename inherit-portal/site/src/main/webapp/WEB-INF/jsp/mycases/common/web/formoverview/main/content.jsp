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
		
<div id="tab-element-container">
    <ul>
       <li><a href="#tab-element-1">Blanketter & tj&auml;nster</a></li>
       <li><a href="#tab-element-2">Hj&auml;lp & information</a></li>
    </ul>
	<div id="tab-element-1">
		<div class="tab-content">
		
			<table class="e-services">
			  <thead>
			    <tr>
			      <th id="case-name"><fmt:message key="mycases.service.lbl"/></th>
			      <th id="case-description" colspan="3"><fmt:message key="mycases.select.lbl"/></th>      
			    </tr>
			  </thead>
			  <tbody>
			
			   <c:forEach var="item" items="${startForms}">
			
					<tr>
				      <td headers="case-name">
				         ${item.label}
				      </td>
				      <td class="select">
				      </td>
				      <td>
				      </td>
				      <td>
				      	<c:if test="${not empty item.relativePageLink}">
							<a href="${item.relativePageLink}"><fmt:message key="mycases.eservice"/></a>
						</c:if>
				      </td>
				    </tr>
			        <!-- tr>
			          <td colspan=4>
			          </td>
			        </tr-->
				</c:forEach>          
			</tbody>
			</table>
			
		</div>
	</div>

	<div id="tab-element-2">
		<div class="tab-content">
		
		</div>
	</div>

</div>

