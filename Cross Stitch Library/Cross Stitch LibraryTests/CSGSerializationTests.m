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
#import "CSGDesignCoordinate.h"

#define CSG_TEST_THREAD_COLORS_PALETTE_LENGTH 10

@interface CSGSerializationTestHelper : NSObject
{
    CSGThreadsPalette* threadsPalette;
}

- (id) init;

@end


@implementation CSGSerializationTestHelper

- (CSGThreadsPalette*) threadsPalette
{
    return threadsPalette;
}

+ (CGFloat) randomColorComponent
{
    return ((CGFloat)(arc4random() % 265)) / 255.0;
}

+ (uint32_t) randomIndexFor: (uint32_t) seed
{
    return arc4random() % seed;
}

- (UIColor*) randomThreadColor
{
    return [[threadsPalette threadAtIndex: [CSGSerializationTestHelper randomIndexFor:[threadsPalette size]]] color];
}

+ (BOOL) randomBool
{
    return [CSGSerializationTestHelper randomIndexFor:2];
}

#define CSG_TEST_DESIGN_SIZE 100
#define CSG_TEST_DESIGN_CELL_GRANULARITY 2

- (CSGDesignCoordinate*) randomDesignCoordinate
{
    CSGDesignCoordinate *coordinate = [[CSGDesignCoordinate alloc] initWithX:[CSGSerializationTestHelper randomIndexFor: CSG_TEST_DESIGN_SIZE] Y:[CSGSerializationTestHelper randomIndexFor: CSG_TEST_DESIGN_SIZE] CellX:[CSGSerializationTestHelper randomIndexFor: CSG_TEST_DESIGN_CELL_GRANULARITY] CellY:[CSGSerializationTestHelper randomIndexFor: CSG_TEST_DESIGN_CELL_GRANULARITY] CellDenominator:CSG_TEST_DESIGN_CELL_GRANULARITY];
    return coordinate;
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
    return [threadsPalette threadMaterialByColor: self.randomThreadColor];
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
    NSMutableArray *threads = [[NSMutableArray alloc] init];
    
    int threadsCount = [CSGSerializationTestHelper randomIndexFor: CSG_MAX_THREADS_IN_BLEND] + 1;
    for (int i = 0; i < threadsCount; ++i)
    {
        CSGThreadInBlend* threadInBlend = self.randomThreadInBlend;
        [threads addObject:threadInBlend];
    }
    CSGThreadsBlend* blend = [[CSGThreadsBlend alloc] initWithThreadsInBlend: threads];
    return blend;
}

