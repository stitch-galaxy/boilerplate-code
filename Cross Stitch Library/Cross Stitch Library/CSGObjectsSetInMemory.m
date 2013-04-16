//
//  CSGObjectsSetInMemory.m
//  Cross Stitch Library
//
//  Created by 123 on 15.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGObjectsSetInMemory.h"

@interface CSGObjectsSetInMemory()
{
    uint32_t freeIndex;
}

@end

@implementation CSGObjectsSetInMemory

@synthesize objectsSet;
@synthesize objectsArray;
@synthesize objectToIndexMap;

- (id) init
{
    if (self = [super init])
    {
        freeIndex = 0;
        //TODO: capacity
        uint32_t aCapacity = 1;
        objectsSet = [[NSHashTable alloc] initWithOptions:NSPointerFunctionsStrongMemory capacity:aCapacity];
        objectsArray = [[NSMutableArray alloc] initWithCapacity:aCapacity];
        objectToIndexMap = [[NSMapTable alloc] initWithKeyOptions:NSPointerFunctionsWeakMemory valueOptions:NSMapTableStrongMemory capacity:aCapacity];
    }
    return self;
}

- (id) member: (id) anObject
{
    return [objectsSet member:anObject];
}

- (void) putObject: (id) anObject
{
    [objectsSet addObject:anObject];
    [objectsArray addObject:anObject];
    [objectToIndexMap setObject: [NSNumber numberWithUnsignedInteger:freeIndex] forKey:anObject];
    ++freeIndex;
}


- (id) getObjectByIndex: (uint32_t) anIndex
{
    return [objectsArray objectAtIndex:anIndex];
}

-(uint32_t) getIndexByObject: (id) anObject
{
    NSNumber *anIndex = [objectToIndexMap objectForKey:anObject];
    return anIndex.unsignedIntegerValue;
}

@end
