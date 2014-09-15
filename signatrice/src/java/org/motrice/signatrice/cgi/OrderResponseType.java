
package org.motrice.signatrice.cgi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrderResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OrderResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="transactionId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="orderRef" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AutoStartToken" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderResponseType", propOrder = {
    "transactionId",
    "orderRef",
    "autoStartToken"
})
public class OrderResponseType {

    @XmlElement(required = true)
    protected String transactionId;
    @XmlElement(required = true)
    protected String orderRef;
    @XmlElement(name = "AutoStartToken", required = true)
    protected String autoStartToken;

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
     * Gets the value of the orderRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderRef() {
        return orderRef;
    }

    /**
     * Sets the value of the orderRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderRef(String value) {
        this.orderRef = value;
    }

    /**
     * Gets the value of the autoStartToken property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAutoStartToken() {
        return autoStartToken;
    }

    /**
     * Sets the value of the autoStartToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAutoStartToken(String value) {
        this.autoStartToken = value;
    }

}
