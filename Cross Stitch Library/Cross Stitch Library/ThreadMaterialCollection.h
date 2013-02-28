//
//  ThreadMaterialCollection.h
//  Cross Stitch Library
//
//  Created by 123 on 27.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <stdint.h>

#import "ThreadMaterial.h"

@interface ThreadMaterialCollection : NSObject
{
    NSHashTable* threadMaterials;
    NSMapTable* threadMaterialsToIndexMap;
    NSMapTable* indexToThreadMaterialMap;
    uint32_t freeIndex;
}

- (ThreadMaterial *) GetThreadMaterialByColor: (UIColor *) color;

- (id) initWithCapacity: (NSUInteger) capacity;

@end

@interface ThreadMaterialCollection (Serialization)

- (uint32_t) GetThreadMaterialIndex: (ThreadMaterial *) aThreadMaterial;
- (ThreadMaterial *) GetThreadMaterialByIndex: (uint32_t) anIndex;

- (size_t) GetSerializedLength;
- (void) SerializeToBuffer: (void*) buffer;
+ (id) DeserializeFromBuffer: (void*) buffer;

@end
