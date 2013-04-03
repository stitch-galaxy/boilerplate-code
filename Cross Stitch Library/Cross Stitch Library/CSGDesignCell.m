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
} CSG_STITCH_IN_CELL_TYPE;

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
    
    return [self isEqualToCSGDesignCell: object];
}

- (BOOL) isEqualToCSGDesignCell: (CSGDesignCell*) aCell
{
    if (self == aCell)
    {
        return YES;
    }
    //CROSS
    if (crossStitch != NULL)
    {
        if (![crossStitch isEqual:aCell.crossStitch]) return NO;
    }
    else
    {
        if (aCell.crossStitch) return NO;
    }
    //Petites
    if (leftUpPetiteStitch != NULL)
    {
        if (![leftUpPetiteStitch isEqual:aCell.leftUpPetiteStitch]) return NO;
    }
    else
    {
        if (aCell.leftUpPetiteStitch) return NO;
    }
    
    if (leftDownPetiteStitch != NULL)
    {
        if (![leftDownPetiteStitch isEqual:aCell.leftDownPetiteStitch]) return NO;
    }
    else
    {
        if (aCell.leftDownPetiteStitch) return NO;
    }
    
    if (rightUpPetiteStitch != NULL)
    {
        if (![rightUpPetiteStitch isEqual:aCell.rightUpPetiteStitch]) return NO;
    }
    else
    {
        if (aCell.rightUpPetiteStitch) return NO;
    }
    
    if (rightDownPetiteStitch != NULL)
    {
        if (![rightDownPetiteStitch isEqual:aCell.rightDownPetiteStitch]) return NO;
    }
    else
    {
        if (aCell.rightDownPetiteStitch) return NO;
    }
    //Quarter stitches
    if (leftUpQuarterStitch != NULL)
    {
        if (![leftUpQuarterStitch isEqual:aCell.leftUpQuarterStitch]) return NO;
    }
    else
    {
        if (aCell.leftUpQuarterStitch) return NO;
    }
    
    if (leftDownQuarterStitch != NULL)
    {
        if (![leftDownQuarterStitch isEqual:aCell.leftDownQuarterStitch]) return NO;
    }
    else
    {
        if (aCell.leftDownQuarterStitch) return NO;
    }
    
    if (rightUpQuarterStitch != NULL)
    {
        if (![rightUpQuarterStitch isEqual:aCell.rightUpQuarterStitch]) return NO;
    }
    else
    {
        if (aCell.rightUpQuarterStitch) return NO;
    }
    
    if (rightDownQuarterStitch != NULL)
    {
        if (![rightDownQuarterStitch isEqual:aCell.rightDownQuarterStitch]) return NO;
    }
    else
    {
        if (aCell.rightDownQuarterStitch) return NO;
    }
    //ThreeQuarter stitches
    if (leftUpThreeQuarterStitch != NULL)
    {
        if (![leftUpThreeQuarterStitch isEqual:aCell.leftUpThreeQuarterStitch]) return NO;
    }
    else
    {
        if (aCell.leftUpThreeQuarterStitch) return NO;
    }
    
    if (leftDownThreeQuarterStitch != NULL)
    {
        if (![leftDownThreeQuarterStitch isEqual:aCell.leftDownThreeQuarterStitch]) return NO;
    }
    else
    {
        if (aCell.leftDownThreeQuarterStitch) return NO;
    }
    
    if (rightUpThreeQuarterStitch != NULL)
    {
        if (![rightUpThreeQuarterStitch isEqual:aCell.rightUpThreeQuarterStitch]) return NO;
    }
    else
    {
        if (aCell.rightUpThreeQuarterStitch) return NO;
    }
    
    if (rightDownThreeQuarterStitch != NULL)
    {
        if (![rightDownThreeQuarterStitch isEqual:aCell.rightDownThreeQuarterStitch]) return NO;
    }
    else
    {
        if (aCell.rightDownThreeQuarterStitch) return NO;
    }
    //HalfStitches
    if (slashHalfStitch != NULL)
    {
        if (![slashHalfStitch isEqual:aCell.slashHalfStitch]) return NO;
    }
    else
    {
        if (aCell.slashHalfStitch) return NO;
    }
    if (backslashHalfStitch != NULL)
    {
        if (![backslashHalfStitch isEqual:aCell.backslashHalfStitch]) return NO;
    }
    else
    {
        if (aCell.backslashHalfStitch) return NO;
    }
    //French knots
    if (frenchKnot00 != NULL)
    {
        if (![frenchKnot00 isEqual:aCell.frenchKnot00]) return NO;
    }
    else
    {
        if (aCell.frenchKnot00) return NO;
    }
    if (frenchKnot01 != NULL)
    {
        if (![frenchKnot01 isEqual:aCell.frenchKnot01]) return NO;
    }
    else
    {
        if (aCell.frenchKnot01) return NO;
    }
    if (frenchKnot02 != NULL)
    {
        if (![frenchKnot02 isEqual:aCell.frenchKnot02]) return NO;
    }
    else
    {
        if (aCell.frenchKnot02) return NO;
    }
    if (frenchKnot10 != NULL)
    {
        if (![frenchKnot10 isEqual:aCell.frenchKnot10]) return NO;
    }
    else
    {
        if (aCell.frenchKnot10) return NO;
    }
    if (frenchKnot11 != NULL)
    {
        if (![frenchKnot11 isEqual:aCell.frenchKnot11]) return NO;
    }
    else
    {
        if (aCell.frenchKnot11) return NO;
    }
    if (frenchKnot12 != NULL)
    {
        if (![frenchKnot12 isEqual:aCell.frenchKnot12]) return NO;
    }
    else
    {
        if (aCell.frenchKnot12) return NO;
    }
    if (frenchKnot20 != NULL)
    {
        if (![frenchKnot20 isEqual:aCell.frenchKnot20]) return NO;
    }
    else
    {
        if (aCell.frenchKnot20) return NO;
    }
    if (frenchKnot21 != NULL)
    {
        if (![frenchKnot21 isEqual:aCell.frenchKnot21]) return NO;
    }
    else
    {
        if (aCell.frenchKnot21) return NO;
    }
    if (frenchKnot22 != NULL)
    {
        if (![frenchKnot22 isEqual:aCell.frenchKnot22]) return NO;
    }
    else
    {
        if (aCell.frenchKnot22) return NO;
    }
    
    return YES;
}

