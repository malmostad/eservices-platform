package se.inherit.portal.components.mycases.search;

import org.inherit.service.common.domain.PagedProcessInstanceSearchResult;
import org.inherit.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchCasesStartedByUserId extends BaseSearchCasesComponent {

	public static final Logger log = LoggerFactory.getLogger(SearchCasesStartedByUserId.class);

	@Override
	public PagedProcessInstanceSearchResult executeSearch(
			String searchForUserId, int fromIndex, int pageSize, String sortBy,
			String sortOrder, String filter, String userId) {
		
        InheritServiceClient isc = new InheritServiceClient();
        PagedProcessInstanceSearchResult searchResult = isc.searchProcessInstancesStartedByUser(searchForUserId, fromIndex, pageSize, sortBy, sortOrder,  filter,  userId);

		return searchResult;
	}
}
