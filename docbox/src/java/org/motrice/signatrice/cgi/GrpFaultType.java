
package org.motrice.signatrice.cgi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GrpFaultType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GrpFaultType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="faultStatus" type="{http://funktionstjanster.se/grp/service/v1.0.0/}FaultStatusType"/>
 *         &lt;element name="detailedDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GrpFaultType", propOrder = {
    "faultStatus",
    "detailedDescription"
})
public class GrpFaultType {

    @XmlElement(required = true)
    protected FaultStatusType faultStatus;
    @XmlElement(required = true)
    protected String detailedDescription;

    /**
     * Gets the value of the faultStatus property.
     * 
     * @return
     *     possible object is
     *     {@link FaultStatusType }
     *     
     */
    public FaultStatusType getFaultStatus() {
        return faultStatus;
    }

    /**
     * Sets the value of the faultStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link FaultStatusType }
     *     
     */
    public void setFaultStatus(FaultStatusType value) {
        this.faultStatus = value;
    }

    /**
     * Gets the value of the detailedDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDetailedDescription() {
        return detailedDescription;
    }

    /**
     * Sets the value of the detailedDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDetailedDescription(String value) {
        this.detailedDescription = value;
    }

}
