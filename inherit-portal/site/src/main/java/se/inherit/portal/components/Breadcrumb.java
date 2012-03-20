package se.inherit.portal.components;

import java.util.ArrayList;
import java.util.List;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.sitemenu.HstSiteMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Breadcrumb extends BaseHstComponent {

    public static final Logger log = LoggerFactory.getLogger(Breadcrumb.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        HippoBean doc = getContentBean(request);
        request.setAttribute("document",doc);

        // breadcrumb
    	HstSiteMenu menu = request.getRequestContext().getHstSiteMenus().getSiteMenu("main");
        ArrayList<HstSiteMenuItem> breadcrumb = new ArrayList<HstSiteMenuItem>();
        List<HstSiteMenuItem> currentMenuItems = menu.getSiteMenuItems();
        boolean finished = false;
        while (currentMenuItems != null && !finished) {
        	HstSiteMenuItem foundItem = null;
        	int N = currentMenuItems.size();
        	int index = 0;
        	while (foundItem == null && index<N) {
        		HstSiteMenuItem currentMenuItem = currentMenuItems.get(index);

        		if (currentMenuItem.isExpanded() || currentMenuItem.isSelected()) {
        			foundItem = currentMenuItem;
        		}
        		index++;
        	}
        	
        	if (foundItem == null) {
        		finished = true;
        	}
        	else {
    			breadcrumb.add(foundItem);
    			if (foundItem.isSelected()) {
    				finished = true;
    			}
    			else {
    				currentMenuItems = foundItem.getChildMenuItems();
    			}
        	}
        }
        request.setAttribute("breadcrumb", breadcrumb);

    }

}
