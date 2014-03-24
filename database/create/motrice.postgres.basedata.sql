--
-- Initialize a motrice database with some demo data 
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


