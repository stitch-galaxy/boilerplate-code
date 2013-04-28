//
//  CSGObjectsSetInMemory.m
//  Cross Stitch Library
//
//  Created by 123 on 15.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGMemorySetWithIndex.h"

@interface CSGMemorySetWithIndex()
{
    uint32_t freeIndex;
}

@property (nonatomic, retain) NSHashTable* objectsSet;
@property (nonatomic, retain) NSMutableArray* objectsArray;
@property (nonatomic, retain) NSMapTable* objectToIndexMap;

@end

@implementation CSGMemorySetWithIndex

@synthesize objectsSet;
@synthesize objectsArray;
@synthesize objectToIndexMap;

- (id) init
{
    return [self initWithCapacity: 1];
}

- (id) initWithCapacity: (uint32_t) aCapacity
{
    if (self = [super init])
    {
        freeIndex = 0;
        objectsSet = [[NSHashTable alloc] initWithOptions:NSPointerFunctionsStrongMemory capacity:aCapacity];
        objectsArray = [[NSMutableArray alloc] initWithCapacity:aCapacity];
        objectToIndexMap = [[NSMapTable alloc] initWithKeyOptions:NSPointerFunctionsWeakMemory valueOptions:NSMapTableStrongMemory capacity:aCapacity];
    }
    return self;
}

- (NSArray*) objects
{
    return objectsArray;
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
