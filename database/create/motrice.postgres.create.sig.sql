--
-- SIGNATRICE TABLES, NOW PART OF DOCBOX
--

CREATE TABLE sig_displayname
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  name character varying(120) NOT NULL,
  CONSTRAINT sig_displayname_pkey PRIMARY KEY (id),
  CONSTRAINT sig_displayname_name_key UNIQUE (name)
);

CREATE TABLE sig_fault_obj
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  details text,
  status character varying(80) NOT NULL,
  CONSTRAINT sig_fault_obj_pkey PRIMARY KEY (id)
);

CREATE TABLE sig_policy
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  name character varying(40) NOT NULL,
  CONSTRAINT sig_policy_pkey PRIMARY KEY (id),
  CONSTRAINT sig_policy_name_key UNIQUE (name)
);

CREATE TABLE sig_progress
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  name character varying(80) NOT NULL,
  CONSTRAINT sig_progress_pkey PRIMARY KEY (id),
  CONSTRAINT sig_progress_name_key UNIQUE (name)
);

CREATE TABLE sig_service
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  alias character varying(24) NOT NULL,
  default_display_name_id bigint NOT NULL,
  default_policy_id bigint NOT NULL,
  q_name_local_part character varying(120) NOT NULL,
  q_name_uri character varying(200) NOT NULL,
  wsdl_location character varying(200) NOT NULL,
  CONSTRAINT sig_service_pkey PRIMARY KEY (id),
  CONSTRAINT fkbf37c9475ed41ae8 FOREIGN KEY (default_display_name_id)
      REFERENCES sig_displayname (id),
  CONSTRAINT fkbf37c947a1d215d5 FOREIGN KEY (default_policy_id)
      REFERENCES sig_policy (id),
  CONSTRAINT sig_service_alias_key UNIQUE (alias),
  CONSTRAINT sig_service_wsdl_location_key UNIQUE (wsdl_location)
);

CREATE TABLE sig_scheme
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  date_created timestamp without time zone,
  display_name_id bigint NOT NULL,
  name character varying(120) NOT NULL,
  policy_id bigint NOT NULL,
  service_id bigint NOT NULL,
  CONSTRAINT sig_scheme_pkey PRIMARY KEY (id),
  CONSTRAINT fkc3f998d3415b5ee1 FOREIGN KEY (service_id)
      REFERENCES sig_service (id),
  CONSTRAINT fkc3f998d358e990e6 FOREIGN KEY (display_name_id)
      REFERENCES sig_displayname (id),
  CONSTRAINT fkc3f998d35ebf6253 FOREIGN KEY (policy_id)
      REFERENCES sig_policy (id),
  CONSTRAINT sig_scheme_name_key UNIQUE (name)
);

CREATE TABLE sig_default_scheme
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  default_scheme_id bigint,
  last_updated timestamp without time zone,
  CONSTRAINT sig_default_scheme_pkey PRIMARY KEY (id),
  CONSTRAINT fkfcde5691aa9c5cf5 FOREIGN KEY (default_scheme_id)
      REFERENCES sig_scheme (id)
);

CREATE TABLE sig_testcase
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  name character varying(80) NOT NULL,
  personal_id_no character varying(24),
  scheme_id bigint NOT NULL,
  user_visible_text text NOT NULL,
  CONSTRAINT sig_testcase_pkey PRIMARY KEY (id),
  CONSTRAINT fk913876706789a973 FOREIGN KEY (scheme_id)
      REFERENCES sig_scheme (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT sig_testcase_name_key UNIQUE (name)
);

CREATE TABLE sig_result
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  auto_start_token character varying(120),
  date_created timestamp without time zone NOT NULL,
  display_name_id bigint,
  docbox_ref_in character varying(200),
  docbox_ref_out character varying(200),
  fault_status_id bigint,
  order_ref character varying(120),
  personal_id_no character varying(24),
  policy_id bigint,
  progress_status_id bigint,
  scheme_id bigint NOT NULL,
  signature text,
  transaction_id character varying(32),
  finish_conflict character varying(400),
  sig_tstamp timestamp without time zone,
  CONSTRAINT sig_result_pkey PRIMARY KEY (id),
  CONSTRAINT fkc2662b4b58e990e6 FOREIGN KEY (display_name_id)
      REFERENCES sig_displayname (id),
  CONSTRAINT fkc2662b4b5ebf6253 FOREIGN KEY (policy_id)
      REFERENCES sig_policy (id),
  CONSTRAINT fkc2662b4b6789a973 FOREIGN KEY (scheme_id)
      REFERENCES sig_scheme (id),
  CONSTRAINT fkc2662b4b8e5129b9 FOREIGN KEY (fault_status_id)
      REFERENCES sig_fault_obj (id),
  CONSTRAINT fkc2662b4be1f5f2bc FOREIGN KEY (progress_status_id)
      REFERENCES sig_progress (id)
);

CREATE TABLE sig_attribute
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  name character varying(80) NOT NULL,
  result_id bigint NOT NULL,
  value text NOT NULL,
  CONSTRAINT sig_attribute_pkey PRIMARY KEY (id),
  CONSTRAINT fkdcc550ae29ef673 FOREIGN KEY (result_id)
      REFERENCES sig_result (id)
);
