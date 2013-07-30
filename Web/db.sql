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
create procedure createOrUpdateDesignInformation
(
in uuid char(16),
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

	set designUuid = hex(uuid);
	
	select count(*) into recordsFound from T_DESIGNS where DesignUuid = designUuid;
	
	if recordsFound = 1
	then
			
		select ReleaseDate, Sales, TotalRating, TotalRates, Blocked into curReleasedate, curSales, cutTotalRating, curTotalRates, curBlocked from T_DESIGNS where DesignUuid = designUuid;

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

DELIMITER $$
create procedure createOrUpdateDesignLocalization
(
in uuid char(16),
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
	declare parametersId int default null;

	set designUuid = hex(uuid);

	select ParametersId into parametersId from T_DESIGN_PARAMETERS where DesignUuid = designUuid and Language = language;
	
	if parametersId is null
	then
		insert into T_DESIGN_PARAMETERS(DesignUuid, Language, Name, Description, Width, Heigth, Colors, HasThumbnail, HasImage, HasWebDescription, HasDesign) 
		values(designUuid, language, name, description, width, heigth, colors, hasThumbnail, hasImage, hasWebDescription, hasDesign);
	else
		update T_DESIGN_PARAMETERS set DesignUuid = designUuid, Language = language, Name = name, Description = description, Width = width, Heigth = heigth, Colors = colors, HasThumbnail = hasThumbnail, HasImage = hasImage, HasWebDescription = hasWebDescription, HasDesign = hasDesign 
		where ParametersId = parametersId;
	end if;

end$$

DELIMITER $$
create procedure searchDesigns
(
in searchString nvarchar(255),
in sortOrder int
)
begin

#TOP_SALE = 1
#MOST_RECENT = 2
#TOP_RATE = 3
#MOST_RELEVANT = 4
#RANDOM = 5
#PRICE_ASC = 6
#PRICE_DESC = 7
#COMPLEXITY_ASC = 8
#COMPLEXITY_DESC = 9

	declare designUuid varbinary(16);
	declare parametersId int default null;

	set designUuid = hex(uuid);

	select ParametersId into parametersId from T_DESIGN_PARAMETERS where DesignUuid = designUuid and Language = language;
	
	if parametersId is null
	then
		insert into T_DESIGN_PARAMETERS(DesignUuid, Language, Name, Description, Width, Heigth, Colors, HasThumbnail, HasImage, HasWebDescription, HasDesign) 
		values(designUuid, language, name, description, width, heigth, colors, hasThumbnail, hasImage, hasWebDescription, hasDesign);
	else
		update T_DESIGN_PARAMETERS set DesignUuid = designUuid, Language = language, Name = name, Description = description, Width = width, Heigth = heigth, Colors = colors, HasThumbnail = hasThumbnail, HasImage = hasImage, HasWebDescription = hasWebDescription, HasDesign = hasDesign 
		where ParametersId = parametersId;
	end if;

end$$
