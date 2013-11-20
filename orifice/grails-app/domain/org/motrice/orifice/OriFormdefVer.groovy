package org.motrice.orifice

/**
 * A specific version of a form definition.
 * The version is available as an integer code (verno) and also as part
 * of the path (a String).
 */
class OriFormdefVer {
  // Ref: the database id the form definition had in the originating system.
  // A new database id is generated for the snapshot, but the ref is vital
  // for resolving relationships within a package.
  Long ref

  // Identify the owning Formdef
  Long formref

  // Original creation timestamp. Updated omitted, always same as created.
  Date created

  // App name (an app many have any number of forms)
  String app

  // Form name
  String form

  // Path identifying this form definition version
  String path

  // Form version number
  Integer verno

  // Draft number
  Integer draft

  // Is this version published? An explicit flag in addition to the version number.
  Boolean published

  // Form title
  String title

  // Form description
  String description

  // Form language
  String language

  static belongsTo = [formdef: OriFormdef, pkg: OriPackage]
  static constraints = {
    app size: 1..120
    form size: 1..120
    path size: 3..400
    draft nullable: true
    published nullable: true
    title nullable: true, maxSize: 120
    description nullable: true, maxSize: 800
    language nullable: true, maxSize: 80
  }

}
