drop database if exists stitch_galaxy;

create database stitch_galaxy;

use stitch_galaxy;

drop table if exists design;
create table design
(
uuid varbinary(16),
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
id bigint auto_increment,
design_uuid varbinary(16),
language varchar(10) not null,
width int null,
heigth int null,
colors int null,
has_thumbnail bit not null,
has_image bit not null,
has_web_description bit not null,
has_design bit not null,
primary key (id)
) charset utf8 collate utf8_unicode_ci engine=InnoDB;

alter table design_language_parameter
	add foreign key (design_uuid)
		references design (uuid);

drop table if exists design_language_textual_parameter;
create table design_language_textual_parameter
(
id bigint auto_increment,
design_uuid varbinary(16),
language varchar(10) not null,
name nvarchar(255) not null,
description text null,
web_description text null,
primary key (id),
fulltext(name, description, web_description)
) charset utf8 collate utf8_unicode_ci engine=MyISAM;

alter table design_language_textual_parameter
	add foreign key (design_uuid)
		references design (uuid);

drop table if exists design_tag;
create table design_tag
(
	design_uuid varbinary(16),
	tag_id int
) charset utf8 collate utf8_unicode_ci engine=InnoDB;

alter table design_tag
	add foreign key (design_uuid)
		references design (uuid);

drop function if exists uuid_from_bin;
DELIMITER $$
create function uuid_from_bin(b binary(16))
returns char(36) deterministic
begin
declare hex char(32);
set hex = hex(b);
return concat(left(hex, 8), '-', mid(hex, 9,4), '-', mid(hex, 13,4), '-', mid(hex, 17,4), '-', right(hex, 12));
end
$$
DELIMITER ;
 
drop function if exists uuid_to_bin;
DELIMITER $$
create function uuid_to_bin(s char(36))
returns binary(16) deterministic
return unhex(concat(left(s, 8), mid(s, 10, 4), mid(s, 15, 4), mid(s, 20, 4), right(s, 12)))
$$
DELIMITER ;

drop procedure if exists cu_design;
delimiter $$
create procedure cu_design
(
in text_uuid char(36),
in release_date datetime,
in sales bigint,
in total_rating bigint,
in total_rates bigint,
in blocked bit
)
begin

	declare design_uuid varbinary(16);

	declare cur_release_date datetime;
	declare cur_sales bigint;
	declare cur_total_rating bigint;
	declare cur_total_rates bigint;
	declare cur_blocked bit;

	declare records_found int default 0;

	set design_uuid = uuid_to_bin(text_uuid);
	
	select count(*) into records_found from design where uuid = design_uuid;
	
	if records_found = 1
	then

		select release_date, sales, total_rating, total_rates, blocked into cur_release_date, cur_sales, cur_total_rating, cur_total_rates, cur_blocked from design where uuid = design_uuid;

		if release_date is null
		then
			set release_date = cur_release_date;
		end if;

		if sales is null
		then
			set sales = cur_sales;
		end if;

		if total_rating is null
		then
			set total_rating = cur_total_rating;
		end if;

		if total_rates is null
		then
			set total_rates = cur_total_rates;
		end if;

		if blocked is null
		then
			set blocked = cur_blocked;
		end if;
		
		update design
		set 
			release_date = release_date,
			sales = sales,
			total_rating = total_rating,
			total_rates = total_rates,
			blocked = total_blocked
		where
			uuid = design_uuid;
	else
		insert into T_DESIGNS(DesignUuid, ReleaseDate, Sales, TotalRating, TotalRates, Blocked)
		values (designUuid, releaseDate, sales, totalRating, totalRates, blocked);

	end if;

end$$
delimiter ;

#--------------------------#

drop procedure if exists cu_design_language_parameter;
delimiter $$
create procedure cu_design_language_parameter
(
in text_uuid char(36),
in language varchar(10),
in name varchar(255),
in description text,
in webDescription text,

in width int,
in height int,
in colors int,

in hasThumbnail bit,
in hasImage bit,
in hasWebDescription bit,
in hasDesign bit
)
begin

	declare design_uuid varbinary(16);

	declare parId bigint;

	set designUuid = uuid_to_bin(uuid);

	select  ParametersId into parId from T_DESIGN_PARAMETERS where DesignUuid = designUuid and Language = language LIMIT 1;
	
	if parId is null
	then
		insert into T_DESIGN_PARAMETERS(DesignUuid, Language, Name, Description, Width, Heigth, Colors, HasThumbnail, HasImage, HasWebDescription, HasDesign) 
		values(designUuid, language, name, description, width, height, colors, hasThumbnail, hasImage, hasWebDescription, hasDesign);
	else
		update T_DESIGN_PARAMETERS set DesignUuid = designUuid, Language = language, Name = name, Description = description, Width = width, Heigth = height, Colors = colors, HasThumbnail = hasThumbnail, HasImage = hasImage, HasWebDescription = hasWebDescription, HasDesign = hasDesign 
		where ParametersId = parId;
	end if;

end$$
delimiter ;

delimiter $$
create procedure getTagsList
(
	intlist text 
)
begin

declare comma int default 0;
declare mylist text default intlist;
declare temp text default '';
declare strlen int default length(intlist);


create temporary table TagsTempTable(TagId int);

SET comma = LOCATE(',',mylist);

while strlen > 0 do
	if
	comma = 0
	then
		set temp = TRIM(mylist);
		set mylist = '';
		set strlen = 0;
	else
		set temp = trim(substring(mylist,1,comma));
		set mylist = trim(substring(mylist from comma + 1));
		set strlen = length(mylist);
	end if;

	if cast(temp as unsigned) != 0
	then
		insert into TagsTempTable (TagId) values(cast(temp as unsigned));
	end if;
	set comma = locate(',',mylist);
