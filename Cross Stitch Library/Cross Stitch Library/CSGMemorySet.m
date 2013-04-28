//
//  CSGMemeorySet.m
//  Cross Stitch Library
//
//  Created by 123 on 17.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGMemorySet.h"

@interface CSGMemorySet()

@property (nonatomic, retain) NSHashTable* objectsSet;

@end

@implementation CSGMemorySet

@synthesize objectsSet;

- (id) init
{
    return [self initWithCapacity:1];
}

-(id) initWithCapacity: (uint32_t) aCapacity
{
    if (self = [super init])
    {
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

- (uint32_t) count
{
    return [objectsSet count];
}

@end

