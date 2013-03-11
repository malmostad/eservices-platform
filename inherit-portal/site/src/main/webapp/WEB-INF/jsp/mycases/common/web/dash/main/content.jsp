<%@ include file="/WEB-INF/jspf/htmlTags.jspf" %>

<h1>Instrumentpanel</h1>
<p></p>

<table>
  <thead>
    <tr>
      <th>F&ouml;ljsamhet till tidplan</th>
      <th>Antal &auml;renden</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><fmt:message key="mycases.dash.ontrack.lbl"/></td>
      <td>${dash.onTrack}</td>
    </tr>
    <tr>
      <td><fmt:message key="mycases.dash.atrisk.lbl"/></td>
      <td>${dash.atRisk}</td>
    </tr>
    <tr>
      <td><fmt:message key="mycases.dash.overdue.lbl"/></td>
      <td>${dash.overdue}</td>
    </tr>
  </tbody>
</table>
