class UrlMappings {

  static mappings = {
    "/$controller/$action?/$id?"{
      constraints {
	// apply constraints here
      }
    }

    "/"(controller: 'ProcDef', action:'/index')
    "500"(controller: 'errors', action: 'internalServerError')
  }
}
