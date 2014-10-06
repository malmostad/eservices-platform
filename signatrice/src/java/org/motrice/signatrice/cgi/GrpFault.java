
package org.motrice.signatrice.cgi;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "GrpFault", targetNamespace = "http://funktionstjanster.se/grp/service/v1.0.0/")
public class GrpFault
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private GrpFaultType faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public GrpFault(String message, GrpFaultType faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public GrpFault(String message, GrpFaultType faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: org.motrice.signatrice.cgi.GrpFaultType
     */
    public GrpFaultType getFaultInfo() {
        return faultInfo;
    }

}