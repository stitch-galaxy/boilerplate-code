//
//  Cross_Stitch_LibraryTests.m
//  Cross Stitch LibraryTests
//
//  Created by 123 on 17.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <stdlib.h>

#import "CSGSerializationTests.h"

#import "CSGThread.h"
#import "CSGThreadsPalette.h"
#import "CSGThreadsBlend.h"
#import "CSGBinaryCoding.h"
#import "CSGStitchInCell.h"
#import "CSGDesignCell.h"
#import "CSGDesignPoint.h"
#import "CSGDesignPoints.h"
#import "CSGBackStitch.h"
#import "CSGStraightStitch.h"

#define CSG_TEST_THREAD_COLORS_PALETTE_LENGTH 10

@interface CSGSerializationTestHelper : NSObject
{
    NSMutableArray *threads;
}

- (id) init;

@end


@implementation CSGSerializationTestHelper

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
    return [CSGSerializationTestHelper randomIndexFor:2];
}

#define CSG_TEST_DESIGN_SIZE 100
#define CSG_TEST_DESIGN_CELL_GRANULARITY 2

- (CSGDesignPoint*) randomDesignCoordinate
{
    CSGDesignPoint *coordinate = [[CSGDesignPoint alloc] initWithX:[CSGSerializationTestHelper randomIndexFor: CSG_TEST_DESIGN_SIZE] Y:[CSGSerializationTestHelper randomIndexFor: CSG_TEST_DESIGN_SIZE] CellX:[CSGSerializationTestHelper randomIndexFor: CSG_TEST_DESIGN_CELL_GRANULARITY] CellY:[CSGSerializationTestHelper randomIndexFor: CSG_TEST_DESIGN_CELL_GRANULARITY] CellDenominator:CSG_TEST_DESIGN_CELL_GRANULARITY];
    return coordinate;
}

#define MAX_DESIGN_POINTS 10

-(CSGBackStitch*) randomBackStitch
{
    CSGThreadsBlend* blend = [self randomThreadsBlend];
    CSGDesignPoints* curve = [self randomDesignPoints];
    return [[CSGBackStitch alloc] initWithThreadsBlend:blend Curve:curve];
}

-(CSGStraightStitch*) randomStraightStitch
{
    CSGThreadsBlend* blend = [self randomThreadsBlend];
    CSGDesignPoints* curve = [self randomDesignPoints];
    return [[CSGStraightStitch alloc] initWithThreadsBlend:blend Curve:curve];
}

-(CSGDesignPoints*) randomDesignPoints
{
    NSMutableArray* points = [[NSMutableArray alloc] init];
    uint32_t numPoints = [CSGSerializationTestHelper randomIndexFor: MAX_DESIGN_POINTS];
    for(int i = 0; i < numPoints; ++i)
    {
        [points addObject:[self randomDesignCoordinate]];
    }
    return [[CSGDesignPoints alloc] initWithPoints:points];
}

