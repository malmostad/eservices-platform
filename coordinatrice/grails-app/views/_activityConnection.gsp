<g:if test="${connection==null || connection?.state == org.motrice.coordinatrice.ActivityConnection.UNCONNECTED_STATE}">
  <g:set var="statetitle"><g:message code="activity.connection.undefined"/></g:set>
  <td><g:img uri="/images/silk/exclamation.png" title="${statetitle}"/></td><td></td>
</g:if>
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
  <g:link class="edit" controller="bnActDef" action="edit" id="${connection?.activity?.id}">
    <g:img uri="/images/silk/application_form_edit.png" title="${message(code: 'default.button.edit.label', default: 'Edit')}"/>
  </g:link>
</td>