end while;

end $$
delimiter ;

delimiter $$
create procedure searchDesigns
(
in searchString text,
in tagsList text,
in sortOrder int,
in pageNumber int,
in resultsOnPage int,
out totalResults bigint
)
begin

declare categoriesFilter bit default 0;
declare startFrom bigint;

declare rI int default 0;
declare rAlreadySelected bit default 1;

set startFrom = pageNumber * resultsOnPage;

if (length(trim(tagsList)) <> 0)
then
	call getTagsList(tagsList);
	set categoriesFilter = 1;
	select count(*) into totalResults from T_DESIGNS;
else
	select count(distinct degsignUuid) into totalResults from T_DESIGN_TAGS where TagId in (select TagId from TagsTempTable);
end if;

	#MOST_RECENT
	if sortOrder = 0
	then
		if categoriesFilter
		then
			select DesignUuid from T_DESIGNS
			where DesignUuid in (select DesignUuid from T_DESIGN_TAGS where TagId in (select TagId from TagsTempTable))
			order by ReleaseDate desc
			limit startFrom, resultsOnPage;
		else
			select DesignUuid from T_DESIGNS
			order by ReleaseDate desc
			limit startFrom, resultsOnPage;
		end if;
	end if;
	#TOP_SALE
	if sortOrder = 1
	then
		if categoriesFilter
		then
			select DesignUuid from T_DESIGNS
			where DesignUuid in (select DesignUuid from T_DESIGN_TAGS where TagId in (select TagId from TagsTempTable))
			order by Sales desc
			limit startFrom, resultsOnPage;
		else
			select DesignUuid from T_DESIGNS
			order by Sales desc
			limit startFrom, resultsOnPage;
		end if;
	end if;
	#RANDOM
	if sortOrder = 1
	then
		create temporary table tempSearchResults (DesignUuid varbinary(16), StartFrom bigint);

		while rI < resultsOnPage and rI < totalResults do
			set rAlreadySelected = 1;
			while rAlreadySelected do
				set startFrom = round(rand() * totalResults); 	
				select count(*) <> 0 into rAlreadySelected from tempSearchResults where StartFrom = startFrom;
			end while;
			
			#insert data into tempSearchResults
			if categoriesFilter
			then
				insert into tempSearchResults (DesignUuid)
				select DesignUuid from T_DESIGNS limit startFrom, 1;
			else
				insert into tempSearchResults (DesignUuid)
				select DesignUuid from T_DESIGNS 
				where DesignUuid in (select DesignUuid from T_DESIGN_TAGS where TagId in (select TagId from TagsTempTable))
				limit startFrom, 1;
			end if;
			#increment rI for main loop
			set rI = rI + 1;
		end while;
		
		select DesignUuid from tempSearchResults;
	end if;

#Mutually exclusive orders
#MOST_RECENT
#TOP_SALE
#RANDOM
#TOP_RATE
#MOST_RELEVANT
#PRICE_ASC
#PRICE_DESC
#COMPLEXITY_ASC
#COMPLEXITY_DESC

end$$
delimiter ;

delimiter $$
create procedure getDesignInfo
(
in designUuid varbinary(16),
in language varchar(10)
)
begin
	select 
		d.ReleaseDate, 
		d.Sales, 
		d.TotalRating, 
		d.TotalRates, 
		dp.Name,
		dp.Description,
		dp.Width,
		dp.Heigth,
		dp.Colors,
		dp.HasThumbnail,
		dp.HasImage,
		dp.HasWebDescription,
		dp.HasDesign
 from T_DESIGNS d
	INNER JOIN T_DESIGN_PARAMETERS dp on d.DesignUuid = dp.DesignUuid
	where dp.Language = language;
end $$
delimiter ;

SHOW CHARACTER SET;

use StitchGalaxy;
drop table articles;




CREATE TABLE articles (
id INT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
title VARCHAR(200),
body TEXT,
FULLTEXT (title,body)
) CHARSET utf8 COLLATE utf8_general_ci ENGINE=MyISAM;


INSERT INTO articles (title,body) VALUES
('MySQL Tutorial','DBMS stands for DataBase ...'),
('How To Use MySQL Well','After you went through a ...'),
('Optimizing MySQL','In this tutorial we will show ...'),
('1001 MySQL Tricks','1. Never run mysqld as root. 2. ...'),
('MySQL vs. YourSQL','In the following database comparison ...'),
('MySQL Security','When configured properly, MySQL ...'),
('Евгений Тарасов', 'ну-ка попробуем');

INSERT INTO articles (title,body) VALUES
('попробуем','попробуем попробуем');

INSERT INTO articles (title,body) VALUES
('Семен','Семен клевый мужик. Но иногда попадает под дождь.');

INSERT INTO articles (title,body) VALUES
('Дождь','Дождь - это природное явления. Во время дождя бывает холодно.');

SELECT *, MATCH (title,body) AGAINST ('дождь') as relevance FROM articles
WHERE MATCH (title,body) AGAINST ('дождь') limit 0,10;























create table articles (
    id INT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
    title VARCHAR(200),
    body TEXT,
    FULLTEXT (title,body)
    ) 
CHARSET utf8 COLLATE utf8_general_ci ENGINE=MyISAM;

insert into articles (title, body) values
('Denis Yesakov','Black Jack and sluts'),
('Евгений Тарасов','Свобода выбора');

select * from articles
where match (title, body) against ('Тарасов');