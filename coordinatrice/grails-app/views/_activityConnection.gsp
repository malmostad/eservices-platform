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
<g:set var="isHuman" value="${actDefInst?.userTask}"/>
<g:if test="${!isHuman}">
  <g:set var="statetitle"><g:message code="activity.connection.disabled"/></g:set>
  <td><g:img uri="/images/silk/lock.png" title="${statetitle}"/></td><td></td>
</g:if>
<g:elseif test="${!connection || connection?.unconnectedState}">
  <g:set var="statetitle"><g:message code="activity.connection.undefined"/></g:set>
  <td><g:img uri="/images/silk/exclamation.png" title="${statetitle}"/></td><td></td>
</g:elseif>
<g:else>
  <g:set var="statetitle"><g:message code="activity.connection.defined"/></g:set>
  <td><g:img uri="/images/silk/accept.png" title="${statetitle}"/> <g:message code="${connection?.msgKey()}"/></td>
  <td>
    <g:if test="${connection.formState}">
      <g:link controller="pxdFormdefVer" action="show" id="${formdef?.formdefId}">${connection?.toDisplay()?.encodeAsHTML()}</g:link>
    </g:if>
    <g:else>
      ${connection?.toDisplay()?.encodeAsHTML()}
    </g:else>
  </td>
</g:else>
<td>
  <g:if test="${isHuman && procState?.editable}">
    <g:link class="edit" controller="actDef" action="edit" id="${connection?.activity?.fullId}">
      <g:img uri="/images/silk/connect.png" title="${message(code: 'activity.default.edit.label', default: 'Edit')}"/>
    </g:link>
  </g:if>
</td>
