DELETE FROM workdetails.project;

INSERT INTO workdetails.project(
	project_id, description, project_name)
	VALUES (2,'Assist-Leave MS has Leave,Holiday resources' ,'Assist-Leave' );
	
INSERT INTO workdetails.project(
	project_id, description, project_name)
	VALUES (1,'Assist-Admin MS has Auth,Resource,Role resources' ,'Assist-Admin' );	
	
INSERT INTO workdetails.project(
	project_id, description, project_name)
	VALUES (3,'Assist-Admin MS has WorkLog,Project resources' ,'Assist-ProjectLog' );	
	
	
DELETE FROM workdetails.worklog_table;	

INSERT INTO workdetails.worklog_table(
	work_log_id, description, module, project)
	VALUES (1, 'coding is in progress', 'Auth Module', 'Assist-Admin');
	
INSERT INTO workdetails.worklog_table(
	work_log_id, description, module, project)
	VALUES (2, 'Testing is in progress', 'Resource Module', 'Assist-Admin');	
	
INSERT INTO workdetails.worklog_table(
	work_log_id, description, module, project)
	VALUES (3, 'Coding is done', 'WorkLog Module', 'Assist-ProjectLog');	
	
INSERT INTO workdetails.worklog_table(
	work_log_id, description, module, project)
	VALUES (4, 'testing is in progress', 'Project Module', 'Assist-ProjectLog');	