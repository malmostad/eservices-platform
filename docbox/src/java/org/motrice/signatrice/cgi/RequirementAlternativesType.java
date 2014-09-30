
package org.motrice.signatrice.cgi;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RequirementAlternativesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RequirementAlternativesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="requirement" type="{http://funktionstjanster.se/grp/service/v1.0.0/}RequirementType" maxOccurs="7" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequirementAlternativesType", propOrder = {
    "requirement"
})
public class RequirementAlternativesType {

    protected List<RequirementType> requirement;

    /**
     * Gets the value of the requirement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the requirement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRequirement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RequirementType }
     * 
     * 
     */
    public List<RequirementType> getRequirement() {
        if (requirement == null) {
            requirement = new ArrayList<RequirementType>();
        }
        return this.requirement;
    }

}
