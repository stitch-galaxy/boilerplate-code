//
//  Cross_Stitch_LibraryTests.m
//  Cross Stitch LibraryTests
//
//  Created by 123 on 17.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <stdlib.h>

#import "CSGSerializationTests.h"

#import "CSGDesignGenerator.h"


@implementation CSGSerializationTests
{
    CSGDesignGenerator* testhelper;
}

- (void)setUp
{
    [super setUp];
    testhelper = [[CSGDesignGenerator alloc] init];
}

- (void)tearDown
{
    [super tearDown];
}

-(void) testThreadsBlendSerializationAndEquality
{
    CSGThreadsBlend* blend = testhelper.generateThreadsBlend;
    
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
    CSGThreadInBlend* thread = testhelper.generateThreadInBlend;
    
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
    CSGThread* thread = testhelper.generateThread;
    
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
    CSGDesignCell* cell = testhelper.generateDesignCell;
    
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
    CSGDesignPoint *coordinate = testhelper.generateDesignCoordinate;
    
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
    CSGDesignPoints *points = testhelper.generateDesignPoints;
    
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
    CSGBackStitch* stitch = testhelper.generateBackStitch;
    
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
    CSGStraightStitch* stitch = testhelper.generateStraightStitch;
    
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


-(void) testDesignSerialization
{
    CSGDesign* design = testhelper.generateDesign;
    
    NSDate *tStart = [NSDate date];
    
    CSGBinaryEncoder* anEncoder = [[CSGBinaryEncoder alloc] initWithLength:design.serializedLength];
    
    NSDate *tBufferPrepared = [NSDate date];
    NSLog(@"%f seconds to prepare buffer", [tBufferPrepared timeIntervalSinceDate:tStart]);
    
    [design serializeWithBinaryEncoder:anEncoder];
    
    NSDate *tSerialized = [NSDate date];
    
    NSLog(@"%f seconds to serilize", [tSerialized timeIntervalSinceDate:tBufferPrepared]);
    
    CSGObjectsRegistry *registry = [[CSGObjectsRegistry alloc] init];
    CSGBinaryDecoder* anDecoder = [[CSGBinaryDecoder alloc] initWithData:anEncoder.data];
    
    NSDate *tBufferCopied = [NSDate date];
    NSLog(@"%f seconds to copy buffer", [tBufferCopied timeIntervalSinceDate:tSerialized]);
    
    CSGDesign* design1 = [CSGDesign deserializeWithBinaryDecoder:anDecoder ObjectsRegistry:registry];
    
    NSDate *tDeserialized = [NSDate date];
    NSLog(@"%f seconds to deserialize", [tDeserialized timeIntervalSinceDate:tBufferCopied]);
    
    if (design.hash != design1.hash || ![design isEqual:design1])
    {
        STFail(@"Design serialization and equality");
    }
    
    NSDate *tCompared = [NSDate date];
    NSLog(@"%f seconds to compare", [tCompared timeIntervalSinceDate:tDeserialized]);

    int i = 0;
    ++i;
}



@end