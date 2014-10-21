class UrlMappings {

  static mappings = {
    "/$controller/$action?/$id?"{
      constraints {
	// apply constraints here
      }
    }

    "/"(controller:'TdbSuite', action:'/index')
    "500"(view:'/error')
  }
}
