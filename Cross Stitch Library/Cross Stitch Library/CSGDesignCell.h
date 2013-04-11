//
//  CSGDesignCell.h
//  Cross Stitch Library
//
//  Created by 123 on 16.03.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "CSGStitchInCell.h"

@protocol CSGDesignCell <NSObject>

//Cross stitch
@property (nonatomic, retain) CSGStitchInCell* crossStitch;
//Petites
@property (nonatomic, retain) CSGStitchInCell* leftUpPetiteStitch;
@property (nonatomic, retain) CSGStitchInCell* leftDownPetiteStitch;
@property (nonatomic, retain) CSGStitchInCell* rightUpPetiteStitch;
@property (nonatomic, retain) CSGStitchInCell* rightDownPetiteStitch;
//Quarter stitches
@property (nonatomic, retain) CSGStitchInCell* leftUpQuarterStitch;
@property (nonatomic, retain) CSGStitchInCell* leftDownQuarterStitch;
@property (nonatomic, retain) CSGStitchInCell* rightUpQuarterStitch;
@property (nonatomic, retain) CSGStitchInCell* rightDownQuarterStitch;
//ThreeQuarter stitches
@property (nonatomic, retain) CSGStitchInCell* leftUpThreeQuarterStitch;
@property (nonatomic, retain) CSGStitchInCell* leftDownThreeQuarterStitch;
@property (nonatomic, retain) CSGStitchInCell* rightUpThreeQuarterStitch;
@property (nonatomic, retain) CSGStitchInCell* rightDownThreeQuarterStitch;
//HalfStitches
@property (nonatomic, retain) CSGStitchInCell* slashHalfStitch;
@property (nonatomic, retain) CSGStitchInCell* backslashHalfStitch;
//French knots
@property (nonatomic, retain) CSGStitchInCell* frenchKnot00;
@property (nonatomic, retain) CSGStitchInCell* frenchKnot01;
@property (nonatomic, retain) CSGStitchInCell* frenchKnot02;
@property (nonatomic, retain) CSGStitchInCell* frenchKnot10;
@property (nonatomic, retain) CSGStitchInCell* frenchKnot11;
@property (nonatomic, retain) CSGStitchInCell* frenchKnot12;
@property (nonatomic, retain) CSGStitchInCell* frenchKnot20;
@property (nonatomic, retain) CSGStitchInCell* frenchKnot21;
@property (nonatomic, retain) CSGStitchInCell* frenchKnot22;

@end

@interface CSGDesignCell : NSObject<CSGDesignCell>

@end

@interface CSGDesignCell (Serialization)

- (size_t) serializedLength;
- (void) serializeWithBinaryEncoder: (CSGBinaryEncoder *) anEncoder;
+ (id) deserializeWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder ObjectsRegistry: (CSGObjectsRegistry*) registry;

@end
