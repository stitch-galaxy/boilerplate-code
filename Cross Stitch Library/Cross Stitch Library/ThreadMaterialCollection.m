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
      threadMaterials = [[NSMutableSet alloc] initWithCapacity:aCapacity];
      threadMeterialsWithIndices = [[NSMutableDictionary alloc] initWithCapacity:aCapacity];
      maxIndex = 0;
  }
    return self;
}

- (ThreadMaterial *) GetThreadMaterialByColor: (UIColor *) color
{
    ThreadMaterial* threadMaterial = [[ThreadMaterial alloc] initWithColor:color];
    
    
    if (![threadMaterials containsObject:threadMaterial])
    {
        [threadMaterials addObject:threadMaterial];
        [threadMeterialsWithIndices setObject: [NSNumber numberWithInteger:maxIndex] forKey: threadMaterial];
        return threadMaterial;
    }
    
    for (ThreadMaterial* aThreadMaterial in threadMaterials)
    {
        if ([threadMaterial isEqual: aThreadMaterial])
        {
            return aThreadMaterial;
        }
    }
    return nil;
}

@end

@implementation ThreadMaterialCollection (Serialization)

- (uint32_t) GetThreadMaterialIndex: (ThreadMaterial *) aThreadMaterial
{
    NSNumber *number = [threadMeterialsWithIndices objectForKey:aThreadMaterial];
    if (number)
    {
        return number.integerValue;
    }
}

- (ThreadMaterial *) GetThreadMaterialByIndex: (uint32_t) anIndex;

- (size_t) GetSerializedLength;
- (void) SerializeToBuffer: (void*) buffer;
+ (id) DeserializeFromBuffer: (void*) buffer;

@end
