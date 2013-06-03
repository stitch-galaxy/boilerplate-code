//
//  CSGNeedlePerforationCoordinate.m
//  Cross Stitch Library
//
//  Created by 123 on 03.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGDesignPoint.h"

@interface CSGDesignPoint ()
{
    uint32_t x;
    uint32_t y;
    uint8_t cellX;
    uint8_t cellY;
    uint8_t cellDenominator;
}

@end

@implementation CSGDesignPoint

-(uint32_t) x
{
    return x;
}

-(uint32_t) y
{
    return y;
}

-(uint8_t) cellX
{
    return cellX;
}

-(uint8_t) cellY
{
    return cellY;
}

-(uint8_t) cellDenominator
{
    return cellDenominator;
}

- (id) init
{
    NSAssert( false, @"Please use designated initializer" );
    
    return nil;
}


- (id) initWithX: (uint32_t) aX Y: (uint32_t) anY CellX: (uint8_t) aCellX CellY: (uint8_t) aCellY CellDenominator: (uint8_t) aCellDenominator
{
    if (self = [super init])
    {
        x = aX;
        y = anY;
        cellX = aCellX;
        cellY = aCellY;
        cellDenominator = aCellDenominator;
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
    
    return [self isEqualToCSGDesignCoordinate: object];
}

- (BOOL) isEqualToCSGDesignCoordinate: (CSGDesignPoint*) aCoordinate
{
    if (self == aCoordinate)
    {
        return YES;
    }
    if (x != aCoordinate.x || y != aCoordinate.y || cellX != aCoordinate.cellX || cellY != aCoordinate.cellY || cellDenominator != aCoordinate.cellDenominator)
    {
        return NO;
    }
    
    return YES;
}

- (NSUInteger)hash
{
    NSUInteger hash = 17;
    hash = hash*31 + x;
    hash = hash*31 + y;
    hash = hash*31 + cellX;
    hash = hash*31 + cellY;
    hash = hash*31 + cellDenominator;
    
    return hash;
}


@end
