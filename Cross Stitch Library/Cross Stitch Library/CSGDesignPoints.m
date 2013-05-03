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
