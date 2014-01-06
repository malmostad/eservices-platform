class UrlMappings {

  static mappings = {
    "/$controller/$action?/$id?"{
      constraints {
	// apply constraints here
      }
    }

    "/"(controller: 'oriPackage', action: 'list')
    "500"(view:'/error')
  }
}
