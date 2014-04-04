--
-- Create tables with their constraints (PK, UNIQUE,...) and other indexes
--

CREATE TABLE mig_package
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  date_created timestamp without time zone,
  origin_local boolean,
  package_format character varying(255) NOT NULL,
  package_name character varying(120) NOT NULL,
  site_name character varying(120) NOT NULL,
  site_tstamp timestamp without time zone NOT NULL,
  CONSTRAINT mig_package_pkey PRIMARY KEY (id)
);

CREATE TABLE mig_formdef
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  app character varying(120) NOT NULL,
  created timestamp without time zone NOT NULL,
  current_draft character varying(400) NOT NULL,
  form character varying(120) NOT NULL,
  pack_id bigint NOT NULL,
  ref bigint NOT NULL,
  updated timestamp without time zone NOT NULL,
  uuid character varying(200) NOT NULL,
  CONSTRAINT mig_formdef_pkey PRIMARY KEY (id),
  CONSTRAINT fka336e2cddea8e74a FOREIGN KEY (pack_id)
      REFERENCES mig_package (id)
);

CREATE TABLE mig_formdef_ver
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  app character varying(120) NOT NULL,
  created timestamp without time zone NOT NULL,
  description character varying(800),
  draft integer,
  form character varying(120) NOT NULL,
  formdef_id bigint NOT NULL,
  formref bigint NOT NULL,
  language character varying(80),
  pack_id bigint NOT NULL,
  path character varying(400) NOT NULL,
  published boolean,
  ref bigint NOT NULL,
  title character varying(120),
  verno integer NOT NULL,
  CONSTRAINT mig_formdef_ver_pkey PRIMARY KEY (id),
  CONSTRAINT fk946bab312e1db41d FOREIGN KEY (formdef_id)
      REFERENCES mig_formdef (id),
  CONSTRAINT fk946bab31dea8e74a FOREIGN KEY (pack_id)
      REFERENCES mig_package (id)
);

CREATE TABLE mig_item
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  created timestamp without time zone NOT NULL,
  format character varying(80) NOT NULL,
  formdef_id bigint NOT NULL,
  formpath character varying(400),
  formref bigint,
  pack_id bigint NOT NULL,
  path character varying(400) NOT NULL,
  ref bigint NOT NULL,
  sha1 character varying(400) NOT NULL,
  size integer NOT NULL,
  stream bytea,
  text text,
  uuid character varying(200) NOT NULL,
  CONSTRAINT mig_item_pkey PRIMARY KEY (id),
  CONSTRAINT fka27dc4872e1db41d FOREIGN KEY (formdef_id)
      REFERENCES mig_formdef (id),
  CONSTRAINT fka27dc487dea8e74a FOREIGN KEY (pack_id)
      REFERENCES mig_package (id)
);

CREATE TABLE mig_report
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  body text NOT NULL,
  pkg_id bigint NOT NULL,
  tstamp timestamp without time zone NOT NULL,
  CONSTRAINT mig_report_pkey PRIMARY KEY (id),
  CONSTRAINT fk8ac11a8d3f696b7 FOREIGN KEY (pkg_id)
      REFERENCES mig_package (id)
);
