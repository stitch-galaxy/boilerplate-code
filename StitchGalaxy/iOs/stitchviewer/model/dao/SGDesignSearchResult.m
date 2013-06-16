//
//  SGDesignSearchResult.m
//  stitchviewer
//
//  Created by 123 on 16.06.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "SGDesignSearchResult.h"

static NSString * const SG_JSON_SEARCH_ITEM_DESIGN_NAME = @"designName";
static NSString * const SG_JSON_SEARCH_ITEM_WIDTH = @"width";
static NSString * const SG_JSON_SEARCH_ITEM_HEIGHT = @"height";
static NSString * const SG_JSON_SEARCH_ITEM_COLORS_NUM = @"colorsNumber";
static NSString * const SG_JSON_SEARCH_ITEM_IMAGE_SMALL_URL = @"imageSmallUrl";
static NSString * const SG_JSON_SEARCH_ITEM_IMAGE_LARGE_URL = @"imageLargeUrl";
static NSString * const SG_JSON_SEARCH_ITEM_PRICE = @"price";
static NSString * const SG_JSON_SEARCH_ITEM_DICSOUNT_PERCENTAGE = @"discountPercents";
static NSString * const SG_JSON_SEARCH_ITEM_RELEASE_DATE = @"releaseDate";
static NSString * const SG_JSON_SEARCH_ITEM_RATING = @"rating";
static NSString * const SG_JSON_SEARCH_ITEM_DOWNLOADS = @"downloads";
static NSString * const SG_JSON_SEARCH_ITEM_DESCRIPTION_URL = @"descriptionUrl";
static NSString * const SG_JSON_SEARCH_ITEM_DESIGN_DOWNLOAD_URL = @"designDownloadUrl";


@implementation SGDesignSearchResult

@synthesize designName;
//technical parameters
@synthesize width;
@synthesize height;
@synthesize colorsNumber;
//images
@synthesize imageSmallUrl;
@synthesize imageLargeUrl;
//pricing
@synthesize price;
@synthesize discountPercentage;
//social information
@synthesize releaseDate;
@synthesize rating;
@synthesize downloads;
//details
@synthesize descriptionUrl;
//url to download design
@synthesize designDownloadUrl;

- (id) init
{
    if (self = [super init])
    {
        designName = nil;
        width = 0;
        height = 0;
        colorsNumber = 0;
        imageSmallUrl = nil;
        imageLargeUrl = nil;
        NSDecimalNumber *aPrice = [[NSDecimalNumber alloc] initWithInt:0];
        price = [aPrice decimalValue];
        discountPercentage = 0;
        static NSDate *defaultReleaseDate = nil;
        if (nil == defaultReleaseDate)
        {
            NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
            [dateFormat setDateFormat:@"yyyy-MM-dd"];
            defaultReleaseDate = [dateFormat dateFromString:@"2013-01-01"];
        }
        releaseDate = defaultReleaseDate;
        rating = 0;
        downloads = 0;
        descriptionUrl = nil;
        designDownloadUrl = nil;
    }
    return self;
}

- (void) loadJSON: (id) JSON
{
    NSDictionary *dict = JSON;
    NSString *aDesignName = [dict objectForKey:SG_JSON_SEARCH_ITEM_DESIGN_NAME];
    if (aDesignName)
    {
        designName = aDesignName;
    }
    NSNumber *aWidth = [dict objectForKey:SG_JSON_SEARCH_ITEM_WIDTH];
    if (aWidth)
    {
        width = aWidth.unsignedIntegerValue;
    }
    NSNumber *aHeight = [dict objectForKey:SG_JSON_SEARCH_ITEM_HEIGHT];
    if (aHeight)
    {
        height = aHeight.unsignedIntegerValue;
    }
    NSNumber *aColorsNumber = [dict objectForKey:SG_JSON_SEARCH_ITEM_COLORS_NUM];
    if (aColorsNumber)
    {
        colorsNumber = aColorsNumber.unsignedIntegerValue;
    }
    NSString *aImageSmallUrl = [dict objectForKey:SG_JSON_SEARCH_ITEM_IMAGE_SMALL_URL];
    if (aImageSmallUrl)
    {
        imageSmallUrl = [[NSURL alloc] initWithString:aImageSmallUrl];
    }
    NSString *aImageLargeUrl = [dict objectForKey:SG_JSON_SEARCH_ITEM_IMAGE_LARGE_URL];
    if (aImageLargeUrl)
    {
        imageLargeUrl = [[NSURL alloc] initWithString:aImageLargeUrl];
    }
    NSNumber *aPrice = [dict objectForKey:SG_JSON_SEARCH_ITEM_PRICE];
    if (aPrice)
    {
        price = aPrice.decimalValue;
    }
    NSNumber *aDiscountPercentage = [dict objectForKey:SG_JSON_SEARCH_ITEM_DICSOUNT_PERCENTAGE];
    if (aDiscountPercentage)
    {
        discountPercentage = aDiscountPercentage.unsignedIntegerValue;
    }
    NSString *aReleaseDate = [dict objectForKey:SG_JSON_SEARCH_ITEM_RELEASE_DATE];
    if (aReleaseDate)
    {
        static NSDateFormatter *dateFormat = nil;
        if (nil == dateFormat)
        {
            dateFormat = [[NSDateFormatter alloc] init];
            [dateFormat setDateFormat:@"yyyy-MM-dd"];
        }
        releaseDate = [dateFormat dateFromString:aReleaseDate];
    }
    NSNumber *aRating = [dict objectForKey:SG_JSON_SEARCH_ITEM_RATING];
    if (aRating)
    {
        rating = aRating.unsignedIntegerValue;
    }
    NSNumber *aDownloads = [dict objectForKey:SG_JSON_SEARCH_ITEM_DOWNLOADS];
    if (aDownloads)
    {
        downloads = aDownloads.unsignedIntegerValue;
    }
    NSString *aDescriptionUrl = [dict objectForKey:SG_JSON_SEARCH_ITEM_DESCRIPTION_URL];
    if (aDescriptionUrl)
    {
        descriptionUrl = [[NSURL alloc] initWithString:aDescriptionUrl];
    }
    NSString *aDesignDownloadUrl = [dict objectForKey:SG_JSON_SEARCH_ITEM_DESIGN_DOWNLOAD_URL];
    if (aDesignDownloadUrl)
    {
        designDownloadUrl = [[NSURL alloc] initWithString:aDesignDownloadUrl];
    }
}

@end
