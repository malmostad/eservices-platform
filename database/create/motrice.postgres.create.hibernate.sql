--
-- Create SEQUENCE used by Hibernate
--

--
-- WORKAROUND START WITH 1000 because of COPY above update hibernate_sequence
-- TODO load demo data with inserts and nextval('hibernate_sequence'), 
--      instead of COPY 
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

