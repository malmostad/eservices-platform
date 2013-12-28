import grails.converters.*

class BootStrap {

  def init = { servletContext ->
    // This call makes Grails look for a toXml method in the domain
    // when rendering an instance as XML
    XML.registerObjectMarshaller(new org.codehaus.groovy.grails.web.converters
				 .marshaller.xml.InstanceMethodBasedMarshaller())
  }
  def destroy = {
  }
}
