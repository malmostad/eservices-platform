<%@ page import="org.motrice.signatrice.SigCustomElement" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
      <g:set var="entityName" value="${message(code: 'sigCustomElement.label', default: 'SigCustomElement')}" />
      <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <a href="#list-sigCustomElement" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
    <div id="list-sigCustomElement" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
	<thead>
	  <tr>
	    <g:sortableColumn property="elementName" title="${message(code: 'sigCustomElement.elementName.label', default: 'Xml Id')}" />
	    <g:sortableColumn property="idAttrName" title="${message(code: 'sigCustomElement.idAttrName.label', default: 'IdAttrName')}" />
	  </tr>
	</thead>
	<tbody>
	  <g:each in="${sigCustomElementObjList}" status="i" var="sigCustomElementObj">
	    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
	      <td><g:link action="show" id="${sigCustomElementObj.id}">${fieldValue(bean: sigCustomElementObj, field: "elementName")}</g:link></td>
	      <td>${fieldValue(bean: sigCustomElementObj, field: "idAttrName")}</td>
	    </tr>
	  </g:each>
	</tbody>
      </table>
      <div class="pagination">
	<g:paginate total="${sigCustomElementObjTotal}" />
      </div>
    </div>
  </body>
</html>
