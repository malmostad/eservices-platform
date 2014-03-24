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

import java.util.List;

import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inheritsource.portal.components.BaseComponent;
import org.inheritsource.portal.componentsinfo.PageableListInfo;
import org.inheritsource.service.common.domain.InboxTaskItem;
import org.inheritsource.service.common.domain.StartForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO remove FormOverview.java and PageableListInfo deps???
@ParametersInfo(type = PageableListInfo.class)
public class FormOverview extends MyCasesBaseComponent {

    public static final Logger log = LoggerFactory.getLogger(FormOverview.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
              
       List<StartForm> startForms = engine.getStartForms("prod", request.getLocale());
       
       request.setAttribute("startForms", startForms);
    }

}
