//
//  ArtWork.h
//  Cross Stitch Library
//
//  Created by 123 on 18.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <stdint.h>

#import "Stitch.h"
#import "ArtWorkMaterials.h"

@protocol IArtWork <NSObject>

@property (copy, readonly) NSDate *Date;
@property (readonly) int32_t Rating;
@property (readonly) NSDecimal Price;
@property (readonly) int32_t Complexity;

@property (strong) NSSet<IThread> *Threads;
@property (strong) NSSet<IBead> *Beads;

- (NSString*) GetSmallPictureUrl;
- (NSString*) GetLargePictureUrl;

-(NSArray<IStitch>*) GetPicture;

@property (readonly) int32_t Width;
@property (readonly) int32_t Height;

-(id<IArtWorkMaterials>) GetMaterials;

@end
