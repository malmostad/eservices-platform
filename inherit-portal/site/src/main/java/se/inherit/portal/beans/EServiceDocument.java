package se.inherit.portal.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

@Node(jcrType="inheritportal:eservicedocument")
public class EServiceDocument extends BaseDocument{
    
    public String getSummary() {
        return getProperty("inheritportal:summary");
    }
 
    public String getTitle() {
        return getProperty("inheritportal:title");
    }

    public String getPdfUrl() {
        return getProperty("inheritportal:pdfurl");
    }

    public String getWordUrl() {
        return getProperty("inheritportal:wordurl");
    }

    public String getFormPath() {
    	return getProperty("inheritportal:formpath");
    }
}
