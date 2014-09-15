
package org.motrice.signatrice.cgi;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CollectResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CollectResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="transactionId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="progressStatus" type="{http://funktionstjanster.se/grp/service/v1.0.0/}ProgressStatusType"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="signature" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="attributes" type="{http://funktionstjanster.se/grp/service/v1.0.0/}Property" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CollectResponseType", propOrder = {
    "transactionId",
    "progressStatus",
    "signature",
    "attributes"
})
public class CollectResponseType {

    @XmlElement(required = true)
    protected String transactionId;
    @XmlElement(required = true)
    protected ProgressStatusType progressStatus;
    protected String signature;
    protected List<Property> attributes;

    /**
     * Gets the value of the transactionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Sets the value of the transactionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionId(String value) {
        this.transactionId = value;
    }

    /**
     * Gets the value of the progressStatus property.
     * 
     * @return
     *     possible object is
     *     {@link ProgressStatusType }
     *     
     */
    public ProgressStatusType getProgressStatus() {
        return progressStatus;
    }

    /**
     * Sets the value of the progressStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProgressStatusType }
     *     
     */
    public void setProgressStatus(ProgressStatusType value) {
        this.progressStatus = value;
    }

    /**
     * Gets the value of the signature property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignature(String value) {
        this.signature = value;
    }

    /**
     * Gets the value of the attributes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attributes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttributes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Property }
     * 
     * 
     */
    public List<Property> getAttributes() {
        if (attributes == null) {
            attributes = new ArrayList<Property>();
        }
        return this.attributes;
    }

}
