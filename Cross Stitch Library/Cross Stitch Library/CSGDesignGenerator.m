//
//  CSGDesignGenerator.m
//  Cross Stitch Library
//
//  Created by 123 on 15.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGDesignGenerator.h"

#define DESIGN_COLORS_NUMBER 50
#define DESIGN_MAX_FLOSSES_OF_THREAD 2
#define DESIGN_THREADS_IN_BLEND_MAX_NUMBER 2
#define DESIGN_WIDTH 200
#define DESIGN_HEIGHT 200
#define DESIGN_CELL_GRANULARITY 2


#define DESIGN_STRAIGTH_STITCHES 2
#define DESIGN_BACK_STITCHES 2
#define DESIGN_POINTS_MAX_NUMBER 100



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
            
            [threads addObject: color];
        }
    }
    return self;
}

- (CSGThread*) generateThread: (CSGObjectsRegistry*) registry;
{
    return [registry getThreadWithColor:[threads objectAtIndex:[CSGDesignGenerator randomIndexFor:DESIGN_COLORS_NUMBER]]];
}

- (CSGThreadInBlend*) generateThreadInBlend: (CSGObjectsRegistry*) registry;
{
    uint32_t flossesCount = [CSGDesignGenerator randomIndexFor: DESIGN_MAX_FLOSSES_OF_THREAD] + 1;
    CSGThread* thread = [self generateThread: registry];
    CSGThreadInBlend *threadInBlend = [registry getThreadInBlendWithThread:thread FlossCount:flossesCount];
    return threadInBlend;
}

- (CSGThreadsBlend*) generateThreadsBlend: (CSGObjectsRegistry*) registry;
{
    NSMutableArray *aThreads = [[NSMutableArray alloc] init];
    
    int threadsCount = [CSGDesignGenerator randomIndexFor: DESIGN_THREADS_IN_BLEND_MAX_NUMBER] + 1;
    for (int i = 0; i < threadsCount; ++i)
    {
        CSGThreadInBlend* threadInBlend = [self generateThreadInBlend: registry];
        [aThreads addObject:threadInBlend];
    }
    return [registry getThreadsBlendWithThreadsInBlend:aThreads];
}


- (CSGStitchInCell*) generateStitchInCell: (CSGObjectsRegistry*) registry;
{
    return [[CSGStitchInCell alloc] initWithThreadsBlend:[self generateThreadsBlend: registry]];
}

- (CSGDesignCell*) generateDesignCell: (CSGObjectsRegistry*) registry;
{
    CSGDesignCell* designCell = [[CSGDesignCell alloc] init];
    //Cross
    if (CSGDesignGenerator.randomBool)
    {
        designCell.crossStitch = [self generateStitchInCell: registry];
    }
    //Petites
    if (CSGDesignGenerator.randomBool)
    {
        designCell.leftUpPetiteStitch = [self generateStitchInCell: registry];
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.leftDownPetiteStitch = [self generateStitchInCell: registry];
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.rightUpPetiteStitch = [self generateStitchInCell: registry];
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.rightDownPetiteStitch = [self generateStitchInCell: registry];
    }
    //Quarter stitches
    if (CSGDesignGenerator.randomBool)
    {
        designCell.leftUpQuarterStitch = [self generateStitchInCell: registry];
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.leftDownQuarterStitch = [self generateStitchInCell: registry];
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.rightUpQuarterStitch = [self generateStitchInCell: registry];
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.rightDownQuarterStitch = [self generateStitchInCell: registry];
    }
    //ThreeQuarter stitches
    if (CSGDesignGenerator.randomBool)
    {
        designCell.leftUpThreeQuarterStitch = [self generateStitchInCell: registry];
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.leftDownThreeQuarterStitch = [self generateStitchInCell: registry];
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.rightUpThreeQuarterStitch = [self generateStitchInCell: registry];
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.rightDownThreeQuarterStitch = [self generateStitchInCell: registry];
    }
    //HalfStitches
    if (CSGDesignGenerator.randomBool)
    {
        designCell.slashHalfStitch = [self generateStitchInCell: registry];
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.backslashHalfStitch = [self generateStitchInCell: registry];
    }
    //French knots
    if (CSGDesignGenerator.randomBool)
    {
        designCell.frenchKnot00 = [self generateStitchInCell: registry];
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.frenchKnot01 = [self generateStitchInCell: registry];
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.frenchKnot02 = [self generateStitchInCell: registry];
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.frenchKnot10 = [self generateStitchInCell: registry];
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.frenchKnot11 = [self generateStitchInCell: registry];
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.frenchKnot12 = [self generateStitchInCell: registry];
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.frenchKnot20 = [self generateStitchInCell: registry];
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.frenchKnot21 = [self generateStitchInCell: registry];
    }
    if (CSGDesignGenerator.randomBool)
    {
        designCell.frenchKnot22 = [self generateStitchInCell: registry];
    }
    
    return designCell;
}

