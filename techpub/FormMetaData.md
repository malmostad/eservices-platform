# Form Metadata (Proposal) #

Form definitions and form instances in Motrice quickly increase in number and have to be managed. A minimal set of metadata in form definitions and form instances is needed. A tiny set of metadata is maintained in form definitions by Orbeon Forms. Orbeon adds no metadata whatsoever to form instances.

We envision a system where form instances are converted to some variant of PDF/A.

Proposal: Use Dublin Core Metadata Element Set, or a subset thereof. It consists of 15 well-defined properties. PDF/A mandates inclusion of metadata as an uncompressed stream in XML format according to the XMP standard. This means that metadata can be extracted with or without knowledge of the PDF format. Dublin Core is one of the most firmly established metadata frameworks, and goes well with XMP.

Dublin Core assumes some design decisions are already made. One of them is a system for uniquely identifying items. Identity is specially treated in this proposal.

 NOTE: Identity in this context is *visible* identity, as it appears to users.  In addition to a visible identity an object may or may not have an identity less visible to users,  a database primary key, for instance.

## Terminology ##


* **form definition**  
 A form as a template to be filled out


* **form instance**  
 A filled-out form

## Form Definition Identity ##

At the time of writing a form definition is identified by two names, an app name and a form name. A slash (/) is used to separate the two names when written as a single name. App and form names are chosen by the form designer and checked for uniqueness by Orbeon. If version management is introduced a version number will be appended to the form name.

 NOTE: The terms *app name* and *form name* is Orbeon vocabulary.  They are not relevant to users and should not appear in the user experience.

Example: `miljoforvaltningen/pcb_inventering` or `miljoforvaltningen/pcb_inventering--v02`

This kind of identity is not suitable as a database primary key. Among other things it would be impossible to change the name of a form. It is probable that a different identifier (perhaps an integer) is used as the database key. In such case a form definition has two different identities: a display name (like those in the example above) and a database key that the user need not worry about.

## Form Instance Identity ##

A form instance needs a visible identity as soon as an end user begins filling it out. It should be short and readable because end users may have to type it or compare it to other identities.

Proposal: Assign a random integer. Encode the integer according to the Crockford Base32 algorithm (5 bits per character). It uses a subset of digits and letters of the alphabet to minimize the risk of confusion. Limit the value set to six characters, i.e. 30 bits, around 10^9^. Insert an optional separator for clarity. The algorithm works in both directions.

Examples of identifiers shown to the user: `23X-3ZS, V2V-7AM, GBH-R4G`

## Form Definition Metadata ##

Form definitions carry a title and a description. This is the built-in metadata that Orbeon Forms manages.

Would it be possible to extend the embedded set of metadata? Probably not. The main reason is the Orbeon form editor. It generates all, or almost all form definitions in the application. The editor accepts arbitrary extra metadata when it reads a form definition. However, it strips away everything except standard Orbeon metadata on output.

The conclusion is that metadata except title and description must be added as read-only form fields. Since form fields are fully visible to users they must have a localized (Swedish) label.

The following subset of the Dublin Core Metadata Element Set is used. For each element the Swedish label is noted in brackets, and then (in italics) the definition of the element, optionally followed by a comment.


* **title**  
 [-] *A name given to the resource.*

 Typically, a Title will be a name by which the resource is formally known.  To a user the title appears as a headline at the top of the form.


* **description**  
 [-] *An account of the resource.*

 To a user the description appears as fixed text after the title.


* **identifier**  
 [Beteckning] *An unambiguous reference to the resource within a given context.*

 Orbeon app and form names separated by a slash (/) as defined above.


* **publisher**  
 [Utgivare] *An entity responsible for making the resource available.*

 A sequence of names of organizational units, separated by comma.  The sequence starts with the city name, and ends with the smallest organizational unit.


* **language**  
 [Språk] *A language of the resource.*

 A two-letter language code.  The language of a form definition is present in the XML text, but would not be visible to users  if this field was not included.

## Form Instance Metadata ##

A form instance in its final form is a PDF document built from an Orbeon form definition and form data. XML form data as generated by Orbeon carries no metadata. Form instance metadata refers to metadata stored in the PDF document. Most of it occurs twice,


1. As an XMP packet, invisible to users


2. As visible text, the source of which is DocBook XML. Localized (Swedish) labels are needed.

Some form instance metadata is copied from the form definition. Other metadata is retrieved from the application database.

The following subset of the Dublin Core Metadata Element Set is used. For each element the Swedish label is noted in brackets, and then (in italics) the definition of the element optionally followed by a comment. Elements with an asterisk (*) are copied from form definition metadata.


* **title**  
 [-]* *A name given to the resource.*


* **description**  
 [-]* *An account of the resource.*


* **identifier**  
 [Beteckning] *An unambiguous reference to the resource within a given context.*

 The Base32-encoded identity as defined above.


* **language**  
 [Språk]* *A language of the resource.*

 A two-letter language code copied from the form definition.


* **relation**  
 [Formulär]* *A related resource.*

 The identity of the form definition, as defined above.


* **publisher**  
 [Utgivare]* *An entity responsible for making the resource available.*

 This element refers to the publisher of the form definition.


* **date**  
 [Utgåva] *A point or period of time associated with an event in the lifecycle of the resource.*

 Date when the form definition was published.





