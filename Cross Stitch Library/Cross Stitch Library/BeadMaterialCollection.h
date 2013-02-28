//
//  BeadMaterialCollection.h
//  Cross Stitch Library
//
//  Created by 123 on 27.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <stdint.h>

#import "BeadMaterial.h"

@interface BeadMaterialCollection : NSObject
{
    NSHashTable* beadMaterials;
    NSMapTable* beadMaterialsToIndexMap;
    NSMapTable* indexToBeadMaterialMap;
    uint32_t freeIndex;
}

- (BeadMaterial *) GetBeadMaterialByColor: (UIColor *) color AndSize: (NSDecimalNumber*) aSize;

- (id) initWithCapacity: (NSUInteger) capacity;

@end

@interface BeadMaterialCollection (Serialization)

- (uint32_t) GetBeadMaterialIndex: (BeadMaterial *) aBeadMaterial;
- (BeadMaterial *) GetBeadMaterialByIndex: (uint32_t) anIndex;

- (size_t) GetSerializedLength;
- (void) SerializeToBuffer: (void*) buffer;
+ (id) DeserializeFromBuffer: (void*) buffer;

@end
