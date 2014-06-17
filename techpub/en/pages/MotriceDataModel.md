# Motrice Datamodel #


|motricedb|
|---------|
|mtf_process_activity_form_instance |processactivityforminstanceid|bigint|
| |activityinstanceuuid|character varying(255)|
| |formdocid|character varying(255)|
| |formpath|character varying(255)|
| |processinstanceuuid|character varying(255)|
| |submitted|timestamp without timezone|
| |userid|character varying(255)|
| |startformdefinitionid|bigint|
|mtf_process_activity_tag |processactivitytagid|bigint|
| |timestamp|timestamp without timezone|
| |userid|character varying(255)|
| |value|character varying(255)|
| |processactivityforminstanceid|bigint|
| |tagtypeid|bigint|
|mtf_activity_form_definition |activityformdefinitionid|bigint|
| |activitydefinitionuuid|character varying(255)|
| |formpath|character varying(255)|
| |startformdefinitionid|bigint|
|mtf_start_form_definition|startformdefinitionid|bigint|
| |authtypereq|character varying(255)|
| |formpath|character varying(255)|
| |processdefinitionuuid|character varying(255)|
| |userdataxpath|character varying(255)|
|mtf_tag_type|tagtypeid|bigint|
| |label|character varying(255)|
| |name|character varying(255)|
|motrice_user|userid|bigint|
| |category|bigint|
| |cn|character varying(255)|
| |dn|character varying(255)|
| |gn|character varying(255)|
| |serial|character varying(255)|
| |sn|character varying(255)|
| |uuid|character varying(255)|