- (CSGDesignCell*) randomDesignCell
{
    CSGDesignCell* designCell = [[CSGDesignCell alloc] init];
    //Cross
    if (CSGSerializationTestHelper.randomBool)
    {
        designCell.crossStitch = self.randomStitchInCell;
    }
    //Petites
    if (CSGSerializationTestHelper.randomBool)
    {
        designCell.leftUpPetiteStitch = self.randomStitchInCell;
    }
    if (CSGSerializationTestHelper.randomBool)
    {
        designCell.leftDownPetiteStitch = self.randomStitchInCell;
    }
    if (CSGSerializationTestHelper.randomBool)
    {
        designCell.rightUpPetiteStitch = self.randomStitchInCell;
    }
    if (CSGSerializationTestHelper.randomBool)
    {
        designCell.rightDownPetiteStitch = self.randomStitchInCell;
    }
    //Quarter stitches
    if (CSGSerializationTestHelper.randomBool)
    {
        designCell.leftUpQuarterStitch = self.randomStitchInCell;
    }
    if (CSGSerializationTestHelper.randomBool)
    {
        designCell.leftDownQuarterStitch = self.randomStitchInCell;
    }
    if (CSGSerializationTestHelper.randomBool)
    {
        designCell.rightUpQuarterStitch = self.randomStitchInCell;
    }
    if (CSGSerializationTestHelper.randomBool)
    {
        designCell.rightDownQuarterStitch = self.randomStitchInCell;
    }
    //ThreeQuarter stitches
    if (CSGSerializationTestHelper.randomBool)
    {
        designCell.leftUpThreeQuarterStitch = self.randomStitchInCell;
    }
    if (CSGSerializationTestHelper.randomBool)
    {
        designCell.leftDownThreeQuarterStitch = self.randomStitchInCell;
    }
    if (CSGSerializationTestHelper.randomBool)
    {
        designCell.rightUpThreeQuarterStitch = self.randomStitchInCell;
    }
    if (CSGSerializationTestHelper.randomBool)
    {
        designCell.rightDownThreeQuarterStitch = self.randomStitchInCell;
    }
    //HalfStitches
    if (CSGSerializationTestHelper.randomBool)
    {
        designCell.slashHalfStitch = self.randomStitchInCell;
    }
    if (CSGSerializationTestHelper.randomBool)
    {
        designCell.backslashHalfStitch = self.randomStitchInCell;
    }
    //French knots
    if (CSGSerializationTestHelper.randomBool)
    {
        designCell.frenchKnot00 = self.randomStitchInCell;
    }
    if (CSGSerializationTestHelper.randomBool)
    {
        designCell.frenchKnot01 = self.randomStitchInCell;
    }
    if (CSGSerializationTestHelper.randomBool)
    {
        designCell.frenchKnot02 = self.randomStitchInCell;
    }
    if (CSGSerializationTestHelper.randomBool)
    {
        designCell.frenchKnot10 = self.randomStitchInCell;
    }
    if (CSGSerializationTestHelper.randomBool)
    {
        designCell.frenchKnot11 = self.randomStitchInCell;
    }
    if (CSGSerializationTestHelper.randomBool)
    {
        designCell.frenchKnot12 = self.randomStitchInCell;
    }
    if (CSGSerializationTestHelper.randomBool)
    {
        designCell.frenchKnot20 = self.randomStitchInCell;
    }
    if (CSGSerializationTestHelper.randomBool)
    {
        designCell.frenchKnot21 = self.randomStitchInCell;
    }
    if (CSGSerializationTestHelper.randomBool)
    {
        designCell.frenchKnot22 = self.randomStitchInCell;
    }
    
    return designCell;
}

- (CSGStitchInCell*) randomStitchInCell
{
    return [[CSGStitchInCell alloc] initWithThreadsBlend:self.randomThreadsBlend];
}

- (CSGThread*) randomThread
{
    return [threads objectAtIndex:[CSGSerializationTestHelper randomIndexFor:CSG_TEST_THREAD_COLORS_PALETTE_LENGTH]];    
}

#define CSG_MAX_FLOSSES_OF_THREAD 4

- (CSGThreadInBlend*) randomThreadInBlend
{
    uint32_t flossesCount = [CSGSerializationTestHelper randomIndexFor: CSG_MAX_FLOSSES_OF_THREAD];
    CSGThread* thread = self.randomThread;
    CSGThreadInBlend *threadInBlend = [[CSGThreadInBlend alloc] initWithThread:thread FlossCount: flossesCount];
    return threadInBlend;

}

#define CSG_MAX_THREADS_IN_BLEND 4

- (CSGThreadsBlend*) randomThreadsBlend
{
    NSMutableArray *aThreads = [[NSMutableArray alloc] init];
    
    int threadsCount = [CSGSerializationTestHelper randomIndexFor: CSG_MAX_THREADS_IN_BLEND] + 1;
    for (int i = 0; i < threadsCount; ++i)
    {
        CSGThreadInBlend* threadInBlend = self.randomThreadInBlend;
        [aThreads addObject:threadInBlend];
    }
    CSGThreadsBlend* blend = [[CSGThreadsBlend alloc] initWithThreadsInBlend: aThreads];
    return blend;
}

- (id) init
{
    if (self = [super init])
    {
        threads = [[NSMutableArray alloc] init];
        
        for(int i =0; i < CSG_TEST_THREAD_COLORS_PALETTE_LENGTH; ++i)
        {
            CGFloat red = [CSGSerializationTestHelper randomColorComponent];
            CGFloat green = [CSGSerializationTestHelper randomColorComponent];
            CGFloat blue = [CSGSerializationTestHelper randomColorComponent];
            
            UIColor *color = [[UIColor alloc] initWithRed:red green:green blue:blue alpha:1.0];
            
            [threads addObject: [[CSGThread alloc] initWithColor: color]];
        }
    }
    return self;
}

