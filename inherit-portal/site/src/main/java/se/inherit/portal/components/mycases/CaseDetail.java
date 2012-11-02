package se.inherit.portal.components.mycases;


import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inherit.service.common.domain.ProcessInstanceDetails;
import org.inherit.service.rest.client.InheritServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CaseDetail extends BaseHstComponent {

	public static final Logger log = LoggerFactory.getLogger(CaseDetail.class);
	
	@Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        // TODO SSO SAML JAAS
        //Principal principal = request.getUserPrincipal();
        //String userName = principal.getName();
        
        String taskUuid = getPublicRequestParameter(request, "taskUuid");
          
        if (taskUuid!=null) {
        	 InheritServiceClient isc = new InheritServiceClient(); 
        	 ProcessInstanceDetails piDetails = isc.getProcessInstanceDetailByActivityInstanceUuid(taskUuid);
        	 request.setAttribute("processInstanceDetails", piDetails);
        }
    }
}
