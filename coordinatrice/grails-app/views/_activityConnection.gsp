<g:set var="isHuman" value="${actDefInst?.userTask}"/>
<g:if test="${!isHuman}">
  <g:set var="statetitle"><g:message code="activity.connection.disabled"/></g:set>
  <td><g:img uri="/images/silk/lock.png" title="${statetitle}"/></td><td></td>
</g:if>
<g:elseif test="${connection==null || connection?.state == org.motrice.coordinatrice.ActivityConnection.UNCONNECTED_STATE}">
  <g:set var="statetitle"><g:message code="activity.connection.undefined"/></g:set>
  <td><g:img uri="/images/silk/exclamation.png" title="${statetitle}"/></td><td></td>
</g:elseif>
<g:else>
  <g:set var="statetitle"><g:message code="activity.connection.defined"/></g:set>
  <td><g:img uri="/images/silk/accept.png" title="${statetitle}"/> <g:message code="${connection?.msgKey()}"/></td>
  <td>
    <g:if test="${connection?.path != null}">
      ${connection?.toDisplay()?.encodeAsHTML()}
    </g:if>
  </td>
</g:else>
<td>
  <g:if test="${isHuman}">
    <g:link class="edit" controller="actDef" action="edit" id="${connection?.activity?.fullId}">
      <g:img uri="/images/silk/connect.png" title="${message(code: 'activity.default.edit.label', default: 'Edit')}"/>
    </g:link>
  </g:if>
</td>
