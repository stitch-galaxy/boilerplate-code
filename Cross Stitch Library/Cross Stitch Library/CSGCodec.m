//
//  CSGCodec.m
//  Cross Stitch Library
//
//  Created by 123 on 28.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGCodec.h"

#import "CSGObjectsRegistry.h"
#import "CSGBinaryCoding.h"

#import "CSGThread.h"
#import "CSGThreadsBlend.h"
#import "CSGDesignCell.h"
#import "CSGDesignPoint.h"
#import "CSGDesignPoints.h"
#import "CSGBackStitch.h"
#import "CSGStraightStitch.h"

@interface CSGCodec()

@property (nonatomic, retain) CSGBinaryEncoder* encoder;
@property (nonatomic, retain) CSGObjectsRegistry* registry;

@end

@implementation CSGCodec

@synthesize encoder = anEncoder;//TODO: rename to encoder
@synthesize registry;

- (id) initWithObjectsRegistry: (CSGObjectsRegistry*) anRegistry
{
    if (self = [super init])
    {
        registry = anRegistry;
    }
    return self;
}

- (NSData*) data
{
    return anEncoder.data;
}

- (size_t) serializedObjectsRegistryLength
{
    //TODO: implement when necessary
    return 0;
}

- (void) serializeObjectsRegistry
{
    //TODO: implement when necessary
}

- (void) serializeThread: (CSGThread*) aThread
{
    size_t anSize =  self.serializedObjectsRegistryLength;
    anSize += [self serializedThreadLength: aThread];
    anEncoder = [[CSGBinaryEncoder alloc] initWithLength: anSize];
    [self serializeObjectsRegistry];
    
    [self serializeThreadImpl:aThread];
}

-(size_t) serializedThreadLength: (CSGThread*) aThread
{
    return sizeof(uint8_t) * 3;
}

- (void) serializeThreadImpl:(CSGThread *)aThread
{
    const CGFloat *components = CGColorGetComponents(aThread.color.CGColor);
    uint8_t red = lroundf(components[0] * 255.0);
    uint8_t green = lround(components[1] * 255.0);
    uint8_t blue = lround(components[2] * 255.0);
    
    uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
    *buf = red;
    buf = [anEncoder modifyBytes:sizeof(uint8_t)];
    *buf = green;
    buf = [anEncoder modifyBytes:sizeof(uint8_t)];
    *buf = blue;
}

- (void) serializeThreadInBlend:(CSGThreadInBlend *)aThread
{
    size_t anSize =  self.serializedObjectsRegistryLength;
    anSize += [self serializedThreadInBlendLength: aThread];
    anEncoder = [[CSGBinaryEncoder alloc] initWithLength: anSize];
    
    [self serializeObjectsRegistry];
    [self serializeThreadInBlendImpl:aThread];
}

-(size_t) serializedThreadInBlendLength: (CSGThreadInBlend*) aThread
{
    return [self serializedThreadLength:aThread.thread] + sizeof(uint8_t);
}

- (void) serializeThreadInBlendImpl:(CSGThreadInBlend*)aThread
{
    [self serializeThreadImpl:aThread.thread];
    
    uint8_t *buf = [anEncoder modifyBytes: sizeof(uint8_t)];
    *buf = aThread.flossCount;
}

- (void) serializeThreadsBlend: (CSGThreadsBlend*) aBlend
{
    size_t anSize =  self.serializedObjectsRegistryLength;
    anSize += [self serializedThreadsBlendLength: aBlend];
    anEncoder = [[CSGBinaryEncoder alloc] initWithLength: anSize];
    
    [self serializeObjectsRegistry];
    [self serializeThreadsBlendImpl:aBlend];
}

- (size_t) serializedThreadsBlendLength: (CSGThreadsBlend*) aBlend
{
    size_t result = sizeof(uint8_t);
    
    for(CSGThreadInBlend *thread in aBlend.threadsInBlend)
    {
        result += [self serializedThreadInBlendLength:thread];
    }
    return result;
}

- (void) serializeThreadsBlendImpl: (CSGThreadsBlend*) aBlend
{
    uint8_t* buf = [anEncoder modifyBytes:sizeof(uint8_t)];
    *buf = aBlend.threadsInBlend.count;
    
    for(CSGThreadInBlend *thread in aBlend.threadsInBlend)
    {
        [self serializeThreadInBlendImpl:thread];
    }
}

