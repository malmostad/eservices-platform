package se.inherit.portal.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

@Node(jcrType="inheritportal:textdocument")
public class TextDocument extends BaseDocument{
    
    public HippoHtml getHtml(){
        return getHippoHtml("inheritportal:body");    
    }

    public String getSummary() {
        return getProperty("inheritportal:summary");
    }
 
    public String getTitle() {
        return getProperty("inheritportal:title");
    }

}
