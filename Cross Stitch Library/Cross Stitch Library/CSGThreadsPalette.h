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
#import "CSGBinaryCoding.h"

@interface CSGThreadsPalette : NSObject

- (CSGThread*) threadMaterialByColor: (UIColor *) color;
- (uint32_t) size;

- (id) initWithCapacity: (NSUInteger) capacity;

@end

@interface CSGThreadsPalette (Serialization)

- (uint32_t) threadIndex: (CSGThread *) aThreadMaterial;
- (CSGThread *) threadAtIndex: (uint32_t) anIndex;

- (size_t) serializedLength;
- (void) serializeWithBinaryEncoder: (CSGBinaryEncoder *) anEncoder ObjectsRegistry: (CSGObjectsRegistry*) registry;
- (id) initWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder ObjectRegistry: (CSGObjectsRegistry*) registry;

@end
