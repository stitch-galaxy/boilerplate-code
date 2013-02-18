//
//  StitchCategory.h
//  Cross Stitch Library
//
//  Created by 123 on 18.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <stdint.h>

#import "ArtWork.h"

@protocol IStitchCategory <NSObject>

@property (nonatomic, copy, readonly) NSString *Name;
@property (nonatomic, copy, readonly) NSString *PictureUrl;
@property (nonatomic, readonly) int32_t Priority;

- (NSArray<IArtWork>*) GetArtWorks;

@end
