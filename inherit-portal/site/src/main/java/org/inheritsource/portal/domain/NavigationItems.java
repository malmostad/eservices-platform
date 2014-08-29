package org.inheritsource.portal.domain;

import java.util.ArrayList;
import java.util.List;

public class NavigationItems {

	private List<NavigationItem> items;
	
	public NavigationItems() {
		items = new ArrayList<NavigationItem>();
	}

	public List<NavigationItem> getItems() {
		return items;
	}

	public void setItems(List<NavigationItem> items) {
		this.items = items;
	}

}
