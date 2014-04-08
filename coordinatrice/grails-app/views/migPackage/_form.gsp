<%@ page import="org.motrice.migratrice.MigPackage" %>

<div class="fieldcontain ${hasErrors(bean: migPackageInst, field: 'siteName', 'error')} ">
  <label for="siteName">
    <g:message code="migPackage.siteName.label" default="Site Name"/>
  </label>
  <g:textField name="siteName" maxlength="120" value="${migPackageInst?.siteName}" readonly="readonly"/>
</div>

<div class="fieldcontain ${hasErrors(bean: migPackageInst, field: 'packageName', 'error')} ">
  <label for="packageName">
    <g:message code="migPackage.packageName.label" default="Package Name" />
    <span class="required-indicator">*</span>
  </label>
  <g:textField name="packageName" maxlength="120" value="${migPackageInst?.packageName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: migPackageInst, field: 'originLocal', 'error')} ">
  <label for="originLocal">
    <g:message code="migPackage.originLocal.label" default="Origin Local" />
  </label>
  <g:checkBox name="originLocal" value="${migPackageInst?.originLocal}" readonly="readonly"/>
</div>

<div class="fieldcontain ${hasErrors(bean: migPackageInst, field: 'packageFormat', 'error')} ">
  <label for="packageFormat">
    <g:message code="migPackage.packageFormat.label" default="Package Format" />
  </label>
  <g:textField name="packageFormat" value="${migPackageInst?.packageFormat}" readonly="readonly"/>
</div>

<div class="fieldcontain ${hasErrors(bean: migPackageInst, field: 'siteTstamp', 'error')} required">
  <label for="siteTstamp">
    <g:message code="migPackage.siteTstamp.label" default="Site Tstamp" />
  </label>
  <g:tstamp date="${migPackageInst?.siteTstamp}"/>
</div>
