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

	public static final Logger log = LoggerFactory.getLogger(BaseSearchCasesComponent.class);
	
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
    	HippoBean doc = getContentBean(request);
        UserInfo user = getUserName(request);

        String searchStr = getPublicRequestParameter(request, "searchStr");
        String pageStr = getPublicRequestParameter(request, "page");
        String pageSizeStr = getPublicRequestParameter(request, "pageSize");
        String sortBy = getPublicRequestParameter(request, "sortBy");
        String sortOrder = getPublicRequestParameter(request, "sortOrder");
        String filter = getPublicRequestParameter(request, "filter");
        String editablefilter = getPublicRequestParameter(request, "editablefilter");
        
        
        int page = 1;
        if (pageStr!=null && pageStr.length()>0 ) {
        	try {
        		page = Integer.parseInt(pageStr);
        	}
        	catch(Exception e) {
        		log.warn("pageStr=[" + pageStr + "] is not an integer => ignored");
        	}
        }
        	
        int pageSize = 10;
        if (pageSizeStr!=null && pageSizeStr.length()>0 ) {
        	try {
        		pageSize = Integer.parseInt(pageSizeStr);
        	}
        	catch(Exception e) {
        		log.warn("pageSizeStr=[" + pageSizeStr + "] is not an integer => ignored");
        	}
        }
        
        if (filter==null || filter.trim().length()==0) {
        	// default filter
        	filter = "STARTED";
        }
        	
        if (sortBy==null || sortBy.trim().length()==0) {
        	// default filter
        	sortBy = "started";
        }
        	
        if (sortOrder==null || sortOrder.trim().length()==0) {
        	// default filter
        	sortOrder = "desc";
        }
        	
        int fromIndex = (page-1)*pageSize;
        
        
        if (doc == null) {
            log.warn("Did not find a content bean for relative content path '{}' for pathInfo '{}'", 
                         request.getRequestContext().getResolvedSiteMapItem().getRelativeContentPath(),
                         request.getRequestContext().getResolvedSiteMapItem().getPathInfo());
  //          response.setStatus(404);
  //          return;
        }
        
        request.setAttribute("document",doc);
        
        
        PagedProcessInstanceSearchResult searchResult = executeSearch(searchStr, fromIndex, pageSize, sortBy, sortOrder, filter, request.getLocale(), user.getUuid());

        // append hippo jcr labels on processes and activities in the serach result
        appendChannelLabels(request, searchResult);
        
        // set request attributes 
        
        request.setAttribute("searchResult", searchResult);
        int pageCount = ( (searchResult.getNumberOfHits()-1) / searchResult.getPageSize()) + 1;
        request.setAttribute("pageCount", pageCount);
        
        request.setAttribute("searchStr", searchStr);
        request.setAttribute("filter", filter);
        request.setAttribute("editablefilter", editablefilter);
        request.setAttribute("page", page);
        request.setAttribute("pageLinkLb", Math.max(page-3, 1));
        request.setAttribute("pageLinkUb", Math.min(page+3, pageCount));
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("sortOrder", sortOrder);
        
        request.setAttribute("submitUri", request.getRequestURI());
        
        
    }
    
    public abstract PagedProcessInstanceSearchResult executeSearch(String searchStr, int fromIndex, int pageSize, String sortBy, String sortOrder, String filter, Locale locale, String userId);

}
