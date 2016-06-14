drop SCHEMA fileservice CASCADE;
create schema fileservice;
create table fileservice.instance (
  i_id BIGSERIAL PRIMARY KEY,
  i_name TEXT,
  i_display_name TEXT,
  i_description TEXT,
  i_cluster_name TEXT,
  i_tempDir TEXT,
  i_webHDFSUsername TEXT,
  i_webHDFSAuth TEXT
);


INSERT into fileservice.instance(i_name, i_display_name, i_description,
 i_cluster_name, i_tempDir, i_webHDFSUsername)
  VALUES('test','test','test','test','/tmp','admin');
