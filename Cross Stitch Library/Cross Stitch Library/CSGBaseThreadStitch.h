//
//  CSGBaseThreadStitch.h
//  Cross Stitch Library
//
//  Created by 123 on 14.03.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "CSGThreadsBlend.h"

@protocol CSGBaseThreadStitch <NSObject>

@property (nonatomic, retain) CSGThreadsBlend *threadsBlend;

@end

@interface CSGBaseThreadStitch : NSObject<CSGBaseThreadStitch>

- (id) initWithThreadsBlend: (CSGThreadsBlend *) aThreadsBlend;

@end


@interface CSGBaseThreadStitch (Serialization)

- (size_t) serializedLength;
- (void) serializeToBuffer: (void*) buffer WithThreadsPalette: (CSGThreadsPalette*) palette;
+ (id) deserializeFromBuffer: (const void*) buffer WithThreadsPalette: (CSGThreadsPalette*) palette;

@end