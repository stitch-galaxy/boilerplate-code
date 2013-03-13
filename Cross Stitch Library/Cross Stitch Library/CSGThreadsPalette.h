//
//  ThreadMaterialCollection.h
//  Cross Stitch Library
//
//  Created by 123 on 27.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <stdint.h>

#import "CSGThread.h"

@interface CSGThreadsPalette : NSObject

- (CSGThread*) threadMaterialByColor: (UIColor *) color;
- (uint32_t) size;

- (id) initWithCapacity: (NSUInteger) capacity;

@end

@interface CSGThreadsPalette (Serialization)

- (uint32_t) threadIndex: (CSGThread *) aThreadMaterial;
- (CSGThread *) threadByIndex: (uint32_t) anIndex;

- (size_t) serializedLength;
- (void) serializeToBuffer: (void*) buffer;
+ (id) deserializeFromBuffer: (const void*) buffer;

@end
