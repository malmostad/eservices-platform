package se.inherit.portal.components.mycases;

import se.inherit.portal.components.BaseComponent;
import se.inherit.portal.componentsinfo.PageableListInfo;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
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
       
       loadImage(request, "wordIcon", "/content/gallery/inheritportal/doc_word.png");
       loadImage(request, "pdfIcon", "/content/gallery/inheritportal/doc_pdf.png");

       createAndExecuteSearch(request, info, scope, null);
    }
    
    private void loadImage(final HstRequest request, String attributeName, String imagePath) {
    	Object image = null;
        try {
        	image = getObjectBeanManager(request).getObject(imagePath);
            if (image instanceof HippoGalleryImageSet) {
                request.setAttribute(attributeName, image);
            } else {
                log.warn("Mount '{}' has illegal logo path '{}' (not an image set). No logo will be shown.");
            }
        } catch (ObjectBeanManagerException e) {
            log.warn("Cannot retrieve logo at '" + imagePath + "'", e);
        }
    }

}
