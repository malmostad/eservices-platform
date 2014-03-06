class UrlMappings {

  static mappings = {
    "/$controller/$action?/$id?"{
      constraints {
	// apply constraints here
      }
    }
    "/rest/procdef/state/$id"(controller: 'RestProcdef') {
      action = [GET: 'procdefStateGet']
    }
    "/rest/activitylabel/$procdefkey/$locale/$activityname?"(controller: 'RestActivity') {
      action = [GET: 'activityLabelGet']
    }
    "/rest/guideurl/$procdefkey/$locale/$activityname?"(controller: 'RestGuideUrl') {
      action = [GET: 'guideUrlGet']
    }

    "/"(controller: 'Procdef', action:'/index')
    "500"(controller: 'errors', action: 'internalServerError')
  }
}
