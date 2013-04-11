//
//  CSGLinearStitch.m
//  Cross Stitch Library
//
//  Created by 123 on 03.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGDesignPoints.h"

@implementation CSGDesignPoints

@synthesize points;

- (id) init
{
    NSAssert( false, @"Please use designated initializer" );
    
    return nil;
}


- (id) initWithPoints: (NSArray*) aPoints
{
    if (self = [super init])
    {
        points = aPoints;
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
    
    return [self isEqualToCSGDesignPoints: object];
}

- (BOOL) isEqualToCSGDesignPoints: (CSGDesignPoints*) aPoints
{
    if (self == aPoints)
    {
        return YES;
    }
    if (points.count != aPoints.points.count)
    {
        return NO;
    }
    int32_t i = 0;
    for(CSGDesignPoint* point in points)
    {
        CSGDesignPoint* aPoint = [aPoints.points objectAtIndex: i];
        if (![point isEqual:aPoint])
        {
            return NO;
        }
        ++i;
    }
    
    return YES;
}

- (NSUInteger)hash
{
    NSUInteger hash = 17;
    for(CSGDesignPoint* point in points)
    {
        hash = hash*31 + point.hash;
    }
    
    return hash;
}

@end

@implementation CSGDesignPoints (Serialization)

- (size_t) serializedLength
{
    size_t size = sizeof(uint32_t);
    for(CSGDesignPoint *coord in points)
    {
        size += coord.serializedLength;
    }
    return size;
}

- (void) serializeWithBinaryEncoder: (CSGBinaryEncoder *) anEncoder
{
    uint32_t* buf = [anEncoder modifyBytes:sizeof(uint32_t)];
    *buf = points.count;
    for(CSGDesignPoint *coord in points)
    {
        [coord serializeWithBinaryEncoder:anEncoder];
    }
}

+ (id) deserializeWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder ObjectsRegistry: (CSGObjectsRegistry*) registry;
{
    NSMutableArray *aPoints = [[NSMutableArray alloc] init];
    const uint32_t *buf = [anDecoder readBytes:sizeof(uint32_t)];
    uint32_t numPoints = *buf;
    for(uint32_t i = 0; i < numPoints; ++i)
    {
        CSGDesignPoint *coord = [CSGDesignPoint deserializeWithBinaryDecoder: anDecoder ObjectsRegistry: registry];
        [aPoints addObject:coord];
    }
    
    CSGDesignPoints* aPoints =  [[CSGDesignPoints alloc] initWithPoints:aPoints];
    return [registry getDesignPoints: aPoints];
}


@end
