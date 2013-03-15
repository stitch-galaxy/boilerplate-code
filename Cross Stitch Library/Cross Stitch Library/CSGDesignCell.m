//
//  CSGDesignCell.m
//  Cross Stitch Library
//
//  Created by 123 on 16.03.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGDesignCell.h"

typedef enum
{
    //CROSS
    CSG_STITCH_IN_CELL_CROSS = 0x01,
    //PETITE
    CSG_STITCH_IN_CELL_LEFT_UP_PETITE,
    CSG_STITCH_IN_CELL_LEFT_DOWN_PETITE,
    CSG_STITCH_IN_CELL_RIGHT_UP_PETITE,
    CSG_STITCH_IN_CELL_RIGHT_DOWN_PETITE,
    //QUARTER
    CSG_STITCH_IN_CELL_LEFT_UP_QUARTER,
    CSG_STITCH_IN_CELL_LEFT_DOWN_QUARTER,
    CSG_STITCH_IN_CELL_RIGHT_UP_QUARTER,
    CSG_STITCH_IN_CELL_RIGHT_DOWN_QUARTER,
    //THREE QUARTER
    CSG_STITCH_IN_CELL_LEFT_UP_THREE_QUARTER,
    CSG_STITCH_IN_CELL_LEFT_DOWN_THREE_QUARTER,
    CSG_STITCH_IN_CELL_RIGHT_UP_THREE_QUARTER,
    CSG_STITCH_IN_CELL_RIGHT_DOWN_THREE_QUARTER,
    //HALF_STITCHES
    CSG_STITCH_IN_CELL_SLASH_HALF,
    CSG_STITCH_IN_CELL_BACKSLASH_HALF,
    //FRENCH KNOTS
    CSG_STITCH_IN_CELL_FRENCH_KNOT_00,
    CSG_STITCH_IN_CELL_FRENCH_KNOT_01,
    CSG_STITCH_IN_CELL_FRENCH_KNOT_02,
    CSG_STITCH_IN_CELL_FRENCH_KNOT_10,
    CSG_STITCH_IN_CELL_FRENCH_KNOT_11,
    CSG_STITCH_IN_CELL_FRENCH_KNOT_12,
    CSG_STITCH_IN_CELL_FRENCH_KNOT_20,
    CSG_STITCH_IN_CELL_FRENCH_KNOT_21,
    CSG_STITCH_IN_CELL_FRENCH_KNOT_22,
} CSG_STITCH_IN_CELL_MASK;

@implementation CSGDesignCell

//Cross stitch
@synthesize crossStitch;
//Petites
@synthesize leftUpPetiteStitch;
@synthesize leftDownPetiteStitch;
@synthesize rightUpPetiteStitch;
@synthesize rightDownPetiteStitch;
//Quarter stitches
@synthesize leftUpQuarterStitch;
@synthesize leftDownQuarterStitch;
@synthesize rightUpQuarterStitch;
@synthesize rightDownQuarterStitch;
//Three quarter stitch
@synthesize leftUpThreeQuarterStitch;
@synthesize leftDownThreeQuarterStitch;
@synthesize rightUpThreeQuarterStitch;
@synthesize rightDownThreeQuarterStitch;
//HalfStitches
@synthesize slashHalfStitch;
@synthesize backslashHalfStitch;
//French knots
@synthesize frenchKnot00;
@synthesize frenchKnot01;
@synthesize frenchKnot02;
@synthesize frenchKnot10;
@synthesize frenchKnot11;
@synthesize frenchKnot12;
@synthesize frenchKnot20;
@synthesize frenchKnot21;
@synthesize frenchKnot22;

- (id) init
{
    return self = [super init];
}

@end

@implementation CSGDesignCell (Serialization)

