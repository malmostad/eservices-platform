class UrlMappings {

  static mappings = {
    "/rest/db/orbeon-pe/fr/$app/$form/form/$resource"(controller: 'RestFormdef') {
      action = [GET: 'getop', PUT: 'putop', DELETE: 'delete']
    }
    "/rest/db/orbeon-pe/fr/orbeon/builder/data/$uuid/$resource"(controller: 'RestResource') {
      action = [GET: 'getop', PUT: 'putop', DELETE: 'delete']
    }
    "/rest/db/orbeon-pe/fr/$app/$form/data/$uuid/$resource"(controller: 'RestFormdata') {
      action = [GET: 'getop', PUT: 'putop', DELETE: 'delete']
    }
    "/rest/db/orbeon-pe/fr/orbeon/builder/data"(controller: 'RestFormdef', action: 'list')
    "/postxdb/formdef/$id?"(controller: 'RestPostxdb') {
      action = [GET: 'formdefget']
    }
    "/postxdb/formdefver/$id?"(controller: 'RestPostxdb') {
      action = [GET: 'versionget']
    }
    "/postxdb/defitem"(controller: 'RestPostxdb') {
      action = [GET: 'defitemget']
    }
    "/postxdb/institem"(controller: 'RestPostxdb') {
      action = [GET: 'institemget']
    }
    "/postxdb/item/$id"(controller: 'RestPostxdb') {
      action = [GET: 'itemgetbyid']
    }
    "/$controller/$action?/$id?"{
      constraints {
	// apply constraints here
      }
    }

    "/"(controller: 'PxdFormdef', action: 'index')
    "500"(view:'/error')
  }
}
