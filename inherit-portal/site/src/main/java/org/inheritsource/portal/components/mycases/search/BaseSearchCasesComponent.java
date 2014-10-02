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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inheritsource.portal.components.mycases.MyCasesBaseComponent;
import org.inheritsource.service.common.domain.PagedProcessInstanceSearchResult;
import org.inheritsource.service.common.domain.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseSearchCasesComponent extends MyCasesBaseComponent {

	public static final Logger log = LoggerFactory
			.getLogger(BaseSearchCasesComponent.class);

	protected boolean checkIfIsEnabled() { return  false ;  } ; 
	public void doBeforeRender(final HstRequest request,
			final HstResponse response) throws HstComponentException {
		HippoBean doc = getContentBean(request);
		UserInfo user = getUserName(request);
		String searchStr = null;
		String pageStr = null;
		String pageSizeStr = null;
		String sortBy = null;
		String sortOrder = null;
		String filter = null;
		String editablefilter = null;
		String involvedUserId = null;
		String startDateStr = "";
		String tolDaysStr = null;
		String searchIsEnabled = null;

		try {
			searchStr = getPublicRequestParameter(request, "searchStr");
			pageStr = getPublicRequestParameter(request, "page");
			pageSizeStr = getPublicRequestParameter(request, "pageSize");
			sortBy = getPublicRequestParameter(request, "sortBy");
			sortOrder = getPublicRequestParameter(request, "sortOrder");
			filter = getPublicRequestParameter(request, "filter");
			editablefilter = getPublicRequestParameter(request,
					"editablefilter");
			involvedUserId = getPublicRequestParameter(request,
					"involvedUserId");
			startDateStr = getPublicRequestParameter(request, "startDate");
			tolDaysStr = getPublicRequestParameter(request, "tolDays");
			searchIsEnabled = getPublicRequestParameter(request,
					"searchIsEnabled");
		} catch (Exception e) {
			log.warn("getPublicRequestParameter problem");
		}

		int page = 1;
		if (pageStr != null && pageStr.length() > 0) {
			try {
				page = Integer.parseInt(pageStr);
			} catch (Exception e) {
				log.warn("pageStr=[" + pageStr
						+ "] is not an integer => ignored");
			}
		}

		int pageSize = 10;
		if (pageSizeStr != null && pageSizeStr.length() > 0) {
			try {
				pageSize = Integer.parseInt(pageSizeStr);
			} catch (Exception e) {
				log.warn("pageSizeStr=[" + pageSizeStr
						+ "] is not an integer => ignored");
			}
		}
		int tolDays = 10;
		if (tolDaysStr != null && tolDaysStr.length() > 0) {
			try {
				tolDays = Integer.parseInt(tolDaysStr);
				tolDays = Math.max(tolDays, 1);
			} catch (Exception e) {
				log.warn("tolDaysStr=[" + tolDaysStr
						+ "] is not an integer => ignored");
			}
		}

		Date startDate = null;
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (filter == null || filter.length() == 0) {
			// default filter
			filter = "STARTED";
		}

		if (sortBy == null || sortBy.length() == 0) {
			// default
			sortBy = "started";
		}

		if (sortOrder == null || sortOrder.length() == 0) {
			// default
			if (sortBy.equals("started")) {
				sortOrder = "asc";
			} else {
				sortOrder = "desc";
			}
		}

		if (searchStr != null) {
			searchStr = searchStr.trim();
		}

		if (involvedUserId != null) {
			involvedUserId = involvedUserId.trim();
		}

		int fromIndex = (page - 1) * pageSize;

		if (doc == null) {
			log.warn(
					"Did not find a content bean for relative content path '{}' for pathInfo '{}'",
					request.getRequestContext().getResolvedSiteMapItem()
							.getRelativeContentPath(), request
							.getRequestContext().getResolvedSiteMapItem()
							.getPathInfo());
			// response.setStatus(404);
			// return;
		}

		request.setAttribute("document", doc);

		PagedProcessInstanceSearchResult searchResult;
		if ( checkIfIsEnabled() && (searchIsEnabled == null)) {
			searchResult = null;
		} else {
			searchResult = executeSearch(searchStr, involvedUserId, fromIndex,
					pageSize, sortBy, sortOrder, filter, request.getLocale(),
					user.getUuid(), startDate, tolDays);
		}

		// append hippo jcr labels on processes and activities in the serach
		// result
		appendChannelLabels(request, searchResult);

		// set request attributes

		request.setAttribute("searchResult", searchResult);
		int pageCount;
		if (searchResult == null) {
			log.info("searchResult is null");
			pageCount = 1;
		} else {
			log.info("(searchResult.getNumberOfHits()-1) = "
					+ (searchResult.getNumberOfHits() - 1));
			log.info("searchResult.getPageSize() = "
					+ searchResult.getPageSize());

			pageCount = ((searchResult.getNumberOfHits() - 1) / searchResult
					.getPageSize()) + 1;
		}
		request.setAttribute("pageCount", pageCount);

		request.setAttribute("searchStr", searchStr);
		request.setAttribute("filter", filter);
		request.setAttribute("editablefilter", editablefilter);
		request.setAttribute("page", page);
		request.setAttribute("pageLinkLb", Math.max(page - 3, 1));
		request.setAttribute("pageLinkUb", Math.min(page + 3, pageCount));
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("sortBy", sortBy);
		request.setAttribute("sortOrder", sortOrder);
		request.setAttribute("searchIsEnabled", "true");
		request.setAttribute("involvedUserId", involvedUserId);
		request.setAttribute("startDate", startDateStr);
		request.setAttribute("tolDays", tolDays);

		request.setAttribute("submitUri", request.getRequestURI());

		log.info("pageCount = {}", pageCount);
		log.info("searchStr = {}", searchStr);
		log.info("filter = {}", filter);
		log.info("editablefilter = {}", editablefilter);
		log.info("page = {}", page);
		log.info("pageLinkLb = {}", Math.max(page - 3, 1));
		log.info("pageLinkUb = {}", Math.min(page + 3, pageCount));
		log.info("pageSize = {}", pageSize);
		log.info("sortBy = {}", sortBy);
		log.info("sortOrder = {}", sortOrder);
		log.info("searchIsEnabled = {}", searchIsEnabled);
		log.info("involvedUserId = {}", involvedUserId);
		log.info("startDate = {}", startDate);
		log.info("tolDays = {}", tolDays);

	}

	public abstract PagedProcessInstanceSearchResult executeSearch(
			String searchStr, String involvedUserId, int fromIndex,
			int pageSize, String sortBy, String sortOrder, String filter,
			Locale locale, String userId, Date startDate, int tolDays);

}
