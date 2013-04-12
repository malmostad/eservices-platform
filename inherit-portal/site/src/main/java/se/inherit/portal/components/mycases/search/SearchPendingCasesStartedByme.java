package se.inherit.portal.components.mycases.search;

import java.util.ArrayList;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inherit.service.common.domain.PagedProcessInstanceSearchResult;
import org.inherit.service.common.domain.ProcessInstanceListItem;
import org.inherit.service.common.domain.UserInfo;
import org.inherit.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inherit.portal.components.mycases.MyCasesBaseComponent;

public class SearchPendingCasesStartedByme extends BaseSearchCasesComponent {

	public static final Logger log = LoggerFactory.getLogger(SearchPendingCasesStartedByme.class);

	@Override
	public PagedProcessInstanceSearchResult executeSearch(
			String searchForUserId, int fromIndex, int pageSize, String sortBy,
			String sortOrder, String filter, String userId) {
		
        InheritServiceClient isc = new InheritServiceClient();
        
        // ignore searchForUserId and search for logged on userId
        PagedProcessInstanceSearchResult searchResult = isc.searchProcessInstancesStartedByUser(userId, fromIndex, pageSize, sortBy, sortOrder,  "STARTED",  userId);

		return searchResult;
	}
}
