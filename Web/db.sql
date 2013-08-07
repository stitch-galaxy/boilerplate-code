drop database StitchGalaxy;

create database StitchGalaxy;

use StitchGalaxy;

create table T_DESIGNS
(
DesignUuid varbinary(16),
ReleaseDate datetime default now() not null,
Sales bigint default 0,
TotalRating bigint default 0,
TotalRates bigint default 0,
Blocked bit null,
primary key (DesignUuid)
);

create table T_DESIGN_PARAMETERS
(
ParametersId bigint auto_increment,
DesignUuid varbinary(16),
Language varchar(10) not null,
Name nvarchar(255) not null,
Description nvarchar(255) null,
Width int null,
Heigth int null,
Colors int null,
HasThumbnail bit not null,
HasImage bit not null,
HasWebDescription bit not null,
HasDesign bit not null,
primary key (ParametersId)
);

alter table T_DESIGN_PARAMETERS
	add foreign key (DesignUuid)
		references T_DESIGNS (DesignUuid);

create table T_DESIGN_TAGS
(
	DesignUuid varbinary(16),
	TagId int
);

alter table T_DESIGN_TAGS
	add foreign key (DesignUuid)
		references T_DESIGNS (DesignUuid);

use stitchgalaxy;


DELIMITER $$
 
CREATE FUNCTION uuid_from_bin(b BINARY(16))
RETURNS CHAR(36) DETERMINISTIC
BEGIN
DECLARE hex CHAR(32);
SET hex = HEX(b);
RETURN CONCAT(LEFT(hex, 8), '-', MID(hex, 9,4), '-', MID(hex, 13,4), '-', MID(hex, 17,4), '-', RIGHT(hex, 12));
END
$$
 
CREATE FUNCTION uuid_to_bin(s CHAR(36))
RETURNS BINARY(16) DETERMINISTIC
RETURN UNHEX(CONCAT(LEFT(s, 8), MID(s, 10, 4), MID(s, 15, 4), MID(s, 20, 4), RIGHT(s, 12)))
$$

DELIMITER ;

delimiter $$
create procedure createOrUpdateDesignInformation
(
in uuid char(36),
in releaseDate datetime,
in sales bigint,
in totalRating bigint,
in totalRates bigint,
in blocked bit
)
begin

	declare designUuid varbinary(16);

	declare curReleaseDate datetime;
	declare curSales bigint;
	declare curTotalRating bigint;
	declare curTotalRates bigint;
	declare curBlocked bit;

	declare recordsFound int default 0;

	set designUuid = uuid_to_bin(uuid);
	
	select count(*) into recordsFound from T_DESIGNS where DesignUuid = designUuid;
	
	if recordsFound = 1
	then

		select ReleaseDate, Sales, TotalRating, TotalRates, Blocked into curReleasedate, curSales, curTotalRating, curTotalRates, curBlocked from T_DESIGNS where DesignUuid = designUuid;

		if releaseDate is null
		then
			set releaseDate = curReleaseDate;
		end if;

		if sales is null
		then
			set sales = curSales;
		end if;

		if totalRating is null
		then
			set totalRating = curTotalRating;
		end if;

		if totalRates is null
		then
			set totalRates = curTotalRates;
		end if;

		if blocked is null
		then
			set blocked = curBlocked;
		end if;
		
		update T_DESIGNS 
		set 
			ReleaseDate = releaseDate,
			Sales = sales,
			TotalRating = totalRating,
			TotalRates = totalRates,
			Blocked = totalBlocked
		where
			DesignUuid = designUuid;
	else
		insert into T_DESIGNS(DesignUuid, ReleaseDate, Sales, TotalRating, TotalRates, Blocked)
		values (designUuid, releaseDate, sales, totalRating, totalRates, blocked);

	end if;

end$$
delimiter ;

drop procedure createOrUpdateDesignLocalization;

delimiter $$
create procedure createOrUpdateDesignLocalization
(
in uuid char(36),
in language varchar(10),
in name varchar(255),
in description varchar(255),

in width int,
in height int,
in colors int,

in hasThumbnail bit,
in hasImage bit,
in hasWebDescription bit,
in hasDesign bit
)
begin

	declare designUuid varbinary(16);
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