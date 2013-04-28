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
#import "CSGDesign.h"
#import "CSGMemorySetWithIndex.h"
#import "CSGMemorySet.h"

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
    size_t size = sizeof(uint32_t);//cellsSet
    size += sizeof(uint32_t);//threadsMemorySet
    size += sizeof(uint32_t);//threadsInBlendMemorySet
    size += sizeof(uint32_t);//threadBlendsMemorySet
    
    for(CSGDesignCell *cell in registry.cellsMemorySet.objects)
    {
        size += [self serializedDesignCellLength:cell];
    }
    return size;
}

- (void) serializeObjectsRegistry
{
    uint32_t *buf = [anEncoder modifyBytes:sizeof(uint32_t)];
    *buf = registry.threadsMemorySet.count;
    
    buf = [anEncoder modifyBytes:sizeof(uint32_t)];
    *buf = registry.threadsInBlendMemorySet.count;
    
    buf = [anEncoder modifyBytes:sizeof(uint32_t)];
    *buf = registry.threadBlendsMemorySet.count;
    
    buf = [anEncoder modifyBytes:sizeof(uint32_t)];
    *buf = registry.cellsMemorySet.objects.count;
    
    for(CSGDesignCell *cell in registry.cellsMemorySet.objects)
    {
        [self serializeDesignCellImpl:cell];
    }
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

- (void) serializeBackStitch: (CSGBackStitch*) aStitch
{
    size_t anSize =  self.serializedObjectsRegistryLength;
    anSize += [self serializedBackStitchLength: aStitch];
    anEncoder = [[CSGBinaryEncoder alloc] initWithLength: anSize];
    
    [self serializeObjectsRegistry];
    [self serializeBackStitchImpl:aStitch];
}

- (size_t) serializedBackStitchLength: (CSGBackStitch*) aStitch
{
    return [self serializedThreadsBlendLength:aStitch.threadBlend] + [self serializedDesignPointsLength:aStitch.curve];
}

- (void) serializeBackStitchImpl: (CSGBackStitch*) aStitch
{
    [self serializeThreadsBlendImpl:aStitch.threadBlend];
    [self serializeDesignPointsImpl:aStitch.curve];
}

- (void) serializeStraightStitch: (CSGStraightStitch*) aStitch
{
    size_t anSize =  self.serializedObjectsRegistryLength;
    anSize += [self serializedStraightStitchLength: aStitch];
    anEncoder = [[CSGBinaryEncoder alloc] initWithLength: anSize];
    
    [self serializeObjectsRegistry];
    [self serializeStraightStitchImpl:aStitch];
}

- (size_t) serializedStraightStitchLength: (CSGStraightStitch*) aStitch
{
    return [self serializedThreadsBlendLength:aStitch.threadBlend] + [self serializedDesignPointsLength:aStitch.curve];
}

- (void) serializeStraightStitchImpl: (CSGStraightStitch*) aStitch
{
    [self serializeThreadsBlendImpl:aStitch.threadBlend];
    [self serializeDesignPointsImpl:aStitch.curve];
}

- (void) serializeDesign:(CSGDesign*) aDesign
{
    size_t anSize =  self.serializedObjectsRegistryLength;
    anSize += [self serializedDesignLength: aDesign];
    anEncoder = [[CSGBinaryEncoder alloc] initWithLength: anSize];
    
    [self serializeObjectsRegistry];
    [self serializeDesignImpl:aDesign];
}

- (size_t) serializedDesignLength:(CSGDesign*) aDesign
{
    size_t size = sizeof(uint32_t) * 2;
    size += sizeof(uint32_t) * aDesign.height * aDesign.width;
    
    size += sizeof(uint32_t);
    for(CSGBackStitch* stitch in aDesign.backStitches)
    {
        size += [self serializedBackStitchLength:stitch];
    }
    size += sizeof(uint32_t);
    for(CSGStraightStitch* stitch in aDesign.straightStitches)
    {
        size += [self serializedStraightStitchLength:stitch];
    }
    return size;
}

- (void) serializeDesignImpl:(CSGDesign*) aDesign
{
    uint32_t *pWidth = [anEncoder modifyBytes:sizeof(uint32_t)];
    *pWidth = aDesign.width;
    uint32_t *pHeight = [anEncoder modifyBytes:sizeof(uint32_t)];
    *pHeight = aDesign.height;
    
    
    uint32_t *pCellIndex = nil;
    for(CSGDesignCell* cell in aDesign.cells)
    {
        pCellIndex = [anEncoder modifyBytes:sizeof(uint32_t)];
        *pCellIndex = [registry getDesignCellIndex:cell];
    }
    
    uint32_t *pBackStitchesNum = [anEncoder modifyBytes:sizeof(uint32_t)];
    *pBackStitchesNum = (uint32_t) aDesign.backStitches.count;
    for(CSGBackStitch* stitch in aDesign.backStitches)
    {
        [self serializeBackStitchImpl: stitch];
    }
    
    uint32_t *pStraightStitchesNum = [anEncoder modifyBytes:sizeof(uint32_t)];
    *pStraightStitchesNum = (uint32_t) aDesign.straightStitches.count;
    for(CSGStraightStitch* stitch in aDesign.straightStitches)
    {
        [self serializeStraightStitchImpl: stitch];
    }
}


@end

