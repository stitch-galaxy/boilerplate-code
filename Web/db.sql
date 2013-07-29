drop database StitchGalaxy;

create database StitchGalaxy;

USE StitchGalaxy;

create table T_DESIGNS
(
DesignId VARBINARY(16),
ReleaseDate datetime default now() not null,
Sales bigint default 0,
TotalRating bigint default 0,
TotalRates bigint default 0,
Blocked bit null,
primary key (DesignId)
);

create table T_DESIGN_PARAMETERS
(
ParametersId bigint auto_increment,
DesignId VARBINARY(16),
LanguageId varchar(10) not null,
Name nvarchar(255) not null,
Description nvarchar(255) null,
Width int null,
Heigth int null,
Colors int null,
HasSmallImage bit not null,
HasLargeImage bit not null,
HasWebDescription bit not null,
HasDesignFile bit not null,
primary key (ParametersId)
);

alter table T_DESIGN_PARAMETERS
	add foreign key (DesignId)
		references T_DESIGNS (DesignId);

use stitchgalaxy;

#select d.DesignId, d.ReleaseDate, p.Width, p.Heigth, p.Colors, p.Name, p.Description, p.ImageSmall, p.ImageLargeName, p.DescriptionUrl, p.DownloadUrl, dc.Sales, dc.AvgRating
#from T_DESIGNS d 
#INNER JOIN T_DESIGN_LOCALIZATION dl on dl.DesignId = d.DesignId
#INNER JOIN T_PARAMETERS p on p.ParametersId = dl.ParametersId
#LEFT OUTER JOIN T_DESIGN_COUNTERS dc on d.DesignId = dc.DesignId
#WHERE dl.Language = 'en';

DELIMITER $$
CREATE PROCEDURE createOrUpdateDesignInformation
(
IN guid char(16),
IN releaseDate datetime,
IN sales bigint,
IN totalRating bigint,
IN totalRates bigint
)
BEGIN

	DECLARE currReleaseDate datetime;
	DECLARE curSales bigint;
	DECLARE curTotalRating bigint;
	DECLARE curTotalRates bigint;
		
	select ReleaseDate, Sales, TotalRating, TotalRates into currReleaseDate,  curSales, curTotalRating, curTotalRates from T_DESIGNS where DesignId = hex(guid);

	if releaseDate is null
	then
		set releaseDate = currReleaseDate;
	end if;

	if sales is null
	then
		set sales = currSales;
	end if;

	if totalRating is null
	then
		set totalRating = curTotalRating;
	end if;

	if totalRates is null
	then
		set totalRates = curtTotalRates;
	end if;
	
END$$

DELIMITER $$
CREATE PROCEDURE updateDesignLocalization
(
IN guid char(16),
IN languageId varchar(10),
IN name varchar(255),
IN description varchar(255),

IN width int,
IN height int,
IN colors int,

IN hasDesignFile bit,
IN hasDescriptionFile bit,
IN hasSmallImage bit,
IN hasLargeImage bit
)
BEGIN

	DECLARE recordfound INT default 0;

	select count(*) into recordfound from T_DESIGNS where DesignId = hex(guid);
	
	if recordfound = 0
	then
		INSERT into T_DESIGNS(DesignId) values(hex(GUID));
	end if;

	select count(*) into recordfound from T_DESIGN_LOCALIZATION where Language = Language and Region = region and DesignId = hex(guid);

	if recordfound = 0
	then
		INSERT into T_DESIGNS(DesignId) values(hex(GUID));
	end if;

END$$