- (size_t) serializedLength
{
    
    size_t length = 0;
    if(self.crossStitch)
    {
        length += sizeof(uint8_t);
        length += self.crossStitch.serializedLength;
    }
    //Petite
    if(self.leftUpPetiteStitch)
    {
        length += sizeof(uint8_t);
        length += self.leftUpPetiteStitch.serializedLength;
    }
    if(self.leftDownPetiteStitch)
    {
        length += sizeof(uint8_t);
        length += self.leftDownPetiteStitch.serializedLength;
    }
    if(self.rightUpPetiteStitch)
    {
        length += sizeof(uint8_t);
        length += self.rightUpPetiteStitch.serializedLength;
    }
    if(self.rightDownPetiteStitch)
    {
        length += sizeof(uint8_t);
        length += self.rightDownPetiteStitch.serializedLength;
    }
    //Quarter
    if(self.leftUpQuarterStitch)
    {
        length += sizeof(uint8_t);
        length += self.leftUpQuarterStitch.serializedLength;
    }
    if(self.leftDownQuarterStitch)
    {
        length += sizeof(uint8_t);
        length += self.leftDownQuarterStitch.serializedLength;
    }
    if(self.rightUpQuarterStitch)
    {
        length += sizeof(uint8_t);
        length += self.rightUpQuarterStitch.serializedLength;
    }
    if(self.rightDownQuarterStitch)
    {
        length += sizeof(uint8_t);
        length += self.rightDownQuarterStitch.serializedLength;
    }
    //ThreeQuarter stitches
    if(self.leftUpThreeQuarterStitch)
    {
        length += sizeof(uint8_t);
        length += self.leftUpThreeQuarterStitch.serializedLength;
    }
    if(self.leftDownThreeQuarterStitch)
    {
        length += sizeof(uint8_t);
        length += self.leftDownThreeQuarterStitch.serializedLength;
    }
    if(self.rightUpThreeQuarterStitch)
    {
        length += sizeof(uint8_t);
        length += self.rightUpThreeQuarterStitch.serializedLength;
    }
    if(self.rightDownThreeQuarterStitch)
    {
        length += sizeof(uint8_t);
        length += self.rightDownThreeQuarterStitch.serializedLength;
    }
    //HalfStitches
    if(self.slashHalfStitch)
    {
        length += sizeof(uint8_t);
        length += self.slashHalfStitch.serializedLength;
    }
    if(self.backslashHalfStitch)
    {
        length += sizeof(uint8_t);
        length += self.backslashHalfStitch.serializedLength;
    }
    //French knots
    if(self.frenchKnot00)
    {
        length += sizeof(uint8_t);
        length += self.frenchKnot00.serializedLength;
    }
    if(self.frenchKnot01)
    {
        length += sizeof(uint8_t);
        length += self.frenchKnot01.serializedLength;
    }
    if(self.frenchKnot02)
    {
        length += sizeof(uint8_t);
        length += self.frenchKnot02.serializedLength;
    }
    if(self.frenchKnot10)
    {
        length += sizeof(uint8_t);
        length += self.frenchKnot10.serializedLength;
    }
    if(self.frenchKnot11)
    {
        length += sizeof(uint8_t);
        length += self.frenchKnot11.serializedLength;
    }
    if(self.frenchKnot12)
    {
        length += sizeof(uint8_t);
        length += self.frenchKnot12.serializedLength;
    }
    if(self.frenchKnot20)
    {
        length += sizeof(uint8_t);
        length += self.frenchKnot20.serializedLength;
    }
    if(self.frenchKnot21)
    {
        length += sizeof(uint8_t);
        length += self.frenchKnot21.serializedLength;
    }
    if(self.frenchKnot22)
    {
        length += sizeof(uint8_t);
        length += self.frenchKnot22.serializedLength;
    }
    return length;
}

- (void) serializeWithBinaryEncoder: (CSGBinaryEncoder *) anEncoder ThreadsPalette: (CSGThreadsPalette*) palette
{
    if(self.crossStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_CROSS;
        [crossStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    //Petite
    if(self.leftUpPetiteStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_CROSS;
        [leftUpPetiteStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.leftDownPetiteStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_CROSS;
        [leftDownPetiteStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.rightUpPetiteStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_CROSS;
        [rightUpPetiteStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.rightDownPetiteStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_CROSS;
        [rightDownPetiteStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    //Quarter
    if(self.leftUpQuarterStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_CROSS;
        [leftUpQuarterStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.leftDownQuarterStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_CROSS;
        [leftDownQuarterStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.rightUpQuarterStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_CROSS;
        [rightUpQuarterStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.rightDownQuarterStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_CROSS;
        [rightDownQuarterStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    //ThreeQuarter stitches
    if(self.leftUpThreeQuarterStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_CROSS;
        [leftUpThreeQuarterStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.leftDownThreeQuarterStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_CROSS;
        [leftDownThreeQuarterStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.rightUpThreeQuarterStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_CROSS;
        [rightUpThreeQuarterStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.rightDownThreeQuarterStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_CROSS;
        [rightDownThreeQuarterStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    //HalfStitches
    if(self.slashHalfStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_CROSS;
        [slashHalfStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.backslashHalfStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_CROSS;
        [backslashHalfStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    //French knots
    if(self.frenchKnot00)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_CROSS;
        [frenchKnot00 serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.frenchKnot01)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_CROSS;
        [frenchKnot01 serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.frenchKnot02)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_CROSS;
        [frenchKnot02 serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.frenchKnot10)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_CROSS;
        [frenchKnot10 serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.frenchKnot11)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_CROSS;
        [frenchKnot11 serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.frenchKnot12)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_CROSS;
        [frenchKnot12 serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.frenchKnot20)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_CROSS;
        [frenchKnot20 serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.frenchKnot21)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_CROSS;
        [frenchKnot21 serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.frenchKnot22)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_CROSS;
        [frenchKnot22 serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
}

//    crossStitch;
//    //Petites
//    leftUpPetiteStitch;
//    leftDownPetiteStitch;
//    rightUpPetiteStitch;
//    rightDownPetiteStitch;
//    //Quarter stitches
//    leftUpQuarterStitch;
//    leftDownQuarterStitch;
//    rightUpQuarterStitch;
//    rightDownQuarterStitch;
//    //ThreeQuarter stitches
//    leftUpThreeQuarterStitch;
//    leftDownThreeQuarterStitch;
//    rightUpThreeQuarterStitch;
//    rightDownThreeQuarterStitch;
//    //HalfStitches
//    slashHalfStitch;
//    backslashHalfStitch;
//    //French knots
//    frenchKnot00;
//    frenchKnot01;
//    frenchKnot02;
//    frenchKnot10;
//    frenchKnot11;
//    frenchKnot12;
//    frenchKnot20;
//    frenchKnot21;
//    frenchKnot22;




- (id) initWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder ThreadsPalette: (CSGThreadsPalette*) palette
{
}

@end
