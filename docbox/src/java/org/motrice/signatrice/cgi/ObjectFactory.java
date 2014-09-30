
package org.motrice.signatrice.cgi;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.motrice.signatrice.cgi package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AuthenticateResponse_QNAME = new QName("http://funktionstjanster.se/grp/service/v1.0.0/", "AuthenticateResponse");
    private final static QName _CollectResponse_QNAME = new QName("http://funktionstjanster.se/grp/service/v1.0.0/", "CollectResponse");
    private final static QName _GrpFault_QNAME = new QName("http://funktionstjanster.se/grp/service/v1.0.0/", "GrpFault");
    private final static QName _CollectRequest_QNAME = new QName("http://funktionstjanster.se/grp/service/v1.0.0/", "CollectRequest");
    private final static QName _AuthenticateRequest_QNAME = new QName("http://funktionstjanster.se/grp/service/v1.0.0/", "AuthenticateRequest");
    private final static QName _SignatureFileResponse_QNAME = new QName("http://funktionstjanster.se/grp/service/v1.0.0/", "SignatureFileResponse");
    private final static QName _SignResponse_QNAME = new QName("http://funktionstjanster.se/grp/service/v1.0.0/", "SignResponse");
    private final static QName _SignRequest_QNAME = new QName("http://funktionstjanster.se/grp/service/v1.0.0/", "SignRequest");
    private final static QName _SignatureFileRequest_QNAME = new QName("http://funktionstjanster.se/grp/service/v1.0.0/", "SignatureFileRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.motrice.signatrice.cgi
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link OrderResponseType }
     * 
     */
    public OrderResponseType createOrderResponseType() {
        return new OrderResponseType();
    }

    /**
     * Create an instance of {@link AuthenticateRequestType }
     * 
     */
    public AuthenticateRequestType createAuthenticateRequestType() {
        return new AuthenticateRequestType();
    }

    /**
     * Create an instance of {@link CollectRequestType }
     * 
     */
    public CollectRequestType createCollectRequestType() {
        return new CollectRequestType();
    }

    /**
     * Create an instance of {@link SignatureFileRequestType }
     * 
     */
    public SignatureFileRequestType createSignatureFileRequestType() {
        return new SignatureFileRequestType();
    }

    /**
     * Create an instance of {@link SignRequestType }
     * 
     */
    public SignRequestType createSignRequestType() {
        return new SignRequestType();
    }

    /**
     * Create an instance of {@link CollectResponseType }
     * 
     */
    public CollectResponseType createCollectResponseType() {
        return new CollectResponseType();
    }

    /**
     * Create an instance of {@link GrpFaultType }
     * 
     */
    public GrpFaultType createGrpFaultType() {
        return new GrpFaultType();
    }

    /**
     * Create an instance of {@link ConditionType }
     * 
     */
    public ConditionType createConditionType() {
        return new ConditionType();
    }

    /**
     * Create an instance of {@link Property }
     * 
     */
    public Property createProperty() {
        return new Property();
    }

    /**
     * Create an instance of {@link RequirementAlternativesType }
     * 
     */
    public RequirementAlternativesType createRequirementAlternativesType() {
        return new RequirementAlternativesType();
    }

    /**
     * Create an instance of {@link RequirementType }
     * 
     */
    public RequirementType createRequirementType() {
        return new RequirementType();
    }

    /**
     * Create an instance of {@link EndUserInfoType }
     * 
     */
    public EndUserInfoType createEndUserInfoType() {
        return new EndUserInfoType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://funktionstjanster.se/grp/service/v1.0.0/", name = "AuthenticateResponse")
    public JAXBElement<OrderResponseType> createAuthenticateResponse(OrderResponseType value) {
        return new JAXBElement<OrderResponseType>(_AuthenticateResponse_QNAME, OrderResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CollectResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://funktionstjanster.se/grp/service/v1.0.0/", name = "CollectResponse")
    public JAXBElement<CollectResponseType> createCollectResponse(CollectResponseType value) {
        return new JAXBElement<CollectResponseType>(_CollectResponse_QNAME, CollectResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GrpFaultType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://funktionstjanster.se/grp/service/v1.0.0/", name = "GrpFault")
    public JAXBElement<GrpFaultType> createGrpFault(GrpFaultType value) {
        return new JAXBElement<GrpFaultType>(_GrpFault_QNAME, GrpFaultType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CollectRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://funktionstjanster.se/grp/service/v1.0.0/", name = "CollectRequest")
    public JAXBElement<CollectRequestType> createCollectRequest(CollectRequestType value) {
        return new JAXBElement<CollectRequestType>(_CollectRequest_QNAME, CollectRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthenticateRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://funktionstjanster.se/grp/service/v1.0.0/", name = "AuthenticateRequest")
    public JAXBElement<AuthenticateRequestType> createAuthenticateRequest(AuthenticateRequestType value) {
        return new JAXBElement<AuthenticateRequestType>(_AuthenticateRequest_QNAME, AuthenticateRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://funktionstjanster.se/grp/service/v1.0.0/", name = "SignatureFileResponse")
    public JAXBElement<OrderResponseType> createSignatureFileResponse(OrderResponseType value) {
        return new JAXBElement<OrderResponseType>(_SignatureFileResponse_QNAME, OrderResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://funktionstjanster.se/grp/service/v1.0.0/", name = "SignResponse")
    public JAXBElement<OrderResponseType> createSignResponse(OrderResponseType value) {
        return new JAXBElement<OrderResponseType>(_SignResponse_QNAME, OrderResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://funktionstjanster.se/grp/service/v1.0.0/", name = "SignRequest")
    public JAXBElement<SignRequestType> createSignRequest(SignRequestType value) {
        return new JAXBElement<SignRequestType>(_SignRequest_QNAME, SignRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignatureFileRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://funktionstjanster.se/grp/service/v1.0.0/", name = "SignatureFileRequest")
    public JAXBElement<SignatureFileRequestType> createSignatureFileRequest(SignatureFileRequestType value) {
        return new JAXBElement<SignatureFileRequestType>(_SignatureFileRequest_QNAME, SignatureFileRequestType.class, null, value);
    }

}
