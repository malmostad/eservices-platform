--
-- Create tables with their constraints (PK, UNIQUE,...) and other indexes
--

CREATE TABLE box_contents (
    id bigint NOT NULL,
    version bigint NOT NULL,
    checksum character varying(200),
    date_created timestamp without time zone,
    format character varying(80) NOT NULL,
    last_updated timestamp without time zone,
    name character varying(80) NOT NULL,
    size integer NOT NULL,
    step_id bigint NOT NULL,
    stream bytea,
    text text
);

ALTER TABLE ONLY box_contents
    ADD CONSTRAINT box_contents_pkey PRIMARY KEY (id);

ALTER TABLE ONLY box_contents
    ADD CONSTRAINT box_contents_step_id_name_key UNIQUE (step_id, name);


CREATE TABLE box_doc (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone,
    doc_no character varying(16) NOT NULL,
    form_data_uuid character varying(200) NOT NULL,
    last_updated timestamp without time zone
);

ALTER TABLE ONLY box_doc
    ADD CONSTRAINT box_doc_pkey PRIMARY KEY (id);

CREATE INDEX formdatauuid_idx ON box_doc USING btree (form_data_uuid);


CREATE TABLE box_doc_step (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone,
    doc_id bigint NOT NULL,
    doc_no character varying(16) NOT NULL,
    docbox_ref character varying(200) NOT NULL,
    last_updated timestamp without time zone,
    sign_count integer NOT NULL,
    step integer NOT NULL
);

ALTER TABLE ONLY box_doc_step
    ADD CONSTRAINT box_doc_step_pkey PRIMARY KEY (id);

CREATE INDEX docboxref_idx ON box_doc_step USING btree (docbox_ref);

ALTER TABLE ONLY box_doc_step
    ADD CONSTRAINT fk3b26c567ada4faf3 FOREIGN KEY (doc_id) REFERENCES box_doc(id);

ALTER TABLE ONLY box_contents
    ADD CONSTRAINT fke72c630e5eb37b4b FOREIGN KEY (step_id) REFERENCES box_doc_step(id);