/* 
 *  Process Aware Web Application Platform 
 * 
 *  Copyright (C) 2011-2013 Inherit S AB 
 * 
 *  This program is free software: you can redistribute it and/or modify 
 *  it under the terms of the GNU Affero General Public License as published by 
 *  the Free Software Foundation, either version 3 of the License, or 
 *  (at your option) any later version. 
 * 
 *  This program is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *  GNU Affero General Public License for more details. 
 * 
 *  You should have received a copy of the GNU Affero General Public License 
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 * 
 *  e-mail: info _at_ inherit.se 
 *  mail: Inherit S AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN 
 *  phone: +46 8 641 64 14 
 */ 
 
package org.inheritsource.portal.components;

import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.util.PathUtils;
import org.inheritsource.portal.componentsinfo.ListViewInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ParametersInfo(type = ListViewInfo.class)
public class ListView extends BaseComponent {

    public static final Logger log = LoggerFactory.getLogger(ListView.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {

       ListViewInfo info = getParametersInfo(request);
       HippoBean scopeBean = null;

       String scope = info.getScope();
       if(scope == null) {
           throw new HstComponentException("Scope is not allowed to be null for a List component. Cannot create a list");
       }

       scopeBean = getSiteContentBaseBean(request);
       if("".equals(scope) || "/".equals(scope)) {
           // the scope is the root content bean of this site, scopeBean is already ok.
       } else {
           // strip leading and trailing slashes
           scope = PathUtils.normalizePath(scope);
           scopeBean = scopeBean.getBean(scope);
           if(scopeBean == null) {
               throw new HstComponentException("Scope '" + scope
                       + "' does not point to a bean for Mount with content path '"
                       + request.getRequestContext().getResolvedMount().getMount().getContentPath()
                       + "'. Cannot create a list");
           }
       }

       if(scope == null) {
           response.setStatus(404);
           throw new HstComponentException("For an Overview component there must be a content bean available to search below. Cannot create an overview");
       }
       createAndExecuteSearch(request, info, scopeBean, null);
    }

}
