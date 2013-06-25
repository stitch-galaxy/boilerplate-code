create database stitchgalaxy

use stitchgalaxy

create table T_DESIGNS
(
DesignId BIGINT,
Width INT,
Heigth INT,
ColorsNumber INT,
ReleaseDate DateTime
)

create table T_DESIGN_PARAMETERS
(
ParametersId BIGINT,
#Localized information can be duplicated in store
[Name] NVARCHAR(255),
#Localized information can be duplicated in store
Description NVARCHAR(255),
ImageSmallName VARCHAR(255),
ImageLargeName VARCHAR(255),
DescriptionUrl VARCHAR(255),
DownloadUrl VARCHAR(255)
)

create table T_DESIGN_LOCALIZED_PARAMETERS
(
ID BIGINT,
[Language] VARCHAR(50),
[Region] VARCHAR(50),
DesignId BIGINT,
ParametersId BIGINT
)

alter table T_DESIGN_LOCALIZED_PARAMETERS
	add constraint FK_DESIGNLOCALIZEDPARAMETERS foreign key (DesignId)
		references dbo.T_DESIGNS (DesignId)


create table T_DESIGN_COUNTERS
(
DesignId BIGINT,
Sales BIGINT,
Rating DOUBLE,
)