package org.inheritsource.portal.domain;

import org.hippoecm.hst.core.linking.HstLink;

public class NavigationItem {

	public String title;
    public HstLink link;

    public NavigationItem(HstLink link, String title) {
        this.link = link;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public HstLink getLink() {
        return link;
    }
    
}
