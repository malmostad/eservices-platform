
package org.motrice.signatrice.cgi;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProgressStatusType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ProgressStatusType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="COMPLETE"/>
 *     &lt;enumeration value="USER_SIGN"/>
 *     &lt;enumeration value="OUTSTANDING_TRANSACTION"/>
 *     &lt;enumeration value="NO_CLIENT"/>
 *     &lt;enumeration value="USER_REQ"/>
 *     &lt;enumeration value="STARTED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ProgressStatusType")
@XmlEnum
public enum ProgressStatusType {

    COMPLETE,
    USER_SIGN,
    OUTSTANDING_TRANSACTION,
    NO_CLIENT,
    USER_REQ,
    STARTED;

    public String value() {
        return name();
    }

    public static ProgressStatusType fromValue(String v) {
        return valueOf(v);
    }

}
