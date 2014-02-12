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
(
  actid character varying(64) NOT NULL,
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
