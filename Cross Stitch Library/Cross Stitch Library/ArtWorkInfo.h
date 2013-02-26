//
//  ArtWorkHolder.h
//  Cross Stitch Library
//
//  Created by 123 on 26.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol IArtWorkInfo <NSObject>

@property (copy, readonly) NSString* Name;
@property (copy, readonly) NSDate *Date;

@property (readonly) NSDecimal Price;

@property (readonly) int32_t Rating;
@property (readonly) int32_t Complexity;


@property (copy, readonly) NSUUID* Guid;

- (NSString*) GetSmallPictureUrl;
- (NSString*) GetLargePictureUrl;


@end



@interface ArtWorkInfo : NSObject

@end
