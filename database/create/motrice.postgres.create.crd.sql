--
-- Create tables with their constraints (PK, UNIQUE,...) and other indexes
--

CREATE TABLE crd_proc_category
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  date_created timestamp without time zone,
  description text,
  last_updated timestamp without time zone,
  name character varying(200) NOT NULL,
  CONSTRAINT crd_proc_category_pkey PRIMARY KEY (id),
  CONSTRAINT crd_proc_category_name_key UNIQUE (name )
);

CREATE TABLE crd_procdef_state
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  activiti_state bigint NOT NULL,
  default_message character varying(80) NOT NULL,
  editable boolean NOT NULL,
  res character varying(80) NOT NULL,
  startable integer NOT NULL,
  CONSTRAINT crd_procdef_state_pkey PRIMARY KEY (id)
);

CREATE TABLE crd_procdef
( actid character varying(64) NOT NULL,
  version bigint NOT NULL,
  actdepl character varying(64) NOT NULL,
  actver integer NOT NULL,
  state_id bigint NOT NULL,
  CONSTRAINT crd_procdef_pkey PRIMARY KEY (actid),
  CONSTRAINT fkc1fd9c859157386b FOREIGN KEY (state_id)
      REFERENCES crd_procdef_state (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE crd_task_type
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  default_message character varying(80) NOT NULL,
  res character varying(80) NOT NULL,
  CONSTRAINT crd_task_type_pkey PRIMARY KEY (id )
);

CREATE TABLE crd_i18n_act_label
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  actdef_id character varying(255) NOT NULL,
  actdef_name character varying(255) NOT NULL,
  label character varying(255),
  locale character varying(24) NOT NULL,
  procdef_key character varying(255) NOT NULL,
  procdef_ver integer NOT NULL,
  CONSTRAINT crd_i18n_act_label_pkey PRIMARY KEY (id),
  CONSTRAINT crd_i18n_act_label_locale_actdef_name_procdef_key_procdef_v_key UNIQUE (locale, actdef_name, procdef_key, procdef_ver),
  CONSTRAINT crd_i18n_act_label_locale_actdef_name_procdef_ver_procdef_k_key UNIQUE (locale, actdef_name, procdef_ver, procdef_key, label)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE crd_i18n_act_label
  OWNER TO motriceuser;

CREATE TABLE crd_i18n_form_label
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  formdef_id bigint NOT NULL,
  formdef_ver integer NOT NULL,
  label character varying(255),
  locale character varying(255) NOT NULL,
  CONSTRAINT crd_i18n_form_label_pkey PRIMARY KEY (id),
  CONSTRAINT crd_i18n_form_label_formdef_path_formdef_ver_key UNIQUE (formdef_id, formdef_ver),
  CONSTRAINT crd_i18n_form_label_formdef_ver_formdef_path_label_key UNIQUE (formdef_ver, formdef_id, label)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE crd_i18n_form_label
  OWNER TO motriceuser;

CREATE TABLE crd_i18n_guide_url
(
  id bigint NOT NULL,
  version bigint NOT NULL,
  pattern character varying(400),
  procdef_key character varying(255) NOT NULL,
  procdef_ver integer NOT NULL,
  CONSTRAINT crd_i18n_guide_url_pkey PRIMARY KEY (id),
  CONSTRAINT crd_i18n_guide_url_procdef_key_procdef_ver_key UNIQUE (procdef_key, procdef_ver),
  CONSTRAINT crd_i18n_guide_url_procdef_ver_procdef_key_pattern_key UNIQUE (procdef_ver, procdef_key, pattern)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE crd_i18n_guide_url
  OWNER TO motriceuser;
