drop database if exists stitch_galaxy;
create database stitch_galaxy;
use stitch_galaxy;

drop table if exists language;
create table language
(
id int not null auto_increment,
name varchar(10) not null,
description tinytext null,
primary key (id)
) charset utf8 collate utf8_unicode_ci engine=InnoDB comment='supported languages';

insert into language (name, description) values
('en', 'English general'),
('es', 'Spanish'),
('pt', 'Portuguese'),
('zh-Hans', 'Chinese in the simplified script'),
('zh-Hant', 'Chinese in the traditional script'),
('zh', 'Chinese general'),
('it', 'Italian'),
('ru', 'Russian'),
('nl', 'Dutch'),
('fr', 'French'),
('de', 'German'),
('th', 'Thai'),
('ht', 'Haitian Creole'),
('gl', 'Galician'),
('ga', 'Irish'),
('sv', 'Swedish'),
('ja', 'Japanese'),
('uk', 'Ukrainian'),
('hi', 'Hindi'),
('ar', 'Arabic'),
('be', 'Belarusian');


drop table if exists design;
create table design
(
uuid varbinary(16) not null,
release_date datetime not null,
sales bigint not null,
total_rating bigint not null,
total_rates bigint not null,
blocked bit not null,
primary key (uuid)
) charset utf8 collate utf8_unicode_ci engine=InnoDB;

drop table if exists design_language_parameter;
create table design_language_parameter
(
id bigint auto_increment not null,
design_uuid varbinary(16) not null,
language_id int not null,
width int not null,
heigth int not null,
colors int not null,
has_thumbnail bit not null,
has_image bit not null,
has_web_description bit not null,
has_design bit not null,
primary key (id),
unique key (design_uuid, language_id),
foreign key (design_uuid) references design (uuid),
foreign key (language_id) references language (id)
) charset utf8 collate utf8_unicode_ci engine=InnoDB;

drop table if exists design_language_textual_parameter;
create table design_language_textual_parameter
(
id bigint auto_increment not null,
design_uuid varbinary(16) not null,
language_id int not null,
name varchar(255) not null,
description text null,
web_description text null,
primary key (id),
unique key (design_uuid, language_id),
foreign key (design_uuid) references design (uuid),
foreign key (language_id) references language (id),
fulltext (name, description, web_description)
) charset utf8 collate utf8_unicode_ci engine=MyISAM;

drop table if exists tag;
create table tag
(
id int auto_increment,
primary key (id)
) charset utf8 collate utf8_unicode_ci engine=InnoDb;

drop table if exists tag_language_name;
create table tag_language_name
(
id bigint auto_increment not null,
tag_id int not null,
language_id int not null,
name varchar(255) not null,
primary key (id),
unique key (tag_id, language_id),
foreign key (tag_id) references tag (id),
foreign key (language_id) references language (id),
fulltext(name)
) charset utf8 collate utf8_unicode_ci engine=MyISAM;

drop table if exists design_tag;
create table design_tag
(
id bigint auto_increment,
design_uuid varbinary(16),
tag_id int,
primary key(id),
unique key(design_uuid, tag_id),
foreign key (design_uuid) references design (uuid),
foreign key (tag_id) references tag (id)
) charset utf8 collate utf8_unicode_ci engine=InnoDB;