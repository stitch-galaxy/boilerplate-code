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

@implementation CSGDesignPoint (Serialization)

- (size_t) serializedLength
{
    return sizeof(uint32_t) * 2 + sizeof(uint8_t) * 3;
}

- (void) serializeWithBinaryEncoder: (CSGBinaryEncoder *) anEncoder ObjectsRegistry: (CSGObjectsRegistry*) registry
{
    uint32_t* buf = [anEncoder modifyBytes:sizeof(uint32_t)];
    *buf = x;
    
    uint32_t* buf1 = [anEncoder modifyBytes:sizeof(uint32_t)];
    *buf1 = y;
    
    uint8_t* buf2 = [anEncoder modifyBytes:sizeof(uint8_t)];
    *buf2 = cellX;
    
    uint8_t* buf3 = [anEncoder modifyBytes:sizeof(uint8_t)];
    *buf3 = cellY;
    
    uint8_t* buf4 = [anEncoder modifyBytes:sizeof(uint8_t)];
    *buf4 = cellDenominator;
}

+ (id) deserializeWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder ObjectsRegistry: (CSGObjectsRegistry*) registry;
{

    const uint32_t *buf = [anDecoder readBytes:sizeof(uint32_t)];
    uint32_t aX = *buf;
    
    const uint32_t *buf1 = [anDecoder readBytes:sizeof(uint32_t)];
    uint32_t anY = *buf1;
    
    const uint8_t *buf2 =[anDecoder readBytes:sizeof(uint8_t)];
    uint8_t aCellX = *buf2;
    
    const uint8_t *buf3 =[anDecoder readBytes:sizeof(uint8_t)];
    uint8_t aCellY = *buf3;

    
    const uint8_t *buf4 =[anDecoder readBytes:sizeof(uint8_t)];
    uint8_t aCellDenominator = *buf4;

    CSGDesignPoint *point = [[CSGDesignPoint alloc] initWithX:aX Y:anY CellX:aCellX CellY:aCellY CellDenominator:aCellDenominator];

    return [registry getDesignPoint: point];
}

@end
