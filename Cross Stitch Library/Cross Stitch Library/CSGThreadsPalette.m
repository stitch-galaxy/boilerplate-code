//
//  ThreadMaterialCollection.m
//  Cross Stitch Library
//
//  Created by 123 on 27.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGThreadsPalette.h"

@interface CSGThreadsPalette ()
{
    NSHashTable* threadMaterials;
    NSMapTable* threadMaterialsToIndexMap;
    NSMapTable* indexToThreadMaterialMap;
    uint32_t freeIndex;
}

@end

@implementation CSGThreadsPalette

- (id) init
{
    if (self = [super init])
    {
        return [self initWithCapacity: 0];
    }
    return self;
}

- (id) initWithCapacity: (NSUInteger) aCapacity
{
  if (self = [super init])
  {
      threadMaterials = [[NSHashTable alloc] initWithOptions:NSPointerFunctionsStrongMemory capacity:aCapacity];
      threadMaterialsToIndexMap = [[NSMapTable alloc] initWithKeyOptions: NSMapTableWeakMemory  valueOptions: NSMapTableWeakMemory capacity:aCapacity];
      indexToThreadMaterialMap = [[NSMapTable alloc] initWithKeyOptions: NSMapTableWeakMemory  valueOptions: NSMapTableWeakMemory capacity:aCapacity];
      freeIndex = 0;
  }
    return self;
}

- (uint32_t) size
{
    return [threadMaterials count];
}

- (CSGThread *) threadMaterialByColor: (UIColor *) color
{
    CSGThread* aThreadMaterial = [[CSGThread alloc] initWithColor:color];
    if ([threadMaterials containsObject:aThreadMaterial])
    {
        for(CSGThread* aThMat in threadMaterials)
        {
            if ([aThMat isEqual:aThreadMaterial])
            {
                return aThMat;
            }
        }
    }
    [self AddThreadMaterial:aThreadMaterial WithIndex:freeIndex];
    
    return aThreadMaterial;
}

- (void) AddThreadMaterial: (CSGThread *) aThreadMaterial WithIndex: (uint32_t) index
{
    [threadMaterials addObject:aThreadMaterial];
    
    NSNumber* nIndex = [NSNumber numberWithUnsignedInteger:index];
    [threadMaterialsToIndexMap setObject: nIndex forKey:aThreadMaterial];
    [indexToThreadMaterialMap setObject:aThreadMaterial forKey:nIndex];
    
    if (index >= freeIndex)
    {
        freeIndex = (index + 1);
    }
}


@end

@implementation CSGThreadsPalette (Serialization)

- (uint32_t) threadIndex: (CSGThread *) aThreadMaterial
{
    NSNumber* nIndex = [threadMaterialsToIndexMap objectForKey:aThreadMaterial];
    if (nIndex)
    {
        return nIndex.unsignedIntegerValue;
    }
    [NSException raise:@"Can not find index by threadMaterial" format:nil];
    return 0;
}

- (CSGThread *) threadAtIndex: (uint32_t) anIndex
{
    NSNumber* nIndex = [NSNumber numberWithUnsignedInteger:anIndex];
    CSGThread* ret = [indexToThreadMaterialMap objectForKey:nIndex];
    if (ret)
    {
        return ret;
    }
    [NSException raise:@"Can not find threadMaterial by index" format:nil];
    return nil;
}

- (size_t) serializedLength
{
    size_t size = sizeof(uint32_t);
    for(CSGThread* aThMat in threadMaterials)
    {
        size += [aThMat serializedLength];
        size += sizeof(uint32_t);
    }
    return size;
}

- (void) serializeToBuffer: (void*) buffer
{
    uint32_t *buf = (uint32_t *) buffer;
    *buf = threadMaterials.count;
    ++buf;
    uint32_t *indexBuf = (uint32_t *)buf;
    for(CSGThread *tMat in threadMaterials)
    {
        *indexBuf = [self threadIndex: tMat];
        ++indexBuf;
        void *voidBuf = (void *)indexBuf;
        size_t size = [tMat serializedLength];
        [tMat serializeToBuffer:voidBuf];
        voidBuf += size;
        indexBuf = (uint32_t *)voidBuf;
    }
}

+ (id) deserializeFromBuffer: (const void*) buffer
{
    uint32_t *buf = (uint32_t *) buffer;
    uint32_t length = *buf;
    ++buf;
    CSGThreadsPalette* threads = [[CSGThreadsPalette alloc] initWithCapacity:length];
    for (uint32_t i = 0; i < length; ++i)
    {
        uint32_t index = *buf;
        ++buf;
        void *voidBuf = (void *) buf;
        CSGThread* tMat = [CSGThread deserializeFromBuffer:voidBuf];
        voidBuf += [tMat serializedLength];
        buf = (uint32_t *) voidBuf;
        
        [threads AddThreadMaterial:tMat WithIndex:index];
        
    }
    return threads;
}

@end
