//
//  CSGNeedlePerforationCoordinate.m
//  Cross Stitch Library
//
//  Created by 123 on 03.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGDesignCoordinate.h"

@interface CSGDesignCoordinate ()
{
    uint32_t x;
    uint32_t y;
    uint8_t cellX;
    uint8_t cellY;
    uint8_t cellDenominator;
}

@end

@implementation CSGDesignCoordinate

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

@end

@implementation CSGDesignCoordinate (Serialization)

- (size_t) serializedLength
{
    return sizeof(uint32_t) * 2 + sizeof(uint8_t) * 3;
}

- (void) serializeWithBinaryEncoder: (CSGBinaryEncoder *) anEncoder
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

- (id) initWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder
{
    
    const uint32_t *buf = [anDecoder readBytes:sizeof(uint32_t)];
    uint32_t aX = *buf;
    
    const uint8_t *buf1 = [anDecoder readBytes:sizeof(uint32_t)];
    uint32_t anY = *buf1;
    
    
    
}

@end
