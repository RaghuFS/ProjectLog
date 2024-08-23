-- SCHEMA: workdetails

-- DROP SCHEMA IF EXISTS workdetails ;

CREATE SCHEMA IF NOT EXISTS workdetails
    AUTHORIZATION postgres;
	
	
-- Table: workdetails.worklog_table

-- DROP TABLE IF EXISTS workdetails.worklog_table;

CREATE TABLE IF NOT EXISTS workdetails.worklog_table
(
	worklog_id int4 NOT NULL,
	task_description varchar(255) NULL,
	end_time varchar(255) NULL,
	"module" varchar(255) NULL,
	project_name varchar(255) NULL,
	resource_id varchar(255) NULL,
	start_time varchar(255) NULL,
	total_time varchar(255) NULL,
	work_date date NULL,
	CONSTRAINT worklog_table_pkey PRIMARY KEY (worklog_id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS workdetails.worklog_table
    OWNER to postgres;
	
	
-- Table: workdetails.project

-- DROP TABLE IF EXISTS workdetails.project;

CREATE TABLE IF NOT EXISTS workdetails.project_table
(
    project_id integer NOT NULL,
    description character varying(255) COLLATE pg_catalog."default",
    project_name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT project_pkey PRIMARY KEY (project_id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS workdetails.project_table
    OWNER to postgres;	
