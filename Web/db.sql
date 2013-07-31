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

delimiter $$
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

delimiter $$
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
