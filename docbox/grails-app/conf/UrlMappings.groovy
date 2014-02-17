class UrlMappings {

  static mappings = {
    "/env/validate"(controller: 'Env') {
      action = [GET: 'validate']
    }
    "/doc/formdata/$uuid"(controller: 'RestDoc') {
      action = [GET: 'formDataGet', PUT: 'formDataPut']
    }
    "/doc/input/$docboxref"(controller: 'RestDoc') {
      action = [GET: 'docboxFormData']
    }
    "/doc/ref/$docboxref"(controller: 'RestDoc') {
      action = [GET: 'docboxRefGet']
    }
    "/doc/sig/$docboxref"(controller: 'RestSig') {
      action = [GET: 'docboxSigGet', POST: 'docboxSigPut']
    }
    "/doc/sig/validate/$docboxref"(controller: 'RestSig') {
      action = [GET: 'sigValidate']
    }

    "/$controller/$action?/$id?"{
      constraints {
	// apply constraints here
      }
    }

    "/"(controller: 'BoxDoc', action: '/index')
    "500"(controller: 'errors', action: 'internalServerError')
  }
}
