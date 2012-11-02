package se.inherit.portal.components.mycases;

import se.inherit.portal.components.BaseComponent;
import se.inherit.portal.componentsinfo.PageableListInfo;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO remove FormOverview.java and PageableListInfo deps???
@ParametersInfo(type = PageableListInfo.class)
public class FormOverview extends BaseComponent {

    public static final Logger log = LoggerFactory.getLogger(FormOverview.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {

       PageableListInfo info = getParametersInfo(request);
       HippoBean scope = getContentBean(request);

       if(scope == null) {
           response.setStatus(404);
           throw new HstComponentException("For an Overview component there must be a content bean available to search below. Cannot create an overview");
       }
       createAndExecuteSearch(request, info, scope, null);
    }

}