- (CSGDesignPoint*) generateDesignCoordinate: (CSGObjectsRegistry*) registry;
{
    CSGDesignPoint *coordinate = [[CSGDesignPoint alloc] initWithX:[CSGDesignGenerator randomIndexFor: DESIGN_WIDTH] Y:[CSGDesignGenerator randomIndexFor: DESIGN_HEIGHT] CellX:[CSGDesignGenerator randomIndexFor: DESIGN_CELL_GRANULARITY] CellY:[CSGDesignGenerator randomIndexFor: DESIGN_CELL_GRANULARITY] CellDenominator:DESIGN_CELL_GRANULARITY];
    return coordinate;
}

-(CSGDesignPoints*) generateDesignPoints: (CSGObjectsRegistry*) registry;
{
    NSMutableArray* points = [[NSMutableArray alloc] init];
    uint32_t numPoints = [CSGDesignGenerator randomIndexFor: DESIGN_POINTS_MAX_NUMBER] + 1;
    for(int i = 0; i < numPoints; ++i)
    {
        [points addObject:[self generateDesignCoordinate: registry]];
    }
    return [[CSGDesignPoints alloc] initWithPoints:points];
}

- (CSGBackStitch*) generateBackStitch: (CSGObjectsRegistry*) registry;
{
    CSGThreadsBlend* blend = [self generateThreadsBlend:registry];
    CSGDesignPoints* curve = [self generateDesignPoints:registry];
    return [[CSGBackStitch alloc] initWithThreadsBlend:blend Curve:curve];
}

- (CSGStraightStitch*) generateStraightStitch: (CSGObjectsRegistry*) registry;
{
    CSGThreadsBlend* blend = [self generateThreadsBlend:registry];
    CSGDesignPoints* curve = [self generateDesignPoints:registry];
    return [[CSGStraightStitch alloc] initWithThreadsBlend:blend Curve:curve];
}

- (CSGDesign*) generateDesign: (CSGObjectsRegistry*) registry;
{
    NSMutableArray *aBackStitches = [[NSMutableArray alloc] init];
    for(uint32_t i = 0; i < DESIGN_BACK_STITCHES; ++i)
    {
        [aBackStitches addObject: [self generateBackStitch:registry]];
    }
    NSMutableArray *aStraightStitches = [[NSMutableArray alloc] init];
    for(uint32_t i = 0; i < DESIGN_STRAIGTH_STITCHES; ++i)
    {
        [aStraightStitches addObject: [self generateStraightStitch:registry]];
    }
    NSMutableArray *aCells = [[NSMutableArray alloc] init];
    for(uint32_t i = 0; i < DESIGN_WIDTH * DESIGN_HEIGHT; ++i)
    {
        [aCells addObject: [self generateDesignCell:registry]];
    }
    CSGDesign* design = [[CSGDesign alloc] initWithWidth:DESIGN_WIDTH Height:DESIGN_HEIGHT Cells:aCells BackStitches:aBackStitches StraightStitches:aStraightStitches];
    return design;
}

@end
