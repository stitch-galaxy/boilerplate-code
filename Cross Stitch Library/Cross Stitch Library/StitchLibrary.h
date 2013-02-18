//
//  StitchLibrary.h
//  Cross Stitch Library
//
//  Created by 123 on 17.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "StitchCategory.h"

@protocol IStitchLibrary <NSObject>

- (NSArray<IStitchCategory>*) GetCategories;
- (NSArray<IArtWork>*) GetMyArtWorks;

@end

@interface StitchLibrary : NSObject<IStitchLibrary>

+ (id<IStitchLibrary>) Instance;

@end
