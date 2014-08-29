CREATE TABLE pxd_formdef (
    id bigint NOT NULL,
    version bigint NOT NULL,
    app_name character varying(120) NOT NULL,
    current_draft character varying(400),
    date_created timestamp without time zone,
    form_name character varying(120) NOT NULL,
    last_updated timestamp without time zone,
    path character varying(256) NOT NULL,
    uuid character varying(200) NOT NULL
);

ALTER TABLE ONLY pxd_formdef
    ADD CONSTRAINT pxd_formdef_pkey PRIMARY KEY (id);

ALTER TABLE ONLY pxd_formdef
    ADD CONSTRAINT pxd_formdef_path_key UNIQUE (path);

ALTER TABLE ONLY pxd_formdef
    ADD CONSTRAINT pxd_formdef_uuid_key UNIQUE (uuid);


CREATE TABLE pxd_formdef_ver (
    id bigint NOT NULL,
    version bigint NOT NULL,
    app_name character varying(120) NOT NULL,
    author character varying(80),
    date_created timestamp without time zone,
    description character varying(800),
    draft integer NOT NULL,
    form_name character varying(120) NOT NULL,
    formdef_id bigint NOT NULL,
    fvno integer NOT NULL,
    language character varying(16),
    last_updated timestamp without time zone,
    logo_ref character varying(800),
    path character varying(400) NOT NULL,
    title character varying(120)
);

ALTER TABLE ONLY pxd_formdef_ver
    ADD CONSTRAINT pxd_formdef_ver_pkey PRIMARY KEY (id);

ALTER TABLE ONLY pxd_formdef_ver
    ADD CONSTRAINT pxd_formdef_ver_path_key UNIQUE (path);


CREATE TABLE pxd_item (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone,
    form_def character varying(400),
    format character varying(80) NOT NULL,
    instance boolean NOT NULL,
    last_updated timestamp without time zone,
    path character varying(255) NOT NULL,
    size integer NOT NULL,
    stream bytea,
    text text,
    uuid character varying(200),
    origin_id bigint,
    read_only boolean
);

ALTER TABLE ONLY pxd_item
    ADD CONSTRAINT pxd_item_pkey PRIMARY KEY (id);

ALTER TABLE ONLY pxd_item
    ADD CONSTRAINT pxd_item_path_key UNIQUE (path);

CREATE INDEX formdef_idx ON pxd_item USING btree (form_def);

CREATE INDEX uuid_idx ON pxd_item USING btree (uuid);

ALTER TABLE ONLY pxd_formdef_ver
    ADD CONSTRAINT fk1273a6c21af9b303 FOREIGN KEY (formdef_id) REFERENCES pxd_formdef(id);