- (NSUInteger)hash
{
    NSUInteger hash = 17;
    //cross
    hash = hash*31 + crossStitch.hash;
    //Petites
    hash = hash*31 + leftUpPetiteStitch.hash;
    hash = hash*31 + leftDownPetiteStitch.hash;
    hash = hash*31 + rightUpPetiteStitch.hash;
    hash = hash*31 + rightDownPetiteStitch.hash;
    //Quarter stitches
    hash = hash*31 + leftUpQuarterStitch.hash;
    hash = hash*31 + leftDownQuarterStitch.hash;
    hash = hash*31 + rightUpQuarterStitch.hash;
    hash = hash*31 + rightDownQuarterStitch.hash;
    //ThreeQuarter stitches
    hash = hash*31 + leftUpThreeQuarterStitch.hash;
    hash = hash*31 + leftDownThreeQuarterStitch.hash;
    hash = hash*31 + rightUpThreeQuarterStitch.hash;
    hash = hash*31 + rightDownThreeQuarterStitch.hash;
    //HalfStitches
    hash = hash*31 + slashHalfStitch.hash;
    hash = hash*31 + backslashHalfStitch.hash;
    //French knots
    hash = hash*31 + frenchKnot00.hash;
    hash = hash*31 + frenchKnot01.hash;
    hash = hash*31 + frenchKnot02.hash;
    hash = hash*31 + frenchKnot10.hash;
    hash = hash*31 + frenchKnot11.hash;
    hash = hash*31 + frenchKnot12.hash;
    hash = hash*31 + frenchKnot20.hash;
    hash = hash*31 + frenchKnot21.hash;
    hash = hash*31 + frenchKnot22.hash;
    
    return hash;
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
    //TERMINAL 0
    length += sizeof(uint8_t);
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
        *buf = CSG_STITCH_IN_CELL_LEFT_UP_PETITE;
        [leftUpPetiteStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.leftDownPetiteStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_LEFT_DOWN_PETITE;
        [leftDownPetiteStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.rightUpPetiteStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_RIGHT_UP_PETITE;
        [rightUpPetiteStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.rightDownPetiteStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_RIGHT_DOWN_PETITE;
        [rightDownPetiteStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    //Quarter
    if(self.leftUpQuarterStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_LEFT_UP_QUARTER;
        [leftUpQuarterStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.leftDownQuarterStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_LEFT_DOWN_QUARTER;
        [leftDownQuarterStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.rightUpQuarterStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_RIGHT_UP_QUARTER;
        [rightUpQuarterStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.rightDownQuarterStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_RIGHT_DOWN_QUARTER;
        [rightDownQuarterStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    //ThreeQuarter stitches
    if(self.leftUpThreeQuarterStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_LEFT_UP_THREE_QUARTER;
        [leftUpThreeQuarterStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.leftDownThreeQuarterStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_LEFT_DOWN_THREE_QUARTER;
        [leftDownThreeQuarterStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.rightUpThreeQuarterStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_RIGHT_UP_THREE_QUARTER;
        [rightUpThreeQuarterStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.rightDownThreeQuarterStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_RIGHT_DOWN_THREE_QUARTER;
        [rightDownThreeQuarterStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    //HalfStitches
    if(self.slashHalfStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_SLASH_HALF;
        [slashHalfStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.backslashHalfStitch)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_BACKSLASH_HALF;
        [backslashHalfStitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    //French knots
    if(self.frenchKnot00)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_FRENCH_KNOT_00;
        [frenchKnot00 serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.frenchKnot01)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_FRENCH_KNOT_01;
        [frenchKnot01 serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.frenchKnot02)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_FRENCH_KNOT_02;
        [frenchKnot02 serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.frenchKnot10)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_FRENCH_KNOT_10;
        [frenchKnot10 serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.frenchKnot11)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_FRENCH_KNOT_11;
        [frenchKnot11 serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.frenchKnot12)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_FRENCH_KNOT_12;
        [frenchKnot12 serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.frenchKnot20)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_FRENCH_KNOT_20;
        [frenchKnot20 serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.frenchKnot21)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_FRENCH_KNOT_21;
        [frenchKnot21 serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    if(self.frenchKnot22)
    {
        uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
        *buf = CSG_STITCH_IN_CELL_FRENCH_KNOT_22;
        [frenchKnot22 serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
    }
    //Encode terminal 0
    uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
    *buf = 0;
}

- (id) initWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder ThreadsPalette: (CSGThreadsPalette*) palette
{
    if (self = [super init])
    {
        const uint8_t *buf = [anDecoder readBytes:sizeof(uint8_t)];
        uint8_t stitchType = *buf;
        while (stitchType)
        {
            switch (stitchType) {
                    //CROSS
                case CSG_STITCH_IN_CELL_CROSS:
                    crossStitch = [[CSGStitchInCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:palette];
                    break;
                    //PETITE
                case CSG_STITCH_IN_CELL_LEFT_UP_PETITE:
                    leftUpPetiteStitch = [[CSGStitchInCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:palette];
                    break;
                case CSG_STITCH_IN_CELL_LEFT_DOWN_PETITE:
                    leftDownPetiteStitch = [[CSGStitchInCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:palette];
                    break;
                case CSG_STITCH_IN_CELL_RIGHT_UP_PETITE:
                    rightUpPetiteStitch = [[CSGStitchInCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:palette];
                    break;
                case CSG_STITCH_IN_CELL_RIGHT_DOWN_PETITE:
                    rightDownPetiteStitch = [[CSGStitchInCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:palette];
                    break;
                    //QUARTER
                case CSG_STITCH_IN_CELL_LEFT_UP_QUARTER:
                    leftUpQuarterStitch = [[CSGStitchInCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:palette];
                    break;
                case CSG_STITCH_IN_CELL_LEFT_DOWN_QUARTER:
                    leftDownQuarterStitch = [[CSGStitchInCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:palette];
                    break;
                case CSG_STITCH_IN_CELL_RIGHT_UP_QUARTER:
                    rightUpQuarterStitch = [[CSGStitchInCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:palette];
                    break;
                case CSG_STITCH_IN_CELL_RIGHT_DOWN_QUARTER:
                    rightDownQuarterStitch = [[CSGStitchInCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:palette];
                    break;
                    //THREE QUARTER
                case CSG_STITCH_IN_CELL_LEFT_UP_THREE_QUARTER:
                    leftUpThreeQuarterStitch = [[CSGStitchInCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:palette];
                    break;
                case CSG_STITCH_IN_CELL_LEFT_DOWN_THREE_QUARTER:
                    leftDownThreeQuarterStitch = [[CSGStitchInCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:palette];
                    break;
                case CSG_STITCH_IN_CELL_RIGHT_UP_THREE_QUARTER:
                    rightUpThreeQuarterStitch = [[CSGStitchInCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:palette];
                    break;
                case CSG_STITCH_IN_CELL_RIGHT_DOWN_THREE_QUARTER:
                    rightDownThreeQuarterStitch = [[CSGStitchInCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:palette];
                    break;
                    //HALF_STITCHES
                case CSG_STITCH_IN_CELL_SLASH_HALF:
                    slashHalfStitch = [[CSGStitchInCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:palette];
                    break;
                case CSG_STITCH_IN_CELL_BACKSLASH_HALF:
                    backslashHalfStitch = [[CSGStitchInCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:palette];
                    break;
                    //FRENCH KNOTS
                case CSG_STITCH_IN_CELL_FRENCH_KNOT_00:
                    frenchKnot00 = [[CSGStitchInCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:palette];
                    break;
                case CSG_STITCH_IN_CELL_FRENCH_KNOT_01:
                    frenchKnot01 = [[CSGStitchInCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:palette];
                    break;
                case CSG_STITCH_IN_CELL_FRENCH_KNOT_02:
                    frenchKnot02 = [[CSGStitchInCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:palette];
                    break;
                case CSG_STITCH_IN_CELL_FRENCH_KNOT_10:
                    frenchKnot10 = [[CSGStitchInCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:palette];
                    break;
                case CSG_STITCH_IN_CELL_FRENCH_KNOT_11:
                    frenchKnot11 = [[CSGStitchInCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:palette];
                    break;
                case CSG_STITCH_IN_CELL_FRENCH_KNOT_12:
                    frenchKnot12 = [[CSGStitchInCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:palette];
                    break;
                case CSG_STITCH_IN_CELL_FRENCH_KNOT_20:
                    frenchKnot20 = [[CSGStitchInCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:palette];
                    break;
                case CSG_STITCH_IN_CELL_FRENCH_KNOT_21:
                    frenchKnot21 = [[CSGStitchInCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:palette];
                    break;
                case CSG_STITCH_IN_CELL_FRENCH_KNOT_22:
                    frenchKnot22 = [[CSGStitchInCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:palette];
                    break;
                default:
                    break;
            }
            
            //Read next stitch or terminal byte
            buf = [anDecoder readBytes:sizeof(uint8_t)];
            stitchType = *buf;
        }
    }
    return self;
}

@end
