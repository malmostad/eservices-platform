import org.motrice.signatrice.SigDisplayname
import org.motrice.signatrice.SigPolicy
import org.motrice.signatrice.SigProgress
import org.motrice.signatrice.SigService

class BootStrap {
    def init = {servletContext ->
      SigDisplayname.createDisplaynames()
      SigPolicy.createPolicies()
      SigService.createPredefinedServices()
      SigProgress.createAllProgress()
    }
    def destroy = {
    }
}
