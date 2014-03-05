package org.motrice.coordinatrice

class RestProcdefController {
  private final static Integer CONFLICT_STATUS = 409
  private final static CONT_DISP = 'Content-Disposition'

  /**
   * Get the state of a process definition.
   * $PREFIX/procdef/state/$id
   * where $id must be a process definition id.
   */
  def procdefStateGet(String id) {
    if (log.debugEnabled) log.debug "PROCDEF STATE GET: ${params}"
    def procdef = CrdProcdef.findByActid(id)
    if (procdef) {
      render(status: 200, contentType: 'text/json') {
	procdefId = id
	procdefVer = procdef.actver
	stateCode = procdef.state.id
	stateName = procdef.state.defaultMessage
	editable = procdef.state.editable
	startableCode = procdef.state.startable
	startableName = procdef.state.startableToString()
      }
    } else {
      if (log.debugEnabled) log.debug "PROCDEF STATE GET >> 404: ${id} not found"
      render(status: 404)
    }
  }

}
