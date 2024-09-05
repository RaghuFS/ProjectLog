DELETE FROM workdetails.project_table;

INSERT INTO workdetails.project_table(
	project_id, description, project_name)
	VALUES (2,'Assist-Leave MS has Leave,Holiday resources' ,'Assist-Leave' );
	
INSERT INTO workdetails.project_table(
	project_id, description, project_name)
	VALUES (1,'Assist-Admin MS has Auth,Resource,Role resources' ,'Assist-Admin' );	
	
INSERT INTO workdetails.project_table(
	project_id, description, project_name)
	VALUES (3,'Assist-Admin MS has WorkLog,Project resources' ,'Assist-ProjectLog' );	
	
	
DELETE FROM workdetails.worklog_table;	
