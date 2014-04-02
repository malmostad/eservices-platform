--
-- Initial data that _must_ exist 
--

--
-- mtf_form_type
--

INSERT INTO mtf_form_type(
            formtypeid, label, formhandlerbean)
    VALUES (1, 'Orbeon', 'motriceOrbeonFormHandler');
INSERT INTO mtf_form_type(
            formtypeid, label, formhandlerbean)
    VALUES (2, 'SignStartForm', 'motriceSignStartFormHandler');
INSERT INTO mtf_form_type(
            formtypeid, label, formhandlerbean)
    VALUES (3, 'SignTaskForm', 'motriceSignTaskFormHandler');
INSERT INTO mtf_form_type(
            formtypeid, label, formhandlerbean)
    VALUES (4, 'PayTask', 'motricePaymentFormHandler');
INSERT INTO mtf_form_type(
            formtypeid, label, formhandlerbean)
    VALUES (5, 'NotifyAct', 'motriceNotifyActFormHandler');
INSERT INTO mtf_form_type(
            formtypeid, label, formhandlerbean)
    VALUES (6, 'No form', 'motriceNoFormHandler');


--
-- mtf_tag_type
--

INSERT INTO mtf_tag_type(
            tag_type_id, label, name)
    VALUES (1, 'Diarienr', 'diary_no');

INSERT INTO mtf_tag_type(
            tag_type_id, label, name)
    VALUES (2, 'Ansökan av', 'application_by');

INSERT INTO mtf_tag_type(
            tag_type_id, label, name)
    VALUES (10000, 'Annan', 'other');

