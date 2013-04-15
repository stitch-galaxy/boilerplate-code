//
//  CSGObjectsSetInMemory.m
//  Cross Stitch Library
//
//  Created by 123 on 15.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGObjectsSetInMemory.h"

@implementation CSGObjectsSetInMemory

@synthesize objectsSet;

- (id) init
{
    if (self = [super init])
    {
        //TODO: capacity
        objectsSet = [[NSHashTable alloc] initWithOptions:NSPointerFunctionsStrongMemory capacity:1];
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
