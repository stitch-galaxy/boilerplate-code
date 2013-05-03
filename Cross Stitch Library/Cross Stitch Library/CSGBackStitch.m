//
//  CSGBackStitch.m
//  Cross Stitch Library
//
//  Created by 123 on 06.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGBackStitch.h"

@implementation CSGBackStitch

@synthesize threadBlend;

@synthesize curve;

- (id) init
{
    NSAssert( false, @"Please use designated initializer" );
    
    return nil;
}

-(id) initWithThreadsBlend: (CSGThreadsBlend*) aThreadBlend Curve:(CSGDesignPoints*) aCurve
{
    if (self = [super init])
    {
        threadBlend = aThreadBlend;
        curve = aCurve;
    }
    return self;
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
    
    return [self isEqualToCSGBackStitch: object];
}

- (BOOL) isEqualToCSGBackStitch: (CSGBackStitch*) aBackStitch
{
    if (self == aBackStitch)
    {
        return YES;
    }
    if (![threadBlend isEqual:aBackStitch.threadBlend] || ![curve isEqual:aBackStitch.curve])
    {
        return NO;
    }
    
    
    return YES;
}

- (NSUInteger)hash
{
    NSUInteger hash = 17;
    hash = hash*31 + threadBlend.hash;
    hash = hash*31 + curve.hash;
    
    return hash;
}

@end