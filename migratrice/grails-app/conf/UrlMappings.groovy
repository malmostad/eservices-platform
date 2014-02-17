class UrlMappings {

  static mappings = {
    "/$controller/$action?/$id?"{
      constraints {
	// apply constraints here
      }
    }

    "/"(controller: 'migPackage', action: 'list')
    "500"(controller: 'errors', action: 'internalServerError')
  }
}
