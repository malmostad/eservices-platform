package org.motrice.docbox.doc

class BoxDocController {

  def generatepdf(Long id) {
    String uuid = params.id
    def dataObj = Data.findByUuid(uuid)
    if (!dataObj) {
      flash.message = message(code: 'default.not.found.message', args: [message(code: 'data.label', default: 'Data'), uuid])
      redirect(action: "list")
      return
    }

    // Generate PDF, attach as new resource
    try {
      def name = pdfService.generatePdfa(dataObj, false)
      flash.message = "Generated ${name}"
    } catch (Exception exc) {
      flash.message = exc.message
    }
    redirect(action: 'show', id: dataObj.uuid)
  }

}
