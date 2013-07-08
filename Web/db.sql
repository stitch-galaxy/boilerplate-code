/*
SELECT table_name
FROM information_schema.tables
WHERE table_schema = 'stitchgalaxy'
AND table_name = 'T_DESIGNS';
*/
drop database StitchGalaxy;

create database StitchGalaxy;

USE StitchGalaxy;

create table T_DESIGNS
(
DesignId bigint auto_increment,
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
DesignId bigint,
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
DesignId bigint,
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
WHERE dl.Language = 'en'
