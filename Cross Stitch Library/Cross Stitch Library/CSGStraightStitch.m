//
//  CSGStraightStitch.m
//  Cross Stitch Library
//
//  Created by 123 on 06.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGStraightStitch.h"

@implementation CSGStraightStitch

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
    
    return [self isEqualToCSGStraightStitch: object];
}

- (BOOL) isEqualToCSGStraightStitch: (CSGStraightStitch*) aStraightStitch
{
    if (self == aStraightStitch)
    {
        return YES;
    }
    if (![threadBlend isEqual:aStraightStitch.threadBlend] || ![curve isEqual:aStraightStitch.curve])
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

@implementation CSGStraightStitch (Serialization)

- (size_t) serializedLength
{
    return threadBlend.serializedLength + curve.serializedLength;
}

- (void) serializeWithBinaryEncoder: (CSGBinaryEncoder *) anEncoder
{
    [threadBlend serializeWithBinaryEncoder:anEncoder];
    [curve serializeWithBinaryEncoder:anEncoder];
}

+ (id) deserializeWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder ObjectsRegistry: (CSGObjectsRegistry*) registry;
{
    CSGThreadsBlend *aThreadsBlend = [CSGThreadsBlend deserializeWithBinaryDecoder:anDecoder ObjectsRegistry: registry];
    CSGDesignPoints *aCurve = [CSGDesignPoints deserializeWithBinaryDecoder:anDecoder ObjectsRegistry: registry];
    
    CSGStraightStitch *aStitch =  [[CSGStraightStitch alloc] initWithThreadsBlend:aThreadsBlend Curve:aCurve];
    return [registry getStraightStitch: aStitch];
}

@end