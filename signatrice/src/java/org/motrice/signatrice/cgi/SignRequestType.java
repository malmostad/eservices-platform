
package org.motrice.signatrice.cgi;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SignRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SignRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="policy" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="provider" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="displayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transactionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="personalNumber" type="{http://funktionstjanster.se/grp/service/v1.0.0/}PersonalNumberType" minOccurs="0"/>
 *         &lt;element name="userVisibleData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userNonVisibleData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="endUserInfo" type="{http://funktionstjanster.se/grp/service/v1.0.0/}EndUserInfoType" maxOccurs="20" minOccurs="0"/>
 *         &lt;element name="requirementAlternatives" type="{http://funktionstjanster.se/grp/service/v1.0.0/}RequirementAlternativesType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SignRequestType", propOrder = {
    "policy",
    "provider",
    "displayName",
    "transactionId",
    "personalNumber",
    "userVisibleData",
    "userNonVisibleData",
    "endUserInfo",
    "requirementAlternatives"
})
public class SignRequestType {

    @XmlElement(required = true)
    protected String policy;
    @XmlElement(required = true)
    protected String provider;
    protected String displayName;
    protected String transactionId;
    protected String personalNumber;
    protected String userVisibleData;
    protected String userNonVisibleData;
    protected List<EndUserInfoType> endUserInfo;
    @XmlElement(required = true)
    protected RequirementAlternativesType requirementAlternatives;

    /**
     * Gets the value of the policy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPolicy() {
        return policy;
    }

    /**
     * Sets the value of the policy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPolicy(String value) {
        this.policy = value;
    }

    /**
     * Gets the value of the provider property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvider() {
        return provider;
    }

    /**
     * Sets the value of the provider property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvider(String value) {
        this.provider = value;
    }

    /**
     * Gets the value of the displayName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayName(String value) {
        this.displayName = value;
    }

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
     * Gets the value of the personalNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonalNumber() {
        return personalNumber;
    }

    /**
     * Sets the value of the personalNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonalNumber(String value) {
        this.personalNumber = value;
    }

    /**
     * Gets the value of the userVisibleData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserVisibleData() {
        return userVisibleData;
    }

    /**
     * Sets the value of the userVisibleData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserVisibleData(String value) {
        this.userVisibleData = value;
    }

    /**
     * Gets the value of the userNonVisibleData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserNonVisibleData() {
        return userNonVisibleData;
    }

    /**
     * Sets the value of the userNonVisibleData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserNonVisibleData(String value) {
        this.userNonVisibleData = value;
    }

    /**
     * Gets the value of the endUserInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the endUserInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEndUserInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EndUserInfoType }
     * 
     * 
     */
    public List<EndUserInfoType> getEndUserInfo() {
        if (endUserInfo == null) {
            endUserInfo = new ArrayList<EndUserInfoType>();
        }
        return this.endUserInfo;
    }

    /**
     * Gets the value of the requirementAlternatives property.
     * 
     * @return
     *     possible object is
     *     {@link RequirementAlternativesType }
     *     
     */
    public RequirementAlternativesType getRequirementAlternatives() {
        return requirementAlternatives;
    }

    /**
     * Sets the value of the requirementAlternatives property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequirementAlternativesType }
     *     
     */
    public void setRequirementAlternatives(RequirementAlternativesType value) {
        this.requirementAlternatives = value;
    }

}
