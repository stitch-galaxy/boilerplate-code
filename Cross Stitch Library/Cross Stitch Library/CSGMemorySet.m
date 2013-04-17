//
//  CSGMemeorySet.m
//  Cross Stitch Library
//
//  Created by 123 on 17.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGMemorySet.h"

@implementation CSGMemorySet

@synthesize objectsSet;

- (id) init
{
    if (self = [super init])
    {
        //TODO: capacity
        uint32_t aCapacity = 1;
        objectsSet = [[NSHashTable alloc] initWithOptions:NSPointerFunctionsStrongMemory capacity:aCapacity];
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
}

@end

