package se.inherit.portal.components;

import java.util.ArrayList;
import java.util.List;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.sitemenu.HstSiteMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenuItem;

public class LeftMenu  extends BaseHstComponent{

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
    	// menu
    	HstSiteMenu menu = request.getRequestContext().getHstSiteMenus().getSiteMenu("main");
        request.setAttribute("menu", menu);
    }

}
