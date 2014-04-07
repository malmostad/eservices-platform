<div class="mainmenu" role="navigation">
  <ul id="menu">
    <li><a href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
    <li><a href="#"><g:message code="procdef.menu"/></a>
	<ul class="submenu">
	  <li><g:link controller="procdef" action="list"><g:message code="procdef.list.menu"/></g:link></li>
	  <li><g:link controller="procdef" action="create"><g:message code="procdef.upload.bpmn.label"/></g:link></li>
	  <li><g:link controller="crdProcCategory" action="list"><g:message code="crdProcCategory.list.menu"/></g:link></li>
	  <li><g:link class="create" action="create"><g:message code="crdProcCategory.create.menu"/></g:link></li>
	</ul>
    </li>
    <li><a href="#"><g:message code="pxdFormdef.menu"/></a>
	<ul class="submenu">
	  <li><g:link controller="MtfStartFormDefinition" action="list"><g:message code="startform.list.label"/></g:link></li>
	  <li><g:link controller="PxdFormdef" action="list"><g:message code="pxdFormdef.list.label"/></g:link></li>
	  <li><a class="create" href="${orbeonUri}/new" target="_"><g:message code="pxdFormdef.create.menu"/></a></li>
	</ul>
    </li>
    <li><a href="#"><g:message code="migration.menu"/></a>
    </li>
  </ul>
</div>
