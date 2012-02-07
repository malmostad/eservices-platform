package se.inherit.portal.beans;

import java.util.Calendar;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSetBean;

@Node(jcrType="inheritportal:newsdocument")
public class NewsDocument extends BaseDocument{

    public Calendar getDate() {
        return getProperty("inheritportal:date");
    }

    public HippoHtml getHtml(){
        return getHippoHtml("inheritportal:body");    
    }

    /**
     * Get the imageset of the newspage
     *
     * @return the imageset of the newspage
     */
    public HippoGalleryImageSetBean getImage() {
        return getLinkedBean("inheritportal:image", HippoGalleryImageSetBean.class);
    }

    public String getSummary() {
        return getProperty("inheritportal:summary");
    }

    public String getTitle() {
        return getProperty("inheritportal:title");
    }

}
