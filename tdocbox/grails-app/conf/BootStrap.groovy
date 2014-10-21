import org.motrice.tdocbox.TdbHttpVerb
import org.motrice.tdocbox.TdbMethod
import org.motrice.tdocbox.TdbMode
import org.motrice.tdocbox.TdbProperty

class BootStrap {
  // Injection magic
  def grailsApplication
  def setupService

  def init = { servletContext ->
    // Create the HTTP verbs
    TdbHttpVerb.createVerbs()
    // Create all invocation modes
    TdbMode.createModes()
    // Some properties are assumed to be defined
    TdbProperty.createProperties()
    // Define the API methods
    TdbMethod.createMethods()

    // Database initialization
    setupService.initialize()
  }

  def destroy = {
  }

}
