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

#import "CSGCodec.h"
#import "CSGDecodec.h"


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

- (void) testThreadSerializationAndEquality
{
    CSGObjectsRegistry *registry = [[CSGObjectsRegistry alloc] init];
    CSGCodec *codec = [[CSGCodec alloc] initWithObjectsRegistry:registry];
    CSGThread* thread = [testhelper generateThread:registry];
    [codec serializeThread:thread];
    
    CSGDecodec *decodec = [[CSGDecodec alloc] initWithData:codec.data];
    CSGThread* thread1 = [decodec deserializeThread];
    
    if (thread.hash != thread1.hash || ![thread isEqual:thread1])
    {
        STFail(@"Thread serialization and equality");
    }
}


- (void) testThreadInBlendSerializationAndEquality
{
    CSGObjectsRegistry *registry = [[CSGObjectsRegistry alloc] init];
    CSGCodec *codec = [[CSGCodec alloc] initWithObjectsRegistry:registry];
    CSGThreadInBlend* thread = [testhelper generateThreadInBlend: registry];
    [codec serializeThreadInBlend:thread];
    
    CSGDecodec *decodec = [[CSGDecodec alloc] initWithData:codec.data];
    CSGThreadInBlend* thread1 = [decodec deserializeThreadInBlend];
    
    if (thread.hash != thread1.hash || ![thread isEqual:thread1])
    {
        STFail(@"ThreadInBlend serialization and equality");
    }
}

- (void) testThreadsBlendSerializationAndEquality
{
    CSGObjectsRegistry *registry = [[CSGObjectsRegistry alloc] init];
    CSGCodec *codec = [[CSGCodec alloc] initWithObjectsRegistry:registry];
    CSGThreadsBlend* blend = [testhelper generateThreadsBlend: registry];
    [codec serializeThreadsBlend:blend];
    
    CSGDecodec *decodec = [[CSGDecodec alloc] initWithData:codec.data];
    CSGThreadsBlend* blend1 = [decodec deserializeThreadsBlend];
    
    if (blend.hash != blend1.hash || ![blend isEqual:blend1])
    {
        STFail(@"ThreadBlends serialization and equality");
    }
}

- (void) testDesignCellSerializationAndEquality
{
    CSGObjectsRegistry *registry = [[CSGObjectsRegistry alloc] init];
    CSGCodec *codec = [[CSGCodec alloc] initWithObjectsRegistry:registry];
    CSGDesignCell* cell = [testhelper generateDesignCell: registry];
    [codec serializeDesignCell:cell];
    
    CSGDecodec *decodec = [[CSGDecodec alloc] initWithData:codec.data];
    CSGDesignCell* cell1 = [decodec deserializeDesignCell];
    
    if (cell.hash != cell1.hash || ![cell isEqual:cell1])
    {
        STFail(@"DesignCell serialization and equality");
    }
}

- (void) testDesignPointSerializationAndEquality
{
    CSGObjectsRegistry *registry = [[CSGObjectsRegistry alloc] init];
    CSGCodec *codec = [[CSGCodec alloc] initWithObjectsRegistry:registry];
    CSGDesignPoint* point = [testhelper generateDesignCoordinate:registry];
    [codec serializeDesignPoint:point];
    
    CSGDecodec *decodec = [[CSGDecodec alloc] initWithData:codec.data];
    CSGDesignPoint* point1 = [decodec deserializeDesignPoint];
    
    if (point.hash != point1.hash || ![point isEqual:point1])
    {
        STFail(@"DesignPoint serialization and equality");
    }
}

- (void) testDesignPointsSerializationAndEquality
{
    CSGObjectsRegistry *registry = [[CSGObjectsRegistry alloc] init];
    CSGCodec *codec = [[CSGCodec alloc] initWithObjectsRegistry:registry];
    CSGDesignPoints* points = [testhelper generateDesignPoints:registry];
    [codec serializeDesignPoints:points];
    
    CSGDecodec *decodec = [[CSGDecodec alloc] initWithData:codec.data];
    CSGDesignPoints* points1 = [decodec deserializeDesignPoints];
    
    if (points.hash != points1.hash || ![points isEqual:points1])
    {
        STFail(@"DesignPoints serialization and equality");
    }
}

- (void) testBackStitchSerializationAndEquality
{
    CSGObjectsRegistry *registry = [[CSGObjectsRegistry alloc] init];
    CSGCodec *codec = [[CSGCodec alloc] initWithObjectsRegistry:registry];
    CSGBackStitch* stitch = [testhelper generateBackStitch:registry];
    [codec serializeBackStitch:stitch];
    
    CSGDecodec *decodec = [[CSGDecodec alloc] initWithData:codec.data];
    CSGBackStitch* stitch1 = [decodec deserializeBackStitch];
    
    if (stitch.hash != stitch1.hash || ![stitch isEqual:stitch1])
    {
        STFail(@"BackStitch serialization and equality");
    }
}

- (void) testStraightStitchSerializationAndEquality
{
    CSGObjectsRegistry *registry = [[CSGObjectsRegistry alloc] init];
    CSGCodec *codec = [[CSGCodec alloc] initWithObjectsRegistry:registry];
    CSGStraightStitch* stitch = [testhelper generateStraightStitch:registry];
    [codec serializeStraightStitch:stitch];
    
    CSGDecodec *decodec = [[CSGDecodec alloc] initWithData:codec.data];
    CSGStraightStitch* stitch1 = [decodec deserializeStraightStitch];
    
    if (stitch.hash != stitch1.hash || ![stitch isEqual:stitch1])
    {
        STFail(@"BackStitch serialization and equality");
    }
}


-(void) testDesignSerializationLegacy
{
    NSLog(@"------------------------");
    NSDate *tStart = [NSDate date];
    
    CSGObjectsRegistry *registry = [[CSGObjectsRegistry alloc] init];
    CSGDesign* design = [testhelper generateDesign: registry];
    NSDate *tStartSerialization = [NSDate date];
    NSLog(@"%f seconds to generate design", [tStartSerialization timeIntervalSinceDate:tStart]);
    
    CSGCodec *codec = [[CSGCodec alloc] initWithObjectsRegistry:registry];
    [codec serializeDesign: design];
    
    NSDate *tSerialized = [NSDate date];
    
    NSLog(@"%f seconds to serialize", [tSerialized timeIntervalSinceDate:tStartSerialization]);
    
    CSGDecodec *decodec = [[CSGDecodec alloc] initWithData:codec.data];
    CSGDesign* design1 = [decodec deserializeDesign];
    
    NSDate *tDeserialized = [NSDate date];
    NSLog(@"%f seconds to deserialize", [tDeserialized timeIntervalSinceDate:tSerialized]);
    
    if (design.hash != design1.hash)
    {
        STFail(@"Design serialization and equality");
    }
    
    NSDate *tHashCompared = [NSDate date];
    NSLog(@"%f seconds to compare hash", [tHashCompared timeIntervalSinceDate:tDeserialized]);
    
    if (![design isEqual:design1])
    {
        STFail(@"Design serialization and equality");
    }
    NSDate *tCompared = [NSDate date];
    NSLog(@"%f seconds to check equality", [tCompared timeIntervalSinceDate:tHashCompared]);
    NSLog(@"------------------------");
}

@end