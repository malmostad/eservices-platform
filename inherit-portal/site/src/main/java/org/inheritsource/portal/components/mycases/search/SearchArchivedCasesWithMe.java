package org.inheritsource.portal.components.mycases.search;

import org.inheritsource.service.common.domain.PagedProcessInstanceSearchResult;
import org.inheritsource.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchArchivedCasesWithMe extends BaseSearchCasesComponent {

	public static final Logger log = LoggerFactory.getLogger(SearchArchivedCasesWithMe.class);
	

	@Override
	public PagedProcessInstanceSearchResult executeSearch(
			String searchStr, int fromIndex, int pageSize, String sortBy,
			String sortOrder, String filter, String userId) {
		
        InheritServiceClient isc = new InheritServiceClient();
        // search cases with me => throw away searchStr and search for logged on user's userId instead
        PagedProcessInstanceSearchResult searchResult = isc.searchProcessInstancesWithInvolvedUser(userId, fromIndex, pageSize, sortBy, sortOrder,  "FINISHED",  userId);

		return searchResult;
	}

}
