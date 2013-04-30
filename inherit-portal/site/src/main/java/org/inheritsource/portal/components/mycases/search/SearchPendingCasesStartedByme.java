/* 
 *  Process Aware Web Application Platform 
 * 
 *  Copyright (C) 2011-2013 Inherit S AB 
 * 
 *  This program is free software: you can redistribute it and/or modify 
 *  it under the terms of the GNU Affero General Public License as published by 
 *  the Free Software Foundation, either version 3 of the License, or 
 *  (at your option) any later version. 
 * 
 *  This program is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *  GNU Affero General Public License for more details. 
 * 
 *  You should have received a copy of the GNU Affero General Public License 
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 * 
 *  e-mail: info _at_ inherit.se 
 *  mail: Inherit S AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN 
 *  phone: +46 8 641 64 14 
 */ 
 
package org.inheritsource.portal.components.mycases.search;

import org.inheritsource.service.common.domain.PagedProcessInstanceSearchResult;
import org.inheritsource.service.rest.client.InheritServiceClient;
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
