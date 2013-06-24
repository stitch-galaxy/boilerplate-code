CREATE TABLE T_DESIGNS 
(ID BIGINT,
Width INT,
Height INT,
ColorsNumber INT,
Price DECIMAL,
Currency VARCHAR(3),
)

CREATE TABLE T_DESIGN_LOCALIZED_PARAMETERS
(
ID BIGINT,
DesignId BIGINT,
Name NVARCHAR(255),


)


@property (nonatomic, assign, readwrite) NSString *designName;
//technical parameters
@property (nonatomic, assign, readwrite) uint32_t width;
@property (nonatomic, assign, readwrite) uint32_t height;
@property (nonatomic, assign, readwrite) uint32_t colorsNumber;
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