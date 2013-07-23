drop database StitchGalaxy;

create database StitchGalaxy;

USE StitchGalaxy;

create table T_DESIGNS
(
DesignId VARBINARY(16),
ReleaseDate datetime default now() not null,
primary key (DesignId)
);

create table T_PARAMETERS
(
ParametersId bigint auto_increment,
Width int null,
Heigth int null,
Colors int null,
#Localized information can be duplicated in store
Name nvarchar(255) null,
#Localized information can be duplicated in store
Description nvarchar(255) null,
ImageSmall nvarchar(255) null,
ImageLargeName nvarchar(255) null,
DescriptionUrl nvarchar(255) null,
DownloadUrl nvarchar(255) null,
primary key (ParametersId)
);

create table T_DESIGN_LOCALIZATION
(
ID bigint auto_increment,
Language varchar(50),
Region varchar(50) null,
DesignId VARBINARY(16),
ParametersId bigint,
primary key (ID)
);

alter table T_DESIGN_LOCALIZATION
	add foreign key (DesignId)
		references T_DESIGNS (DesignId);

alter table T_DESIGN_LOCALIZATION
	add foreign key (ParametersId)
		references T_PARAMETERS (ParametersId);

create table T_DESIGN_COUNTERS
(
ID bigint,
DesignId VARBINARY(16),
Sales bigint,
AvgRating float,
TotalRates bigint,
primary key (ID)
);

alter table T_DESIGN_COUNTERS
	add foreign key (DesignId)
		references T_DESIGNS (DesignId);


use stitchgalaxy;

select d.DesignId, d.ReleaseDate, p.Width, p.Heigth, p.Colors, p.Name, p.Description, p.ImageSmall, p.ImageLargeName, p.DescriptionUrl, p.DownloadUrl, dc.Sales, dc.AvgRating
from T_DESIGNS d 
INNER JOIN T_DESIGN_LOCALIZATION dl on dl.DesignId = d.DesignId
INNER JOIN T_PARAMETERS p on p.ParametersId = dl.ParametersId
LEFT OUTER JOIN T_DESIGN_COUNTERS dc on d.DesignId = dc.DesignId
WHERE dl.Language = 'en';

DELIMITER $$
CREATE PROCEDURE updateDesignLocalization
(
IN guid char(16),
IN language varchar(3),
IN region varchar(3),
IN designFileName varchar(255),
IN descriptionFileName varchar(255),
IN imageSmallFileName varchar(255),
IN imageLargeFileName varchar(255),
IN designName varchar(255),
IN designDescription varchar(255),
IN designWidth int,
IN designHeight int,
IN designColors int
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

END
