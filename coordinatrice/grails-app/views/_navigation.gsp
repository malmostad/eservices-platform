<div class="mainmenu" role="navigation">
  <ul id="menu">
    <li><a href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a>
      <ul class="submenu">
	<li><g:link controller="procdef" action="showconfig"><g:message code="config.show.menu"/></g:link></li>
      </ul>
    </li>
    <li><g:link controller="procdef" action="list"><g:message code="procdef.menu"/></g:link>
	<ul class="submenu">
	  <li><g:link controller="procdef" action="list"><g:message code="procdef.list.menu"/></g:link></li>
	  <li><g:link controller="procdef" action="create"><g:message code="procdef.upload.bpmn.label"/></g:link></li>
	  <li><g:link controller="crdProcCategory" action="list"><g:message code="crdProcCategory.list.menu"/></g:link></li>
	  <li><g:link class="create" action="create"><g:message code="crdProcCategory.create.menu"/></g:link></li>
	</ul>
    </li>
    <li><g:link controller="mtfStartFormDefinition" action="list"><g:message code="pxdFormdef.menu"/></g:link>
	<ul class="submenu">
	  <li><g:link controller="mtfStartFormDefinition" action="list"><g:message code="startform.list.label"/></g:link></li>
	  <li><g:link controller="pxdFormdef" action="list"><g:message code="pxdFormdef.list.label"/></g:link></li>
	  <li><a class="create" href="${orbeonUri}/new" target="_"><g:message code="pxdFormdef.create.menu"/></a></li>
	</ul>
    </li>
    <li><g:link controller="migPackage" action="list"><g:message code="migration.menu"/></g:link>
	<ul class="submenu">
	  <li><g:link controller="migPackage" action="list"><g:message code="migPackage.list.menu"/></g:link></li>
	  <li><g:link controller="migPackage" action="uploadprepare"><g:message code="migPackage.upload.menu"/></g:link></li>
	  <li><g:link controller="migPackage" action="listexp"><g:message code="migPackage.create.menu"/></g:link></li>
	</ul>
    </li>
  </ul>
</div>
