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
 
package org.inheritsource.portal.components.mycases;

import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inheritsource.portal.components.BaseComponent;
import org.inheritsource.portal.componentsinfo.PageableListInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO remove FormOverview.java and PageableListInfo deps???
@ParametersInfo(type = PageableListInfo.class)
public class FormOverview extends BaseComponent {

    public static final Logger log = LoggerFactory.getLogger(FormOverview.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {

       PageableListInfo info = getParametersInfo(request);
       HippoBean scope = getContentBean(request);
       
       if(scope == null) {
           response.setStatus(404);
           throw new HstComponentException("For an Overview component there must be a content bean available to search below. Cannot create an overview");
       }
       
       loadImage(request, "wordIcon", "/content/gallery/inheritportal/doc_word.png");
       loadImage(request, "pdfIcon", "/content/gallery/inheritportal/doc_pdf.png");

       createAndExecuteSearch(request, info, scope, null);
    }
    
    private void loadImage(final HstRequest request, String attributeName, String imagePath) {
    	Object image = null;
        try {
        	image = getObjectBeanManager(request).getObject(imagePath);
            if (image instanceof HippoGalleryImageSet) {
                request.setAttribute(attributeName, image);
            } else {
                log.warn("Mount '{}' has illegal logo path '{}' (not an image set). No logo will be shown.");
            }
        } catch (ObjectBeanManagerException e) {
            log.warn("Cannot retrieve logo at '" + imagePath + "'", e);
        }
    }

}