@end


@implementation CSGSerializationTests
{
    CSGSerializationTestHelper* testhelper;
}

- (void)setUp
{
    [super setUp];
    testhelper = [[CSGSerializationTestHelper alloc] init];
}

- (void)tearDown
{
    [super tearDown];
}

-(void) testThreadsBlendSerializationAndEquality
{
    CSGThreadsBlend* blend = testhelper.randomThreadsBlend;
    
    CSGBinaryEncoder* anEncoder = [[CSGBinaryEncoder alloc] initWithLength:blend.serializedLength];
    [blend serializeWithBinaryEncoder:anEncoder];
    
    CSGBinaryDecoder* anDecoder = [[CSGBinaryDecoder alloc] initWithData:anEncoder.data];
    
    CSGObjectsRegistry *registry = [[CSGObjectsRegistry alloc] init];
    CSGThreadsBlend* blend1 = [CSGThreadsBlend deserializeWithBinaryDecoder:anDecoder ObjectsRegistry:registry];
    
    if (blend.hash != blend1.hash || ![blend isEqual:blend1])
    {
        STFail(@"ThreadsBlend serialization and equality");
    }
}

- (void) testThreadInBlendSerializationAndEquality
{
    CSGThreadInBlend* thread = testhelper.randomThreadInBlend;
    
    CSGBinaryEncoder* anEncoder = [[CSGBinaryEncoder alloc] initWithLength:thread.serializedLength];
    [thread serializeWithBinaryEncoder:anEncoder];
    
    
    CSGObjectsRegistry *registry = [[CSGObjectsRegistry alloc] init];
    CSGBinaryDecoder* anDecoder = [[CSGBinaryDecoder alloc] initWithData:anEncoder.data];
    
    CSGThreadInBlend* thread1 = [CSGThreadInBlend deserializeWithBinaryDecoder:anDecoder ObjectsRegistry:registry];

    if (thread.hash != thread1.hash || ![thread isEqual:thread1])
    {
        STFail(@"ThreadInBlend serialization and equality");
    }
}

- (void) testThreadSerializationAndEquality
{
    CSGThread* thread = testhelper.randomThread;
    
    CSGBinaryEncoder* anEncoder = [[CSGBinaryEncoder alloc] initWithLength:thread.serializedLength];
    [thread serializeWithBinaryEncoder:anEncoder];
    
    CSGObjectsRegistry *registry = [[CSGObjectsRegistry alloc] init];
    CSGBinaryDecoder* anDecoder = [[CSGBinaryDecoder alloc] initWithData:anEncoder.data];
    
    CSGThread* thread1 = [CSGThread deserializeWithBinaryDecoder:anDecoder ObjectsRegistry:registry];
    
    if (thread.hash != thread1.hash || ![thread isEqual:thread1])
    {
        STFail(@"Thread serialization and equality");
    }
}

+ (BOOL) IsSerializedViewEqualForData: (NSData*) data1 Data1: (NSData*) data2
{
    if (data1.length != data2.length)
    {
        return NO;
    }
    
    NSUInteger length = data1.length;
    
    return (memcmp(data1.bytes, data2.bytes, length) == 0);
}

-(void) testStitchInCellSerialization
{
    CSGStitchInCell* stitch = testhelper.randomStitchInCell;
    
    CSGBinaryEncoder* anEncoder = [[CSGBinaryEncoder alloc] initWithLength:stitch.serializedLength];
    [stitch serializeWithBinaryEncoder:anEncoder];
    
    CSGObjectsRegistry *registry = [[CSGObjectsRegistry alloc] init];
    CSGBinaryDecoder* anDecoder = [[CSGBinaryDecoder alloc] initWithData:anEncoder.data];
    
    CSGStitchInCell* stitch1 = [CSGStitchInCell deserializeWithBinaryDecoder:anDecoder ObjectsRegistry:registry];
    
    if (stitch.hash != stitch.hash || ![stitch isEqual:stitch1])
    {
        STFail(@"StitchInCell serialization and equality");
    }
}