- (void) serializeDesignCell: (CSGDesignCell*) aCell
{
    size_t anSize =  self.serializedObjectsRegistryLength;
    anSize += [self serializedDesignCellLength: aCell];
    anEncoder = [[CSGBinaryEncoder alloc] initWithLength: anSize];
    
    [self serializeObjectsRegistry];
    [self serializeDesignCellImpl:aCell];
}

- (size_t) serializedDesignCellLength: (CSGDesignCell*) aCell
{
    size_t length = 0;
    if(aCell.crossStitch)
    {
        length += sizeof(uint8_t);
        length += [self serializedThreadsBlendLength: aCell.crossStitch];
    }
    //Petite
    if(aCell.leftUpPetiteStitch)
    {
        length += sizeof(uint8_t);
        length += [self serializedThreadsBlendLength: aCell.leftUpPetiteStitch];
    }
    if(aCell.leftDownPetiteStitch)
    {
        length += sizeof(uint8_t);
        length += [self serializedThreadsBlendLength: aCell.leftDownPetiteStitch];
    }
    if(aCell.rightUpPetiteStitch)
    {
        length += sizeof(uint8_t);
        length += [self serializedThreadsBlendLength: aCell.rightUpPetiteStitch];
    }
    if(aCell.rightDownPetiteStitch)
    {
        length += sizeof(uint8_t);
        length += [self serializedThreadsBlendLength: aCell.rightDownPetiteStitch];
    }
    //Quarter
    if(aCell.leftUpQuarterStitch)
    {
        length += sizeof(uint8_t);
        length += [self serializedThreadsBlendLength: aCell.leftUpQuarterStitch];
    }
    if(aCell.leftDownQuarterStitch)
    {
        length += sizeof(uint8_t);
        length += [self serializedThreadsBlendLength: aCell.leftDownQuarterStitch];
    }
    if(aCell.rightUpQuarterStitch)
    {
        length += sizeof(uint8_t);
        length += [self serializedThreadsBlendLength: aCell.rightUpQuarterStitch];
    }
    if(aCell.rightDownQuarterStitch)
    {
        length += sizeof(uint8_t);
        length += [self serializedThreadsBlendLength: aCell.rightDownQuarterStitch];
    }
    //ThreeQuarter stitches
    if(aCell.leftUpThreeQuarterStitch)
    {
        length += sizeof(uint8_t);
        length += [self serializedThreadsBlendLength: aCell.leftUpThreeQuarterStitch];
    }
    if(aCell.leftDownThreeQuarterStitch)
    {
        length += sizeof(uint8_t);
        length += [self serializedThreadsBlendLength: aCell.leftDownThreeQuarterStitch];
    }
    if(aCell.rightUpThreeQuarterStitch)
    {
        length += sizeof(uint8_t);
        length += [self serializedThreadsBlendLength: aCell.rightUpThreeQuarterStitch];
    }
    if(aCell.rightDownThreeQuarterStitch)
    {
        length += sizeof(uint8_t);
        length += [self serializedThreadsBlendLength: aCell.rightDownThreeQuarterStitch];
    }
    //HalfStitches
    if(aCell.slashHalfStitch)
    {
        length += sizeof(uint8_t);
        length += [self serializedThreadsBlendLength: aCell.slashHalfStitch];
    }
    if(aCell.backslashHalfStitch)
    {
        length += sizeof(uint8_t);
        length += [self serializedThreadsBlendLength: aCell.backslashHalfStitch];
    }
    //French knots
    if(aCell.frenchKnot00)
    {
        length += sizeof(uint8_t);
        length += [self serializedThreadsBlendLength: aCell.frenchKnot00];
    }
    if(aCell.frenchKnot01)
    {
        length += sizeof(uint8_t);
        length += [self serializedThreadsBlendLength: aCell.frenchKnot01];
    }
    if(aCell.frenchKnot02)
    {
        length += sizeof(uint8_t);
        length += [self serializedThreadsBlendLength: aCell.frenchKnot02];
    }
    if(aCell.frenchKnot10)
    {
        length += sizeof(uint8_t);
        length += [self serializedThreadsBlendLength: aCell.frenchKnot10];
    }
    if(aCell.frenchKnot11)
    {
        length += sizeof(uint8_t);
        length += [self serializedThreadsBlendLength: aCell.frenchKnot11];
    }
    if(aCell.frenchKnot12)
    {
        length += sizeof(uint8_t);
        length += [self serializedThreadsBlendLength: aCell.frenchKnot12];
    }
    if(aCell.frenchKnot20)
    {
        length += sizeof(uint8_t);
        length += [self serializedThreadsBlendLength: aCell.frenchKnot20];
    }
    if(aCell.frenchKnot21)
    {
        length += sizeof(uint8_t);
        length += [self serializedThreadsBlendLength: aCell.frenchKnot21];
    }
    if(aCell.frenchKnot22)
    {
        length += sizeof(uint8_t);
        length += [self serializedThreadsBlendLength: aCell.frenchKnot22];
    }
    //TERMINAL 0
    length += sizeof(uint8_t);
    return length;
}

