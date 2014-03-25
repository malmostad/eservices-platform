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
    "/rest/activitylabel/$procdefkey/$locale/$activityname?"(controller: 'RestI18n') {
      action = [GET: 'activityLabelGet']
    }
    "/rest/guideurl/$procdefkey/$locale/$activityname?"(controller: 'RestI18n') {
      action = [GET: 'guideUrlGet']
    }
    "/rest/startformlabel/$appname/$formname/$locale"(controller: 'RestI18n') {
      action = [GET: 'startFormLabelGet']
    }

    "/"(controller: 'Procdef', action:'/index')
    "500"(controller: 'errors', action: 'internalServerError')
  }
}
