/* == Motrice Copyright Notice == 
 * 
 * Motrice Service Platform 
 * 
 * Copyright (C) 2011-2014 Motrice AB 
 * 
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Affero General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU Affero General Public License for more details. 
 * 
 * You should have received a copy of the GNU Affero General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>. 
 * 
 * e-mail: info _at_ motrice.se 
 * mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN 
 * phone: +46 8 641 64 14 
 
 */ 
 
 
package org.inheritsource.portal.components.mycases.search;

import java.util.Date;
import java.util.Locale;

import org.inheritsource.service.common.domain.PagedProcessInstanceSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchCasesByTagValue extends BaseSearchCasesComponent {

	public static final Logger log = LoggerFactory.getLogger(SearchCasesByTagValue.class);

	@Override
	public PagedProcessInstanceSearchResult executeSearch(
			String searchStr, String startedByUserId ,int fromIndex, int pageSize, String sortBy,
			String sortOrder, String filter, Locale locale, String userId, Date startDate, int tolDays) {
		
		// ignore startedByUserId, startDate, tolDays (used in advanced search)
        PagedProcessInstanceSearchResult searchResult = engine.searchProcessInstancesListByTag(searchStr, fromIndex, pageSize, sortBy, sortOrder, filter, locale, userId);

		return searchResult;
	}

}