- (void) serializeDesignCellImpl: (CSGDesignCell*) aCell
{
    if(aCell.crossStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_CROSS;
        [self serializeThreadsBlendImpl: aCell.crossStitch];
    }
    //Petite
    if(aCell.leftUpPetiteStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_LEFT_UP_PETITE;
        [self serializeThreadsBlendImpl: aCell.leftUpPetiteStitch];
    }
    if(aCell.leftDownPetiteStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_LEFT_DOWN_PETITE;
        [self serializeThreadsBlendImpl: aCell.leftDownPetiteStitch];
    }
    if(aCell.rightUpPetiteStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_RIGHT_UP_PETITE;
        [self serializeThreadsBlendImpl: aCell.rightUpPetiteStitch];
    }
    if(aCell.rightDownPetiteStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_RIGHT_DOWN_PETITE;
        [self serializeThreadsBlendImpl: aCell.rightDownPetiteStitch];

    }
    //Quarter
    if(aCell.leftUpQuarterStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_LEFT_UP_QUARTER;
        [self serializeThreadsBlendImpl: aCell.leftUpQuarterStitch];
    }
    if(aCell.leftDownQuarterStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_LEFT_DOWN_QUARTER;
        [self serializeThreadsBlendImpl: aCell.leftDownQuarterStitch];
    }
    if(aCell.rightUpQuarterStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_RIGHT_UP_QUARTER;
        [self serializeThreadsBlendImpl: aCell.rightUpQuarterStitch];
    }
    if(aCell.rightDownQuarterStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_RIGHT_DOWN_QUARTER;
        [self serializeThreadsBlendImpl: aCell.rightDownQuarterStitch];
    }
    //ThreeQuarter stitches
    if(aCell.leftUpThreeQuarterStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_LEFT_UP_THREE_QUARTER;
        [self serializeThreadsBlendImpl: aCell.leftUpThreeQuarterStitch];
    }
    if(aCell.leftDownThreeQuarterStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_LEFT_DOWN_THREE_QUARTER;
        [self serializeThreadsBlendImpl: aCell.leftDownThreeQuarterStitch];
    }
    if(aCell.rightUpThreeQuarterStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_RIGHT_UP_THREE_QUARTER;
        [self serializeThreadsBlendImpl: aCell.rightUpThreeQuarterStitch];
    }
    if(aCell.rightDownThreeQuarterStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_RIGHT_DOWN_THREE_QUARTER;
        [self serializeThreadsBlendImpl: aCell.rightDownThreeQuarterStitch];
    }
    //HalfStitches
    if(aCell.slashHalfStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_SLASH_HALF;
        [self serializeThreadsBlendImpl: aCell.slashHalfStitch];
    }
    if(aCell.backslashHalfStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_BACKSLASH_HALF;
        [self serializeThreadsBlendImpl: aCell.backslashHalfStitch];
    }
    //French knots
    if(aCell.frenchKnot00)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_FRENCH_KNOT_00;
        [self serializeThreadsBlendImpl: aCell.frenchKnot00];
    }
    if(aCell.frenchKnot01)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_FRENCH_KNOT_01;
        [self serializeThreadsBlendImpl: aCell.frenchKnot01];
    }
    if(aCell.frenchKnot02)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_FRENCH_KNOT_02;
        [self serializeThreadsBlendImpl: aCell.frenchKnot02];
    }
    if(aCell.frenchKnot10)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_FRENCH_KNOT_10;
        [self serializeThreadsBlendImpl: aCell.frenchKnot10];
    }
    if(aCell.frenchKnot11)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_FRENCH_KNOT_11;
        [self serializeThreadsBlendImpl: aCell.frenchKnot11];
    }
    if(aCell.frenchKnot12)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_FRENCH_KNOT_12;
        [self serializeThreadsBlendImpl: aCell.frenchKnot12];
    }
    if(aCell.frenchKnot20)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_FRENCH_KNOT_20;
        [self serializeThreadsBlendImpl: aCell.frenchKnot20];
    }
    if(aCell.frenchKnot21)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_FRENCH_KNOT_21;
        [self serializeThreadsBlendImpl: aCell.frenchKnot21];
    }
    if(aCell.frenchKnot22)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_FRENCH_KNOT_22;
        [self serializeThreadsBlendImpl: aCell.frenchKnot22];
    }
    //Encode terminal 0
    uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
    *buf = 0;
}


