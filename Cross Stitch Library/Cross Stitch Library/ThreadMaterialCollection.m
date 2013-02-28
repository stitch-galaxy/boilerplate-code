//
//  ThreadMaterialCollection.m
//  Cross Stitch Library
//
//  Created by 123 on 27.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "ThreadMaterialCollection.h"

@implementation ThreadMaterialCollection

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

- (ThreadMaterial *) GetThreadMaterialByColor: (UIColor *) color
{
    ThreadMaterial* aThreadMaterial = [[ThreadMaterial alloc] initWithColor:color];
    if ([threadMaterials containsObject:aThreadMaterial])
    {
        for(ThreadMaterial* aThMat in threadMaterials)
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

- (void) AddThreadMaterial: (ThreadMaterial *) aThreadMaterial WithIndex: (uint32_t) index
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

@implementation ThreadMaterialCollection (Serialization)

- (uint32_t) GetThreadMaterialIndex: (ThreadMaterial *) aThreadMaterial
{
    NSNumber* nIndex = [threadMaterialsToIndexMap objectForKey:aThreadMaterial];
    if (nIndex)
    {
        return nIndex.unsignedIntegerValue;
    }
    [NSException raise:@"Can not find index by threadMaterial" format:nil];
    return 0;
}

- (ThreadMaterial *) GetThreadMaterialByIndex: (uint32_t) anIndex
{
    NSNumber* nIndex = [NSNumber numberWithUnsignedInteger:anIndex];
    ThreadMaterial* ret = [indexToThreadMaterialMap objectForKey:nIndex];
    if (ret)
    {
        return ret;
    }
    [NSException raise:@"Can not find threadMaterial by index" format:nil];
    return nil;
}

- (size_t) GetSerializedLength
{
    size_t size = sizeof(uint32_t);
    for(ThreadMaterial* aThMat in threadMaterials)
    {
        size += [aThMat GetSerializedLength];
        size += sizeof(uint32_t);
    }
    return size;
}

- (void) SerializeToBuffer: (void*) buffer
{
    uint32_t *buf = (uint32_t *) buffer;
    *buf = threadMaterials.count;
    ++buf;
    uint32_t *indexBuf = (uint32_t *)buf;
    for(ThreadMaterial *tMat in threadMaterials)
    {
        *indexBuf = [self GetThreadMaterialIndex: tMat];
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
    ThreadMaterialCollection* threads = [[ThreadMaterialCollection alloc] initWithCapacity:length];
    for (uint32_t i = 0; i < length; ++i)
    {
        uint32_t index = *buf;
        ++buf;
        void *voidBuf = (void *) buf;
        ThreadMaterial* tMat = [ThreadMaterial DeserializeFromBuffer:voidBuf];
        voidBuf += [tMat GetSerializedLength];
        buf = (uint32_t *) voidBuf;
        
        [threads AddThreadMaterial:tMat WithIndex:index];
        
    }
    return threads;
}

@end
