package se.inherit.portal.components.mycases.search;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inherit.service.common.domain.PagedProcessInstanceSearchResult;
import org.inherit.service.common.domain.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.inherit.portal.components.mycases.MyCasesBaseComponent;

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
        
        
        PagedProcessInstanceSearchResult searchResult = executeSearch(searchStr, fromIndex, pageSize, sortBy, sortOrder, filter, user.getUuid());

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
    
    public abstract PagedProcessInstanceSearchResult executeSearch(String searchStr, int fromIndex, int pageSize, String sortBy, String sortOrder, String filter, String userId);

}