- (void) serializeDesignPoint: (CSGDesignPoint*) aPoint
{
    size_t anSize =  self.serializedObjectsRegistryLength;
    anSize += [self serializedDesignPointLength: aPoint];
    anEncoder = [[CSGBinaryEncoder alloc] initWithLength: anSize];
    
    [self serializeObjectsRegistry];
    [self serializeDesignPointImpl:aPoint];
}

-(size_t) serializedDesignPointLength: (CSGDesignPoint*) aPoint
{
    return sizeof(uint32_t) * 2 + sizeof(uint8_t) * 3;
}

- (void) serializeDesignPointImpl: (CSGDesignPoint*) aPoint
{
    uint32_t* buf = [anEncoder modifyBytes:sizeof(uint32_t)];
    *buf = aPoint.x;
    
    uint32_t* buf1 = [anEncoder modifyBytes:sizeof(uint32_t)];
    *buf1 = aPoint.y;
    
    uint8_t* buf2 = [anEncoder modifyBytes:sizeof(uint8_t)];
    *buf2 = aPoint.cellX;
    
    uint8_t* buf3 = [anEncoder modifyBytes:sizeof(uint8_t)];
    *buf3 = aPoint.cellY;
    
    uint8_t* buf4 = [anEncoder modifyBytes:sizeof(uint8_t)];
    *buf4 = aPoint.cellDenominator;

}

- (void) serializeDesignPoints: (CSGDesignPoints*) aPoints
{
    size_t anSize =  self.serializedObjectsRegistryLength;
    anSize += [self serializedDesignPointsLength: aPoints];
    anEncoder = [[CSGBinaryEncoder alloc] initWithLength: anSize];
    
    [self serializeObjectsRegistry];
    [self serializeDesignPointsImpl:aPoints];
}

-(size_t) serializedDesignPointsLength: (CSGDesignPoints*) aPoints
{
    size_t size = sizeof(uint32_t);
    for(CSGDesignPoint *coord in aPoints.points)
    {
        size += [self serializedDesignPointLength: coord];
    }
    return size;
}

- (void) serializeDesignPointsImpl: (CSGDesignPoints*) aPoints
{
    uint32_t* buf = [anEncoder modifyBytes:sizeof(uint32_t)];
    *buf = aPoints.points.count;
    for(CSGDesignPoint *coord in aPoints.points)
    {
        [self serializeDesignPointImpl:coord];
    }
}

- (void) serializeCSGBackStitch: (CSGBackStitch*) aStitch
{
    size_t anSize =  self.serializedObjectsRegistryLength;
    anSize += [self serializedCSGBackStitchLength: aStitch];
    anEncoder = [[CSGBinaryEncoder alloc] initWithLength: anSize];
    
    [self serializeObjectsRegistry];
    [self serializeCSGBackStitchImpl:aStitch];
}

- (size_t) serializedCSGBackStitchLength: (CSGBackStitch*) aStitch
{
    return [self serializedThreadsBlendLength:aStitch.threadBlend] + [self serializedDesignPointsLength:aStitch.curve];
}

- (void) serializeCSGBackStitchImpl: (CSGBackStitch*) aStitch
{
    [self serializeThreadsBlendImpl:aStitch.threadBlend];
    [self serializeDesignPointsImpl:aStitch.curve];
}

- (void) serializeCSGStraightStitch: (CSGStraightStitch*) aStitch
{
    size_t anSize =  self.serializedObjectsRegistryLength;
    anSize += [self serializedCSGStraightStitchLength: aStitch];
    anEncoder = [[CSGBinaryEncoder alloc] initWithLength: anSize];
    
    [self serializeObjectsRegistry];
    [self serializeCSGStraightStitchImpl:aStitch];
}

- (size_t) serializedCSGStraightStitchLength: (CSGStraightStitch*) aStitch
{
    return [self serializedThreadsBlendLength:aStitch.threadBlend] + [self serializedDesignPointsLength:aStitch.curve];
}

- (void) serializeCSGStraightStitchImpl: (CSGStraightStitch*) aStitch
{
    [self serializeThreadsBlendImpl:aStitch.threadBlend];
    [self serializeDesignPointsImpl:aStitch.curve];
}


@end

