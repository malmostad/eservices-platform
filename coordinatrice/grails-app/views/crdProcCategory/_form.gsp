<%@ page import="org.motrice.coordinatrice.CrdProcCategory" %>
<div class="fieldcontain ${hasErrors(bean: crdProcCategoryInst, field: 'name', 'error')} required">
  <label for="name">
    <g:message code="crdProcCategory.name.label" default="Name" />
    <span class="required-indicator">*</span>
  </label>
  <g:textField name="name" maxlength="200" required="" value="${crdProcCategoryInst?.name}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: crdProcCategoryInst, field: 'description', 'error')} ">
  <label for="description">
    <g:message code="crdProcCategory.description.label" default="Description" />
    
  </label>
  <g:textField name="description" value="${crdProcCategoryInst?.description}"/>
</div>
