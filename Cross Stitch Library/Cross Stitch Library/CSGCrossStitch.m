//
//  CSGCrossStitch.m
//  Cross Stitch Library
//
//  Created by 123 on 15.03.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGCrossStitch.h"

@implementation CSGCrossStitch

- (id) initWithThreadsBlend: (CSGThreadsBlend *) aThreadsBlend
{
    return self = [super initWithThreadsBlend: aThreadsBlend];
}

- (BOOL) isEqual: (id) object
{
    if (object == self)
    {
        return YES;
    }
    if (!object || ![object isKindOfClass:[self class]])
    {
        return NO;
    }
    
    return [self isEqualToCSGCrossStitch: object];
}

- (BOOL) isEqualToCSGCrossStitch: (CSGCrossStitch*) aStitch
{
    if (self == aStitch)
    {
        return YES;
    }
    if (![self.threadsBlend isEqual: aStitch.threadsBlend])
    {
        return NO;
    }
    
    return YES;
}

- (NSUInteger)hash
{
    NSUInteger hash = 17;
    hash = hash*31 + self.threadsBlend.hash;
    
    return hash;
}


@end
