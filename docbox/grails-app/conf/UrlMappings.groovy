class UrlMappings {

  static mappings = {
    "/env/validate"(controller: 'Env') {
      action = [GET: 'validate']
    }
    "/doc/formdata/$uuid"(controller: 'RestDoc') {
      action = [GET: 'formDataGet', PUT: 'formDataPut']
    }
    "/doc/ref/$docboxref"(controller: 'RestDoc') {
      action = [GET: 'docboxRefGet']
    }
    "/doc/no/$docno"(controller: 'RestDoc') {
      action = [GET: 'docNoGet']
    }
    "/doc/sig/$docboxref"(controller: 'RestSig') {
      action = [POST: 'docboxSigPut']
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
    "500"(view:'/error')
  }
}
