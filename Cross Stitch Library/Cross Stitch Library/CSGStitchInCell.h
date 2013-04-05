//
//  CSGCrossStitch.h
//  Cross Stitch Library
//
//  Created by 123 on 15.03.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//
#import <Foundation/Foundation.h>

#import "CSGThreadsBlend.h"

@protocol CSGStitchInCell <NSObject>

@property (nonatomic, retain) CSGThreadsBlend *threadsBlend;

@end

@interface CSGStitchInCell : NSObject<CSGStitchInCell>

- (id) initWithThreadsBlend: (CSGThreadsBlend *) aThreadsBlend;
 
@end


@interface CSGStitchInCell (Serialization)

- (size_t) serializedLength;
- (void) serializeWithBinaryEncoder: (CSGBinaryEncoder *) anEncoder ThreadsPalette: (CSGThreadsPalette*) palette;
- (id) initWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder ThreadsPalette: (CSGThreadsPalette*) palette;

@end