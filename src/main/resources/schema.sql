-- SCHEMA: workdetails

-- DROP SCHEMA IF EXISTS workdetails ;

CREATE SCHEMA IF NOT EXISTS workdetails
    AUTHORIZATION postgres;
	
	
-- Table: workdetails.worklog_table

-- DROP TABLE IF EXISTS workdetails.worklog_table;

CREATE TABLE IF NOT EXISTS workdetails.worklog_table
(
    work_log_id integer NOT NULL,
    description character varying(255) COLLATE pg_catalog."default",
    module character varying(255) COLLATE pg_catalog."default",
    project character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT worklog_table_pkey PRIMARY KEY (work_log_id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS workdetails.worklog_table
    OWNER to postgres;
	
	
-- Table: workdetails.project

-- DROP TABLE IF EXISTS workdetails.project;

CREATE TABLE IF NOT EXISTS workdetails.project
(
    project_id integer NOT NULL,
    description character varying(255) COLLATE pg_catalog."default",
    project_name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT project_pkey PRIMARY KEY (project_id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS workdetails.project
    OWNER to postgres;	
