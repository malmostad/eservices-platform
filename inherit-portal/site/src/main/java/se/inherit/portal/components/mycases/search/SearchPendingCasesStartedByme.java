package se.inherit.portal.components.mycases.search;

import org.inherit.service.common.domain.PagedProcessInstanceSearchResult;
import org.inherit.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