-(void) testDesignCellSerialization
{
    CSGDesignCell* cell = testhelper.randomDesignCell;
    
    CSGBinaryEncoder* anEncoder = [[CSGBinaryEncoder alloc] initWithLength:cell.serializedLength];
    [cell serializeWithBinaryEncoder:anEncoder];
    
    CSGObjectsRegistry *registry = [[CSGObjectsRegistry alloc] init];
    CSGBinaryDecoder* anDecoder = [[CSGBinaryDecoder alloc] initWithData:anEncoder.data];
    
    CSGDesignCell* cell1 = [CSGDesignCell deserializeWithBinaryDecoder:anDecoder ObjectsRegistry:registry];
    
    if (cell.hash != cell1.hash || ![cell isEqual:cell1])
    {
        STFail(@"Design cell serialization and equality");
    }
}

- (void) testDesignCellCoordinateSerialization
{
    CSGDesignPoint *coordinate = testhelper.randomDesignCoordinate;
    
    CSGBinaryEncoder* anEncoder = [[CSGBinaryEncoder alloc] initWithLength:coordinate.serializedLength];
    [coordinate serializeWithBinaryEncoder:anEncoder];
    
    CSGObjectsRegistry *registry = [[CSGObjectsRegistry alloc] init];
    CSGBinaryDecoder* anDecoder = [[CSGBinaryDecoder alloc] initWithData:anEncoder.data];
    
    CSGDesignPoint* coordinate1 = [CSGDesignPoint deserializeWithBinaryDecoder:anDecoder ObjectsRegistry:registry];    
    if (coordinate.hash != coordinate1.hash || ![coordinate isEqual:coordinate1])
    {
        STFail(@"DesignCoordinate serialization and equality");
    }

}

-(void) testDesignPointsSerialization
{
    CSGDesignPoints *points = testhelper.randomDesignPoints;
    
    CSGBinaryEncoder* anEncoder = [[CSGBinaryEncoder alloc] initWithLength:points.serializedLength];
    [points serializeWithBinaryEncoder:anEncoder];
    
    CSGObjectsRegistry *registry = [[CSGObjectsRegistry alloc] init];
    CSGBinaryDecoder* anDecoder = [[CSGBinaryDecoder alloc] initWithData:anEncoder.data];
    
    CSGDesignPoints* points1 = [CSGDesignPoints deserializeWithBinaryDecoder:anDecoder ObjectsRegistry:registry];    
    if (points.hash != points1.hash || ![points isEqual:points1])
    {
        STFail(@"DesignCoordinate serialization and equality");
    }

}


-(void) testBackStitchSerialization
{
    CSGBackStitch* stitch = testhelper.randomBackStitch;
    
    CSGBinaryEncoder* anEncoder = [[CSGBinaryEncoder alloc] initWithLength:stitch.serializedLength];
    [stitch serializeWithBinaryEncoder:anEncoder];
    
    CSGObjectsRegistry *registry = [[CSGObjectsRegistry alloc] init];
    CSGBinaryDecoder* anDecoder = [[CSGBinaryDecoder alloc] initWithData:anEncoder.data];
    
    CSGBackStitch* stitch1 = [CSGBackStitch deserializeWithBinaryDecoder:anDecoder ObjectsRegistry:registry];
    
    if (stitch.hash != stitch1.hash || ![stitch isEqual:stitch1])
    {
        STFail(@"BackStitch serialization and equality");
    }
}


-(void) testStraightStitchSerialization
{
    CSGStraightStitch* stitch = testhelper.randomStraightStitch;
    
    CSGBinaryEncoder* anEncoder = [[CSGBinaryEncoder alloc] initWithLength:stitch.serializedLength];
    [stitch serializeWithBinaryEncoder:anEncoder];
    
    CSGObjectsRegistry *registry = [[CSGObjectsRegistry alloc] init];
    CSGBinaryDecoder* anDecoder = [[CSGBinaryDecoder alloc] initWithData:anEncoder.data];
    
    CSGStraightStitch* stitch1 = [CSGStraightStitch deserializeWithBinaryDecoder:anDecoder ObjectsRegistry:registry];
    
    if (stitch.hash != stitch1.hash || ![stitch isEqual:stitch1])
    {
        STFail(@"StraightStitch serialization and equality");
    }
}



@end