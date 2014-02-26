CREATE TABLE motrice_user (
    userid bigint NOT NULL,
    category integer NOT NULL,
    cn character varying(255) NOT NULL,
    dn character varying(255),
    gn character varying(255),
    serial character varying(255),
    sn character varying(255),
    uuid character varying(255) NOT NULL
);

ALTER TABLE ONLY motrice_user
    ADD CONSTRAINT motrice_user_pkey PRIMARY KEY (userid);

ALTER TABLE ONLY motrice_user
    ADD CONSTRAINT motrice_user_uuid_key UNIQUE (uuid);

CREATE TABLE mtf_form_type (
    formTypeId bigint PRIMARY KEY,
    label character varying(255),
    formHandlerBean   character varying(255) -- spring bean id
);

CREATE TABLE mtf_activity_form_definition (
    activityformdefinitionid bigint NOT NULL,
    processdefinitionuuid character varying(255),
    activitydefinitionuuid character varying(255),
    formTypeId		bigint NOT NULL REFERENCES mtf_form_type (formTypeId),
    formDefinitionKey   character varying(255)
);

ALTER TABLE ONLY mtf_activity_form_definition
    ADD CONSTRAINT mtf_activity_form_definition_pkey PRIMARY KEY (activityformdefinitionid);


CREATE TABLE mtf_process_activity_form_instance (
    processactivityforminstanceid bigint NOT NULL,
    activityinstanceuuid character varying(255),
    formdocid character varying(255) NOT NULL,
    formTypeId		bigint NOT NULL REFERENCES mtf_form_type (formTypeId),
    formDefinitionKey   character varying(255),
    processinstanceuuid character varying(255),
    submitted timestamp without time zone,
    userid character varying(255) NOT NULL,
    startformdefinitionid bigint
);

ALTER TABLE ONLY mtf_process_activity_form_instance
    ADD CONSTRAINT mtf_process_activity_form_instance_pkey PRIMARY KEY (processactivityforminstanceid);

ALTER TABLE ONLY mtf_process_activity_form_instance
    ADD CONSTRAINT mtf_process_activity_form_instance_formdocid_key UNIQUE (formdocid);


CREATE TABLE mtf_process_activity_tag (
    processactivitytagid bigint NOT NULL,
    "timestamp" timestamp without time zone NOT NULL,
    userid character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    processactivityforminstanceid bigint NOT NULL,
    tagtypeid bigint NOT NULL
);

ALTER TABLE ONLY mtf_process_activity_tag
    ADD CONSTRAINT mtf_process_activity_tag_pkey PRIMARY KEY (processactivitytagid);


CREATE TABLE mtf_start_form_definition (
    startformdefinitionid bigint NOT NULL,
    authtypereq character varying(255) NOT NULL,
    formTypeId		bigint NOT NULL REFERENCES mtf_form_type (formTypeId),
    formDefinitionKey   character varying(255),
    processdefinitionuuid character varying(255),
    userdataxpath character varying(255)
);

ALTER TABLE ONLY mtf_start_form_definition
    ADD CONSTRAINT mtf_start_form_definition_pkey PRIMARY KEY (startformdefinitionid);

ALTER TABLE ONLY mtf_start_form_definition
    ADD CONSTRAINT mtf_start_form_definition_formpath_key UNIQUE (formTypeId, formDefinitionKey);


CREATE TABLE mtf_tag_type (
    tagtypeid bigint NOT NULL,
    label character varying(255) NOT NULL,
    name character varying(255) NOT NULL
);

ALTER TABLE ONLY mtf_tag_type
    ADD CONSTRAINT mtf_tag_type_pkey PRIMARY KEY (tagtypeid);

ALTER TABLE ONLY mtf_tag_type
    ADD CONSTRAINT mtf_tag_type_name_key UNIQUE (name);

ALTER TABLE ONLY mtf_process_activity_tag
    ADD CONSTRAINT fk4146f6dac31d8210 FOREIGN KEY (tagtypeid) REFERENCES mtf_tag_type(tagtypeid);

ALTER TABLE ONLY mtf_process_activity_tag
    ADD CONSTRAINT fk4146f6dad5e10f16 FOREIGN KEY (processactivityforminstanceid) REFERENCES mtf_process_activity_form_instance(processactivityforminstanceid);

ALTER TABLE ONLY mtf_process_activity_form_instance
    ADD CONSTRAINT fk606cfcf095f89dba FOREIGN KEY (startformdefinitionid) REFERENCES mtf_start_form_definition(startformdefinitionid);


--
-- Initial data that _must_ exist 
--

--
-- mtf_tag_type
--

INSERT INTO mtf_tag_type(
            tagtypeid, label, name)
    VALUES (1, 'Diarienr', 'diary_no');

INSERT INTO mtf_tag_type(
            tagtypeid, label, name)
    VALUES (2, 'Ansökan av', 'application_by');

INSERT INTO mtf_tag_type(
            tagtypeid, label, name)
    VALUES (10000, 'Annan', 'other');

