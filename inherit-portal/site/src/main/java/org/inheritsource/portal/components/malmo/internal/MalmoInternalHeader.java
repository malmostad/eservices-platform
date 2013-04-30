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
 
package org.inheritsource.portal.components.malmo.internal;



import org.hippoecm.hst.configuration.hosting.Mount;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inheritsource.portal.channels.WebsiteInfo;
import org.inheritsource.portal.components.mycases.UserSessionHeader;
import org.inheritsource.service.common.util.UrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MalmoInternalHeader extends UserSessionHeader {

    public static final Logger log = LoggerFactory.getLogger(MalmoInternalHeader.class);

    public static final String MAST_HEAD_URL = "http://www.malmo.se/assets-2.0/remote/internal-masthead/";
    
    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
    	super.doBeforeRender(request, response);
        final Mount mount = request.getRequestContext().getResolvedMount().getMount();
        
        // TODO replace with Malmo internal channel info
        final WebsiteInfo info = mount.getChannelInfo();

        if (info != null) {
            request.setAttribute("headerName", info.getHeaderName());
        } else {
            log.warn("No channel info available for website '{}'", mount.getMountPath());
        }
        
        String mastHead = UrlUtil.loadFromUrl(MAST_HEAD_URL);
        //log.error("mastHead=" + mastHead);
        request.setAttribute("mastHead", mastHead.replaceAll("//komin.malmo.se/assets-2.0/img/komin-logo.png", "http://www.malmo.se/assets-2.0/img/komin-logo.png"));
		
    }

}
