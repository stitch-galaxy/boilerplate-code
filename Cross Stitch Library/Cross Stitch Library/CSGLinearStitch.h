//
//  CSGLinearStitch.h
//  Cross Stitch Library
//
//  Created by 123 on 03.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "CSGThreadsBlend.h"

@protocol CSGLinearStitch <NSObject>

@property (nonatomic, retain) CSGThreadsBlend *threadsBlend;

@end

@interface CSGLinearStitch : NSObject<CSGLinearStitch>

- (id) initWithThreadsBlend: (CSGThreadsBlend *) aThreadsBlend;

@end

@interface CSGLinearStitch (Serialization)

- (size_t) serializedLength;
- (void) serializeWithBinaryEncoder: (CSGBinaryEncoder *) anEncoder ThreadsPalette: (CSGThreadsPalette*) palette;
- (id) initWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder ThreadsPalette: (CSGThreadsPalette*) palette;

@end
