<g:set var="lid" value="${it?.id}"/>
<td>
  <g:hiddenField name="ver-${lid}" value="${it?.version}" />
  <g:field name="procdefVer-${lid}" type="number" min="0" value="${it?.procdefVer}" required=""/>
</td>
<td>${fieldValue(bean: it, field: "actdefName")}</td>
<td>${fieldValue(bean: it, field: "actdefId")}</td>
<td>${fieldValue(bean: it, field: "locale")}</td>
<td>
  <g:textField name="label-${lid}" maxlength="255" value="${it?.label}"/>
</td>
