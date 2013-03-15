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

@end