- (id) init
{
    if (self = [super init])
    {
        threadsPalette = [[CSGThreadsPalette alloc] init];
        
        for(int i =0; i < CSG_TEST_THREAD_COLORS_PALETTE_LENGTH; ++i)
        {
            CGFloat red = [CSGSerializationTestHelper randomColorComponent];
            CGFloat green = [CSGSerializationTestHelper randomColorComponent];
            CGFloat blue = [CSGSerializationTestHelper randomColorComponent];
            
            UIColor *color = [[UIColor alloc] initWithRed:red green:green blue:blue alpha:1.0];
            
            [threadsPalette threadMaterialByColor:color];
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
    [blend serializeWithBinaryEncoder:anEncoder ThreadsPalette:testhelper.threadsPalette];
    
    CSGBinaryDecoder* anDecoder = [[CSGBinaryDecoder alloc] initWithData:anEncoder.data];
    
    CSGThreadsBlend* blend1 = [[CSGThreadsBlend alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:testhelper.threadsPalette];
    
    if (blend.hash != blend1.hash || ![blend isEqual:blend1])
    {
        STFail(@"ThreadsBlend serialization and equality");
    }
}

- (void) testThreadInBlendSerializationAndEquality
{
    CSGThreadInBlend* thread = testhelper.randomThreadInBlend;
    
    CSGBinaryEncoder* anEncoder = [[CSGBinaryEncoder alloc] initWithLength:thread.serializedLength];
    [thread serializeWithBinaryEncoder:anEncoder ThreadsPalette:testhelper.threadsPalette];
    
    
    CSGBinaryDecoder* anDecoder = [[CSGBinaryDecoder alloc] initWithData:anEncoder.data];
    
    CSGThreadInBlend* thread1 = [[CSGThreadInBlend alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:testhelper.threadsPalette];

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
    
    CSGBinaryDecoder* anDecoder = [[CSGBinaryDecoder alloc] initWithData:anEncoder.data];
    
    CSGThread* thread1 = [[CSGThread alloc] initWithBinaryDecoder:anDecoder];
    
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

- (void) testThreadPeletteSerialization
{
    CSGThreadsPalette* palette = [testhelper threadsPalette];
    
    CSGBinaryEncoder* anEncoder = [[CSGBinaryEncoder alloc] initWithLength:palette.serializedLength];
    [palette serializeWithBinaryEncoder:anEncoder];
    
    
    CSGBinaryDecoder* anDecoder = [[CSGBinaryDecoder alloc] initWithData:anEncoder.data];
    
    CSGThreadsPalette* palette1 = [[CSGThreadsPalette alloc] initWithBinaryDecoder:anDecoder];

    //NO EQUALITY TEST - TEST IF (SERIALIZATION == SERIALIZATION(DESERIALIZATION))
    CSGBinaryEncoder* anEncoder1 = [[CSGBinaryEncoder alloc] initWithLength:palette1.serializedLength];
    [palette1 serializeWithBinaryEncoder:anEncoder1];
    
    if (![CSGSerializationTests IsSerializedViewEqualForData:anEncoder.data Data1:anEncoder1.data])
    {
        STFail(@"Threads palette serilization");
    }
}

-(void) testStitchInCellSerialization
{
    CSGStitchInCell* stitch = testhelper.randomStitchInCell;
    
    CSGBinaryEncoder* anEncoder = [[CSGBinaryEncoder alloc] initWithLength:stitch.serializedLength];
    [stitch serializeWithBinaryEncoder:anEncoder ThreadsPalette:testhelper.threadsPalette];
    
    CSGBinaryDecoder* anDecoder = [[CSGBinaryDecoder alloc] initWithData:anEncoder.data];
    
    CSGStitchInCell* stitch1 = [[CSGStitchInCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:testhelper.threadsPalette];
    
    if (stitch.hash != stitch.hash || ![stitch isEqual:stitch1])
    {
        STFail(@"StitchInCell serialization and equality");
    }
}


-(void) testDesignCellSerialization
{
    CSGDesignCell* cell = testhelper.randomDesignCell;
    
    CSGBinaryEncoder* anEncoder = [[CSGBinaryEncoder alloc] initWithLength:cell.serializedLength];
    [cell serializeWithBinaryEncoder:anEncoder ThreadsPalette:testhelper.threadsPalette];
    
    CSGBinaryDecoder* anDecoder = [[CSGBinaryDecoder alloc] initWithData:anEncoder.data];
    
    CSGDesignCell* cell1 = [[CSGDesignCell alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:testhelper.threadsPalette];
    
    if (cell.hash != cell1.hash || ![cell isEqual:cell1])
    {
        STFail(@"Design cell serialization and equality");
    }
}

- (void) testDesignCellCoordinateSerialization
{
    CSGDesignCoordinate *coordinate = testhelper.randomDesignCoordinate;
    
    CSGBinaryEncoder* anEncoder = [[CSGBinaryEncoder alloc] initWithLength:coordinate.serializedLength];
    [coordinate serializeWithBinaryEncoder:anEncoder];
    
    CSGBinaryDecoder* anDecoder = [[CSGBinaryDecoder alloc] initWithData:anEncoder.data];
    
    CSGDesignCoordinate* coordinate1 = [[CSGDesignCoordinate alloc]initWithBinaryDecoder:anDecoder];
    
    if (coordinate.hash != coordinate1.hash || ![coordinate isEqual:coordinate1])
    {
        STFail(@"DesignCoordinate serialization and equality");
    }

}




@end