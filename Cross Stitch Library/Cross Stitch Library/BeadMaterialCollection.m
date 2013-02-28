//
//  BeadMaterialCollection.m
//  Cross Stitch Library
//
//  Created by 123 on 27.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "BeadMaterialCollection.h"

@implementation BeadMaterialCollection

- (id) initWithCapacity: (NSUInteger) aCapacity
{
    if (self = [super init])
    {
        beadMaterials = [[NSHashTable alloc] initWithOptions:NSPointerFunctionsStrongMemory capacity:aCapacity];
        beadMaterialsToIndexMap = [[NSMapTable alloc] initWithKeyOptions: NSMapTableWeakMemory  valueOptions: NSMapTableWeakMemory capacity:aCapacity];
        indexToBeadMaterialMap = [[NSMapTable alloc] initWithKeyOptions: NSMapTableWeakMemory  valueOptions: NSMapTableWeakMemory capacity:aCapacity];
        freeIndex = 0;
    }
    return self;
}

- (BeadMaterial *) GetBeadMaterialByColor: (UIColor *) color AndSize: (NSDecimalNumber*) aSize
{
    BeadMaterial* aBeadMaterial = [[BeadMaterial alloc] initWithColor:color AndSize:aSize];
    if ([beadMaterials containsObject:aBeadMaterial])
    {
        for(BeadMaterial* aThMat in beadMaterials)
        {
            if ([aThMat isEqual:aBeadMaterial])
            {
                return aThMat;
            }
        }
    }
    [self AddBeadMaterial:aBeadMaterial WithIndex:freeIndex];
    
    return aBeadMaterial;
}

- (void) AddBeadMaterial: (BeadMaterial *) aBeadMaterial WithIndex: (uint32_t) index
{
    [beadMaterials addObject:aBeadMaterial];
    
    NSNumber* nIndex = [NSNumber numberWithUnsignedInteger:index];
    [beadMaterialsToIndexMap setObject: nIndex forKey:aBeadMaterial];
    [indexToBeadMaterialMap setObject:aBeadMaterial forKey:nIndex];
    
    if (index >= freeIndex)
    {
        freeIndex = (index + 1);
    }
}


@end

@implementation BeadMaterialCollection (Serialization)

- (uint32_t) GetBeadMaterialIndex: (BeadMaterial *) aBeadMaterial
{
    NSNumber* nIndex = [beadMaterialsToIndexMap objectForKey:aBeadMaterial];
    if (nIndex)
    {
        return nIndex.unsignedIntegerValue;
    }
    [NSException raise:@"Can not find index by beadMaterial" format:nil];
    return 0;
}

- (BeadMaterial *) GetBeadMaterialByIndex: (uint32_t) anIndex
{
    NSNumber* nIndex = [NSNumber numberWithUnsignedInteger:anIndex];
    BeadMaterial* ret = [indexToBeadMaterialMap objectForKey:nIndex];
    if (ret)
    {
        return ret;
    }
    [NSException raise:@"Can not find beadMaterial by index" format:nil];
    return nil;
}

- (size_t) GetSerializedLength
{
    size_t size = sizeof(uint32_t);
    for(BeadMaterial* aThMat in beadMaterials)
    {
        size += [aThMat GetSerializedLength];
        size += sizeof(uint32_t);
    }
    return size;
}

- (void) SerializeToBuffer: (void*) buffer
{
    uint32_t *buf = (uint32_t *) buffer;
    *buf = beadMaterials.count;
    ++buf;
    uint32_t *indexBuf = (uint32_t *)buf;
    for(BeadMaterial *tMat in beadMaterials)
    {
        *indexBuf = [self GetBeadMaterialIndex: tMat];
        ++indexBuf;
        void *voidBuf = (void *)indexBuf;
        size_t size = [tMat GetSerializedLength];
        [tMat SerializeToBuffer:voidBuf];
        voidBuf += size;
        indexBuf = (uint32_t *)voidBuf;
    }
}

+ (id) DeserializeFromBuffer: (void*) buffer
{
    uint32_t *buf = (uint32_t *) buffer;
    uint32_t length = *buf;
    ++buf;
    BeadMaterialCollection* beads = [[BeadMaterialCollection alloc] initWithCapacity:length];
    for (uint32_t i = 0; i < length; ++i)
    {
        uint32_t index = *buf;
        ++buf;
        void *voidBuf = (void *) buf;
        BeadMaterial* tMat = [BeadMaterial DeserializeFromBuffer:voidBuf];
        voidBuf += [tMat GetSerializedLength];
        buf = (uint32_t *) voidBuf;
        
        [beads AddBeadMaterial:tMat WithIndex:index];
        
    }
    return beads;
}

@end
