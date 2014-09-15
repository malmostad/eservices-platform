class UrlMappings {
  static mappings = {
    "/$controller/$action?/$id?"{
      constraints {
	// apply constraints here
      }
    }

    "/"(controller: 'sigTestcase')
    "500"(view:'/error')
  }
}
