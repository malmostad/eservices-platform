<%@ page import="org.motrice.orifice.OriPackage" %>

<div class="fieldcontain ${hasErrors(bean: oriPackageInst, field: 'siteName', 'error')} ">
  <label for="siteName">
    <g:message code="oriPackage.siteName.label" default="Site Name"/>
  </label>
  <g:textField name="siteName" maxlength="120" value="${oriPackageInst?.siteName}" readonly="readonly"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriPackageInst, field: 'packageName', 'error')} ">
  <label for="packageName">
    <g:message code="oriPackage.packageName.label" default="Package Name" />
    <span class="required-indicator">*</span>
  </label>
  <g:textField name="packageName" maxlength="120" value="${oriPackageInst?.packageName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriPackageInst, field: 'originLocal', 'error')} ">
  <label for="originLocal">
    <g:message code="oriPackage.originLocal.label" default="Origin Local" />
  </label>
  <g:checkBox name="originLocal" value="${oriPackageInst?.originLocal}" readonly="readonly"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriPackageInst, field: 'packageFormat', 'error')} ">
  <label for="packageFormat">
    <g:message code="oriPackage.packageFormat.label" default="Package Format" />
  </label>
  <g:textField name="packageFormat" value="${oriPackageInst?.packageFormat}" readonly="readonly"/>
</div>

<div class="fieldcontain ${hasErrors(bean: oriPackageInst, field: 'siteTstamp', 'error')} required">
  <label for="siteTstamp">
    <g:message code="oriPackage.siteTstamp.label" default="Site Tstamp" />
  </label>
  <g:tstamp date="${oriPackageInst?.siteTstamp}"/>
</div>
