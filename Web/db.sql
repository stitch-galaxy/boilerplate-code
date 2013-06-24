create database stitchgalaxy

create table T_DESIGNS
(ID BIGINT,
Width INT,
Heigth INT,
ColorsNumber INT,
)

CREATE TABLE T_DESIGN_LOCALIZED_PARAMETERS
(
ID BIGINT,
DesignId BIGINT,
[Locale] VARCHAR(50),
imageSmallName VARCHAR(255),
imageLargeName VARCHAR(255),
descriptionUrl VARCHAR(255),
downloadUrl VARCHAR(255)
)


@property (nonatomic, assign, readwrite) NSString *designName;
//images
@property (nonatomic, retain, readwrite) NSURL *imageSmallUrl;
@property (nonatomic, retain, readwrite) NSURL *imageLargeUrl;
//pricing
@property (nonatomic, assign, readwrite) NSDecimal price;
@property (nonatomic, assign, readwrite) uint32_t discountPercentage;
//social information
@property (nonatomic, retain, readwrite) NSDate *releaseDate;
@property (nonatomic, assign, readwrite) uint32_t rating;
@property (nonatomic, assign, readwrite) uint64_t downloads;
//details
@property (nonatomic, retain, readwrite) NSURL *descriptionUrl;
//url to download design
@property (nonatomic, retain, readwrite) NSURL *designDownloadUrl;