package se.inherit.portal.components.mycases.search;

import org.inherit.service.common.domain.PagedProcessInstanceSearchResult;
import org.inherit.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchCasesByTagValue extends BaseSearchCasesComponent {

	public static final Logger log = LoggerFactory.getLogger(SearchCasesByTagValue.class);

	@Override
	public PagedProcessInstanceSearchResult executeSearch(
			String searchStr, int fromIndex, int pageSize, String sortBy,
			String sortOrder, String filter, String userId) {
		InheritServiceClient isc = new InheritServiceClient();
        PagedProcessInstanceSearchResult searchResult = isc.searchProcessInstancesByTagValue(searchStr, fromIndex, pageSize, sortBy, sortOrder, filter, userId);

		return searchResult;
	}

}
