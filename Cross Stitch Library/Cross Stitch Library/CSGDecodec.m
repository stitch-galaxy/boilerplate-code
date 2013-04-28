//
//  CSGDecodec.m
//  Cross Stitch Library
//
//  Created by 123 on 28.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGDecodec.h"

#import "CSGObjectsRegistry.h"
#import "CSGBinaryCoding.h"

#import "CSGThread.h"
#import "CSGThreadsBlend.h"
#import "CSGDesignCell.h"
#import "CSGDesignPoint.h"
#import "CSGDesignPoints.h"
#import "CSGBackStitch.h"
#import "CSGStraightStitch.h"

@interface CSGDecodec()

@property (nonatomic, retain) CSGBinaryDecoder* decoder;
@property (nonatomic, retain) CSGObjectsRegistry* registry;

@end

@implementation CSGDecodec

@synthesize decoder = anDecoder;//TODO: rename to decoder
@synthesize registry;

//initialization
-(id) initWithData: (NSData*) data
{
    if (self = [super init])
    {
        anDecoder = [[CSGBinaryDecoder alloc] initWithData:data];
    }
    return self;
}

-(void) deserilizeObjectsRegistry
{
    //TODO: implement when need
    registry = [[CSGObjectsRegistry alloc] init];
}

//deserialization
- (CSGThread*) deserializeThread
{
    [self deserilizeObjectsRegistry];
    return [self deserializeThreadImpl];
}

- (CSGThread*) deserializeThreadImpl
{
    const uint8_t *buf = [anDecoder readBytes:sizeof(uint8_t)];
    uint8_t iRed = *buf;
    
    buf = [anDecoder readBytes:sizeof(uint8_t)];
    uint8_t iGreen = *buf;
    
    buf = [anDecoder readBytes:sizeof(uint8_t)];
    uint8_t iBlue = *buf;
    
    CGFloat red = (CGFloat) iRed / 255.0;
    CGFloat green = (CGFloat) iGreen / 255.0;
    CGFloat blue = (CGFloat) iBlue / 255.0;
    UIColor* color = [[UIColor alloc] initWithRed: red green: green blue: blue alpha: 1.0];
    
    return [registry getThreadWithColor: color];
}


- (CSGThreadInBlend*) deserializeThreadInBlend
{
    [self deserilizeObjectsRegistry];
    return [self deserializeThreadInBlendImpl];
}

- (CSGThreadInBlend*) deserializeThreadInBlendImpl
{
    CSGThread *thread = [self deserializeThreadImpl];
    const uint8_t *buf1 = [anDecoder readBytes:sizeof(uint8_t)];
    uint8_t flossCount = *buf1;
    
    return [registry getThreadInBlendWithThread:thread FlossCount:flossCount];
}

- (CSGThreadsBlend*) deserializeThreadsBlend
{
    [self deserilizeObjectsRegistry];
    return [self deserializeThreadsBlendImpl];
}

- (CSGThreadsBlend*) deserializeThreadsBlendImpl
{
    const uint8_t *buf = [anDecoder readBytes:sizeof(uint8_t)];
    uint8_t length = *buf;
    
    NSMutableArray* threadsInBlend = [[NSMutableArray alloc] init];
    for(int i = 0; i < length; ++i)
    {
        CSGThreadInBlend* threadInBlend = [self deserializeThreadInBlendImpl];
        [threadsInBlend addObject:threadInBlend];
    }
    return [registry getThreadsBlendWithThreadsInBlend:threadsInBlend];
}

- (CSGDesignCell*) deserializeDesignCell
{
    [self deserilizeObjectsRegistry];
    return [self deserializeDesignCellImpl];
}

