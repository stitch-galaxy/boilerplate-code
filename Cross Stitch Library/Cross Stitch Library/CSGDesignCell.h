//
//  CSGDesignCell.h
//  Cross Stitch Library
//
//  Created by 123 on 16.03.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "CSGThreadsBlend.h"

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

@protocol CSGDesignCell <NSObject>

//Cross stitch
@property (nonatomic, retain) CSGThreadsBlend* crossStitch;
//Petites
@property (nonatomic, retain) CSGThreadsBlend* leftUpPetiteStitch;
@property (nonatomic, retain) CSGThreadsBlend* leftDownPetiteStitch;
@property (nonatomic, retain) CSGThreadsBlend* rightUpPetiteStitch;
@property (nonatomic, retain) CSGThreadsBlend* rightDownPetiteStitch;
//Quarter stitches
@property (nonatomic, retain) CSGThreadsBlend* leftUpQuarterStitch;
@property (nonatomic, retain) CSGThreadsBlend* leftDownQuarterStitch;
@property (nonatomic, retain) CSGThreadsBlend* rightUpQuarterStitch;
@property (nonatomic, retain) CSGThreadsBlend* rightDownQuarterStitch;
//ThreeQuarter stitches
@property (nonatomic, retain) CSGThreadsBlend* leftUpThreeQuarterStitch;
@property (nonatomic, retain) CSGThreadsBlend* leftDownThreeQuarterStitch;
@property (nonatomic, retain) CSGThreadsBlend* rightUpThreeQuarterStitch;
@property (nonatomic, retain) CSGThreadsBlend* rightDownThreeQuarterStitch;
//HalfStitches
@property (nonatomic, retain) CSGThreadsBlend* slashHalfStitch;
@property (nonatomic, retain) CSGThreadsBlend* backslashHalfStitch;
//French knots
@property (nonatomic, retain) CSGThreadsBlend* frenchKnot00;
@property (nonatomic, retain) CSGThreadsBlend* frenchKnot01;
@property (nonatomic, retain) CSGThreadsBlend* frenchKnot02;
@property (nonatomic, retain) CSGThreadsBlend* frenchKnot10;
@property (nonatomic, retain) CSGThreadsBlend* frenchKnot11;
@property (nonatomic, retain) CSGThreadsBlend* frenchKnot12;
@property (nonatomic, retain) CSGThreadsBlend* frenchKnot20;
@property (nonatomic, retain) CSGThreadsBlend* frenchKnot21;
@property (nonatomic, retain) CSGThreadsBlend* frenchKnot22;

@end

@interface CSGDesignCell : NSObject<CSGDesignCell>

-(void) cleanup;

@end

@interface CSGDesignCell (Serialization)

- (size_t) serializedLength;
- (void) serializeWithBinaryEncoder: (CSGBinaryEncoder *) anEncoder ObjectsRegistry: (CSGObjectsRegistry*) registry;
+ (id) deserializeWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder ObjectsRegistry: (CSGObjectsRegistry*) registry;

@end
