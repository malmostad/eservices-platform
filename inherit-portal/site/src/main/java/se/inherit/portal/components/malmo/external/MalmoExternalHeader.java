package se.inherit.portal.components.malmo.external;


import se.inherit.portal.channels.WebsiteInfo;
import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.configuration.hosting.Mount;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.inherit.service.common.util.UrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MalmoExternalHeader extends BaseHstComponent {

    public static final Logger log = LoggerFactory.getLogger(MalmoExternalHeader.class);

    public static final String MAST_HEAD_URL = "http://www.malmo.se/assets-2.0/remote/external-masthead/";
    
    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        final Mount mount = request.getRequestContext().getResolvedMount().getMount();
        
        // TODO replace with Malmo internal channel info
        final WebsiteInfo info = mount.getChannelInfo();

        if (info != null) {
            request.setAttribute("headerName", info.getHeaderName());
        } else {
            log.warn("No channel info available for website '{}'", mount.getMountPath());
        }
        
        String mastHead = UrlUtil.loadFromUrl(MAST_HEAD_URL);
        log.error("mastHead=" + mastHead);
        request.setAttribute("mastHead", mastHead);
		
    }

}