- (CSGDesignCell*) deserializeDesignCellImpl
{
    CSGDesignCell* aCell = [registry getDesignCellPrototype];
	const uint8_t *buf = [anDecoder readBytes:sizeof(uint8_t)];
	uint8_t stitchType = *buf;
	while (stitchType)
	{
		switch (stitchType) {
				//CROSS
			case CSG_STITCH_IN_CELL_CROSS:
				aCell.crossStitch = [self deserializeThreadsBlendImpl];
				break;
				//PETITE
			case CSG_STITCH_IN_CELL_LEFT_UP_PETITE:
				aCell.leftUpPetiteStitch = [self deserializeThreadsBlendImpl];
				break;
			case CSG_STITCH_IN_CELL_LEFT_DOWN_PETITE:
				aCell.leftDownPetiteStitch = [self deserializeThreadsBlendImpl];
				break;
			case CSG_STITCH_IN_CELL_RIGHT_UP_PETITE:
				aCell.rightUpPetiteStitch = [self deserializeThreadsBlendImpl];
				break;
			case CSG_STITCH_IN_CELL_RIGHT_DOWN_PETITE:
				aCell.rightDownPetiteStitch = [self deserializeThreadsBlendImpl];
				break;
				//QUARTER
			case CSG_STITCH_IN_CELL_LEFT_UP_QUARTER:
				aCell.leftUpQuarterStitch = [self deserializeThreadsBlendImpl];
				break;
			case CSG_STITCH_IN_CELL_LEFT_DOWN_QUARTER:
				aCell.leftDownQuarterStitch = [self deserializeThreadsBlendImpl];
				break;
			case CSG_STITCH_IN_CELL_RIGHT_UP_QUARTER:
				aCell.rightUpQuarterStitch = [self deserializeThreadsBlendImpl];
				break;
			case CSG_STITCH_IN_CELL_RIGHT_DOWN_QUARTER:
				aCell.rightDownQuarterStitch = [self deserializeThreadsBlendImpl];
				break;
				//THREE QUARTER
			case CSG_STITCH_IN_CELL_LEFT_UP_THREE_QUARTER:
				aCell.leftUpThreeQuarterStitch = [self deserializeThreadsBlendImpl];
				break;
			case CSG_STITCH_IN_CELL_LEFT_DOWN_THREE_QUARTER:
				aCell.leftDownThreeQuarterStitch = [self deserializeThreadsBlendImpl];
				break;
			case CSG_STITCH_IN_CELL_RIGHT_UP_THREE_QUARTER:
				aCell.rightUpThreeQuarterStitch = [self deserializeThreadsBlendImpl];
				break;
			case CSG_STITCH_IN_CELL_RIGHT_DOWN_THREE_QUARTER:
				aCell.rightDownThreeQuarterStitch = [self deserializeThreadsBlendImpl];
				break;
				//HALF_STITCHES
			case CSG_STITCH_IN_CELL_SLASH_HALF:
				aCell.slashHalfStitch = [self deserializeThreadsBlendImpl];
				break;
			case CSG_STITCH_IN_CELL_BACKSLASH_HALF:
				aCell.backslashHalfStitch = [self deserializeThreadsBlendImpl];
				break;
				//FRENCH KNOTS
			case CSG_STITCH_IN_CELL_FRENCH_KNOT_00:
				aCell.frenchKnot00 = [self deserializeThreadsBlendImpl];
				break;
			case CSG_STITCH_IN_CELL_FRENCH_KNOT_01:
				aCell.frenchKnot01 = [self deserializeThreadsBlendImpl];
				break;
			case CSG_STITCH_IN_CELL_FRENCH_KNOT_02:
				aCell.frenchKnot02 = [self deserializeThreadsBlendImpl];
				break;
			case CSG_STITCH_IN_CELL_FRENCH_KNOT_10:
				aCell.frenchKnot10 = [self deserializeThreadsBlendImpl];
				break;
			case CSG_STITCH_IN_CELL_FRENCH_KNOT_11:
				aCell.frenchKnot11 = [self deserializeThreadsBlendImpl];
				break;
			case CSG_STITCH_IN_CELL_FRENCH_KNOT_12:
				aCell.frenchKnot12 = [self deserializeThreadsBlendImpl];
				break;
			case CSG_STITCH_IN_CELL_FRENCH_KNOT_20:
				aCell.frenchKnot20 = [self deserializeThreadsBlendImpl];
				break;
			case CSG_STITCH_IN_CELL_FRENCH_KNOT_21:
				aCell.frenchKnot21 = [self deserializeThreadsBlendImpl];
				break;
			case CSG_STITCH_IN_CELL_FRENCH_KNOT_22:
				aCell.frenchKnot22 = [self deserializeThreadsBlendImpl];
				break;
			default:
				break;
		}
		
		//Read next stitch or terminal byte
		buf = [anDecoder readBytes:sizeof(uint8_t)];
		stitchType = *buf;
	}
	return [registry getDesignCellByPrototype:aCell];
}

- (CSGDesignPoint*) deserializeDesignPoint
{
    [self deserilizeObjectsRegistry];
    return [self deserializeDesignPointImpl];
}

- (CSGDesignPoint*) deserializeDesignPointImpl
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

- (CSGDesignPoints*) deserializeDesignPoints
{
    [self deserilizeObjectsRegistry];
    return [self deserializeDesignPointsImpl];
}

- (CSGDesignPoints*) deserializeDesignPointsImpl
{
    NSMutableArray *aPoints = [[NSMutableArray alloc] init];
    const uint32_t *buf = [anDecoder readBytes:sizeof(uint32_t)];
    uint32_t numPoints = *buf;
    for(uint32_t i = 0; i < numPoints; ++i)
    {
        CSGDesignPoint *coord = [self deserializeDesignPointImpl];
        [aPoints addObject:coord];
    }
    
    CSGDesignPoints* aRet = [[CSGDesignPoints alloc] initWithPoints:aPoints];
    return [registry getDesignPoints: aRet];
}

- (CSGBackStitch*) deserializeBackStitch
{
    [self deserilizeObjectsRegistry];
    return [self deserializeBackStitchImpl];
}

- (CSGBackStitch*) deserializeBackStitchImpl
{
    CSGThreadsBlend *aThreadsBlend = [self deserializeThreadsBlendImpl];
    CSGDesignPoints *aCurve = [self deserializeDesignPointsImpl];
    
    CSGBackStitch *aStitch = [[CSGBackStitch alloc] initWithThreadsBlend:aThreadsBlend Curve:aCurve];
    return [registry getBackStitch: aStitch];
}

- (CSGStraightStitch*) deserializeStraightStitch
{
    [self deserilizeObjectsRegistry];
    return [self deserializeStraightStitchImpl];
}

- (CSGStraightStitch*) deserializeStraightStitchImpl
{
    CSGThreadsBlend *aThreadsBlend = [self deserializeThreadsBlendImpl];
    CSGDesignPoints *aCurve = [self deserializeDesignPointsImpl];
    
    CSGStraightStitch *aStitch = [[CSGStraightStitch alloc] initWithThreadsBlend:aThreadsBlend Curve:aCurve];
    return [registry getStraightStitch: aStitch];
}

@end
