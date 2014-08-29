/* == Motrice Copyright Notice == 
 * 
 * Motrice Service Platform 
 * 
 * Copyright (C) 2011-2014 Motrice AB 
 * 
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Affero General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU Affero General Public License for more details. 
 * 
 * You should have received a copy of the GNU Affero General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>. 
 * 
 * e-mail: info _at_ motrice.se 
 * mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN 
 * phone: +46 8 641 64 14 
 
 */ 
 
 
package org.inheritsource.portal.components;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.sitemenu.HstSiteMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenuItem;
import org.inheritsource.portal.domain.NavigationItem;
import org.inheritsource.portal.domain.NavigationItems;

public class Navigation  extends BaseHstComponent{

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
    	HstSiteMenu menu = request.getRequestContext().getHstSiteMenus().getSiteMenu("main");
    	
    	// Start with sub navigation links 
    	NavigationItems subnavigation = new NavigationItems();
    	HstSiteMenuItem selectedItem = menu.getSelectSiteMenuItem();
    	if (selectedItem != null && selectedItem.getChildMenuItems()!=null && selectedItem.getChildMenuItems().size()>0) {
    		for (HstSiteMenuItem child : selectedItem.getChildMenuItems()) {
    			NavigationItem item = new NavigationItem(child.getHstLink(), child.getName());
    			subnavigation.getItems().add(item);
    		}
    	}
    	else {
    		if (menu.getSiteMenuItems() != null) {
    			for (HstSiteMenuItem child : menu.getSiteMenuItems()) {
        			NavigationItem item = new NavigationItem(child.getHstLink(), child.getName());
        			subnavigation.getItems().add(item);
        		}
    		}
    	}
    	
        request.setAttribute("subnavigation", subnavigation);
    }

}
