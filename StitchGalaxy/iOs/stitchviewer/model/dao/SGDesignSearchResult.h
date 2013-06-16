//
//  SGDesignSearchResult.h
//  stitchviewer
//
//  Created by 123 on 16.06.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SGDesignSearchResult : NSObject

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

- (void) loadJSON: (id) JSON;

@end
