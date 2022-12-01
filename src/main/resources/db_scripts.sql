create database s3_db;
use s3_db;

create table image_metadata(
       id int,
       name varchar(255),
       content_type varchar(255),
       metadata nvarchar(500),
       created_at timestamp
);

alter table s3_db.image_metadata
    add constraint image_metadata_pk
        primary key (id);

alter table s3_db.image_metadata
    modify id int auto_increment;

alter table s3_db.image_metadata
    auto_increment = 1;

alter table s3_db.image_metadata
    modify name varchar(255) not null;

alter table s3_db.image_metadata
    change metadata original_name varchar(500) charset utf8 null;

alter table s3_db.image_metadata
    add file_size int null after original_name;

