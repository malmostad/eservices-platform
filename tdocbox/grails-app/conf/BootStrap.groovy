import org.motrice.tdocbox.TdbHttpVerb
import org.motrice.tdocbox.TdbMethod
import org.motrice.tdocbox.TdbProperty

class BootStrap {
  // Injection magic
  def grailsApplication

  def init = { servletContext ->
    // Create the HTTP verbs
    TdbHttpVerb.createVerbs()
    // Some properties are assumed to be defined
    TdbProperty.createProperties()
    // Define the API methods
    TdbMethod.createMethods()
  }

  def destroy = {
  }

}
