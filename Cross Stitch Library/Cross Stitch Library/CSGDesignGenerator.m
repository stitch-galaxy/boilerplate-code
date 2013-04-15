//
//  CSGDesignGenerator.m
//  Cross Stitch Library
//
//  Created by 123 on 15.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGDesignGenerator.h"

#define DESIGN_COLORS_NUMBER 50
#define DESIGN_WIDTH 100
#define DESIGN_STRAIGTH_STITCHES 2
#define DESIGN_BACK_STITCHES 2
#define DESIGN_CELL_GRANULARITY 2
#define DESIGN_POINTS_MAX_NUMBER 10
#define DESIGN_MAX_FLOSSES_OF_THREAD 4
#define DESIGN_THREADS_IN_BLEND_MAX_NUMBER 4

@implementation CSGDesignGenerator

+ (CGFloat) randomColorComponent
{
    return ((CGFloat)(arc4random() % 265)) / 255.0;
}

+ (uint32_t) randomIndexFor: (uint32_t) seed
{
    return arc4random() % seed;
}

+ (BOOL) randomBool
{
    return [CSGDesignGenerator randomIndexFor:2];
}

- (id) init
{
    if (self = [super init])
    {
        threads = [[NSMutableArray alloc] init];
        
        for(int i =0; i < DESIGN_COLORS_NUMBER; ++i)
        {
            CGFloat red = [CSGDesignGenerator randomColorComponent];
            CGFloat green = [CSGDesignGenerator randomColorComponent];
            CGFloat blue = [CSGDesignGenerator randomColorComponent];
            
            UIColor *color = [[UIColor alloc] initWithRed:red green:green blue:blue alpha:1.0];
            
            [threads addObject: [[CSGThread alloc] initWithColor: color]];
        }
    }
    return self;
}

- (CSGThread*) randomThread
{
    return [threads objectAtIndex:[CSGDesignGenerator randomIndexFor:DESIGN_COLORS_NUMBER]];
}

- (CSGThreadInBlend*) randomThreadInBlend
{
    uint32_t flossesCount = [CSGDesignGenerator randomIndexFor: DESIGN_MAX_FLOSSES_OF_THREAD];
    CSGThread* thread = self.randomThread;
    CSGThreadInBlend *threadInBlend = [[CSGThreadInBlend alloc] initWithThread:thread FlossCount: flossesCount];
    return threadInBlend;
}

- (CSGThreadsBlend*) randomThreadsBlend
{
    NSMutableArray *aThreads = [[NSMutableArray alloc] init];
    
    int threadsCount = [CSGDesignGenerator randomIndexFor: DESIGN_THREADS_IN_BLEND_MAX_NUMBER] + 1;
    for (int i = 0; i < threadsCount; ++i)
    {
        CSGThreadInBlend* threadInBlend = self.randomThreadInBlend;
        [aThreads addObject:threadInBlend];
    }
    CSGThreadsBlend* blend = [[CSGThreadsBlend alloc] initWithThreadsInBlend: aThreads];
    return blend;
}


- (CSGStitchInCell*) randomStitchInCell
{
    return [[CSGStitchInCell alloc] initWithThreadsBlend:self.randomThreadsBlend];
}

