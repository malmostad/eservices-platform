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

    "/$controller/$action?/$id?"{
      constraints {
	// apply constraints here
      }
    }

    "/"(view:"/index")
    "500"(view:'/error')
  }
}