- (CSGDesignCell*) randomDesignCell
{
    CSGDesignCell* designCell = [[CSGDesignCell alloc] init];
    //Cross
    if (CSGDesignGenerator.randomBool)
    {
        designCell.crossStitch = self.randomStitchInCell;
    }
    //Petites
    if (CSGDesignGenerator.randomBool)
    {
        designCell.leftUpPetiteStitch = self.randomStitchInCell;
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.leftDownPetiteStitch = self.randomStitchInCell;
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.rightUpPetiteStitch = self.randomStitchInCell;
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.rightDownPetiteStitch = self.randomStitchInCell;
    }
    //Quarter stitches
    if (CSGDesignGenerator.randomBool)
    {
        designCell.leftUpQuarterStitch = self.randomStitchInCell;
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.leftDownQuarterStitch = self.randomStitchInCell;
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.rightUpQuarterStitch = self.randomStitchInCell;
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.rightDownQuarterStitch = self.randomStitchInCell;
    }
    //ThreeQuarter stitches
    if (CSGDesignGenerator.randomBool)
    {
        designCell.leftUpThreeQuarterStitch = self.randomStitchInCell;
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.leftDownThreeQuarterStitch = self.randomStitchInCell;
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.rightUpThreeQuarterStitch = self.randomStitchInCell;
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.rightDownThreeQuarterStitch = self.randomStitchInCell;
    }
    //HalfStitches
    if (CSGDesignGenerator.randomBool)
    {
        designCell.slashHalfStitch = self.randomStitchInCell;
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.backslashHalfStitch = self.randomStitchInCell;
    }
    //French knots
    if (CSGDesignGenerator.randomBool)
    {
        designCell.frenchKnot00 = self.randomStitchInCell;
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.frenchKnot01 = self.randomStitchInCell;
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.frenchKnot02 = self.randomStitchInCell;
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.frenchKnot10 = self.randomStitchInCell;
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.frenchKnot11 = self.randomStitchInCell;
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.frenchKnot12 = self.randomStitchInCell;
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.frenchKnot20 = self.randomStitchInCell;
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.frenchKnot21 = self.randomStitchInCell;
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.frenchKnot22 = self.randomStitchInCell;
    }
    
    return designCell;
}

- (CSGDesignPoint*) randomDesignCoordinate
{
    CSGDesignPoint *coordinate = [[CSGDesignPoint alloc] initWithX:[CSGDesignGenerator randomIndexFor: DESIGN_WIDTH] Y:[CSGDesignGenerator randomIndexFor: DESIGN_WIDTH] CellX:[CSGDesignGenerator randomIndexFor: DESIGN_CELL_GRANULARITY] CellY:[CSGDesignGenerator randomIndexFor: DESIGN_CELL_GRANULARITY] CellDenominator:DESIGN_CELL_GRANULARITY];
    return coordinate;
}

-(CSGDesignPoints*) randomDesignPoints
{
    NSMutableArray* points = [[NSMutableArray alloc] init];
    uint32_t numPoints = [CSGDesignGenerator randomIndexFor: DESIGN_POINTS_MAX_NUMBER];
    for(int i = 0; i < numPoints; ++i)
    {
        [points addObject:[self randomDesignCoordinate]];
    }
    return [[CSGDesignPoints alloc] initWithPoints:points];
}

- (CSGBackStitch*) randomBackStitch
{
    CSGThreadsBlend* blend = [self randomThreadsBlend];
    CSGDesignPoints* curve = [self randomDesignPoints];
    return [[CSGBackStitch alloc] initWithThreadsBlend:blend Curve:curve];
}

- (CSGStraightStitch*) randomStraightStitch
{
    CSGThreadsBlend* blend = [self randomThreadsBlend];
    CSGDesignPoints* curve = [self randomDesignPoints];
    return [[CSGStraightStitch alloc] initWithThreadsBlend:blend Curve:curve];
}

- (CSGDesign*) randomDesign
{
    NSMutableArray *aBackStitches = [[NSMutableArray alloc] init];
    for(uint32_t i = 0; i < DESIGN_BACK_STITCHES; ++i)
    {
        [aBackStitches addObject: [self randomBackStitch]];
    }
    NSMutableArray *aStraightStitches = [[NSMutableArray alloc] init];
    for(uint32_t i = 0; i < DESIGN_STRAIGTH_STITCHES; ++i)
    {
        [aStraightStitches addObject: [self randomStraightStitch]];
    }
    NSMutableArray *aCells = [[NSMutableArray alloc] init];
    for(uint32_t i = 0; i < DESIGN_WIDTH * DESIGN_WIDTH; ++i)
    {
        [aCells addObject: [self randomDesignCell]];
    }
    CSGDesign* design = [[CSGDesign alloc] initWithWidth:DESIGN_WIDTH Height:DESIGN_WIDTH Cells:aCells BackStitches:aBackStitches StraightStitches:aStraightStitches];
    return design;
}

@end
