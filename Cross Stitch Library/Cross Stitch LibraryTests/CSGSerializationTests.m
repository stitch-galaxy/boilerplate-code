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
    NSMutableData* data = [[NSMutableData alloc] initWithLength:[blend serializedLength]];
    [blend serializeToBuffer: [data mutableBytes] WithThreadsPalette:testhelper.threadsPalette];
    
    CSGThreadInBlend* blend1 = [CSGThreadsBlend deserializeFromBuffer:data.bytes WithThreadsPalette:testhelper.threadsPalette];
    if (blend.hash != blend1.hash || ![blend isEqual:blend1])
    {
        STFail(@"ThreadsBlend equality");
    }
    NSMutableData* data1 = [[NSMutableData alloc] initWithLength:[blend1 serializedLength]];
    [blend1 serializeToBuffer: data1.mutableBytes WithThreadsPalette:testhelper.threadsPalette];
    
    if (![CSGSerializationTests IsSerializedViewEqualForData:data Data1:data1])
    {
        STFail(@"ThreadsBlend serialization");
    }
}

- (void) testThreadInBlendSerializationAndEquality
{
    CSGThreadInBlend* thread = testhelper.randomThreadInBlend;
    
    NSMutableData* data = [[NSMutableData alloc] initWithLength:[thread serializedLength]];
    [thread serializeToBuffer: [data mutableBytes] WithThreadsPalette:testhelper.threadsPalette];
    
    CSGThreadInBlend* thread1 = [CSGThreadInBlend deserializeFromBuffer:data.bytes WithThreadsPalette:testhelper.threadsPalette];
    if (thread.hash != thread1.hash || ![thread isEqual:thread1])
    {
        STFail(@"ThreadInBlend equality");
    }
    NSMutableData* data1 = [[NSMutableData alloc] initWithLength:[thread1 serializedLength]];
    [thread1 serializeToBuffer: data1.mutableBytes WithThreadsPalette:testhelper.threadsPalette];
    
    if (![CSGSerializationTests IsSerializedViewEqualForData:data Data1:data1])
    {
        STFail(@"ThreadInBlend serialization");
    }
}

- (void) testThreadSerializationAndEquality
{
    CSGThread* thread = testhelper.randomThread;
    
    NSMutableData* data = [[NSMutableData alloc] initWithLength:[thread serializedLength]];
    [thread serializeToBuffer: [data mutableBytes]];
    
    CSGThread* thread1 = [CSGThread deserializeFromBuffer:[data bytes]];
    if (thread.hash != thread1.hash || ![thread isEqual:thread1])
    {
        STFail(@"Thread equality");
    }
    NSMutableData* data1 = [[NSMutableData alloc] initWithLength:[thread1 serializedLength]];
    [thread1 serializeToBuffer: [data1 mutableBytes]];
    
    if (![CSGSerializationTests IsSerializedViewEqualForData:data Data1:data1])
    {
        STFail(@"Thread serialization");
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
    NSMutableData* data = [[NSMutableData alloc] initWithLength:[palette serializedLength]];
    [palette serializeToBuffer: [data mutableBytes]];
    
    CSGThreadsPalette* palette1 = [CSGThreadsPalette deserializeFromBuffer:[data bytes]];
//    if (palette.hash != palette1.hash || ![palette isEqual:palette1])
//    {
//        STFail(@"Thread palette equality");
//    }
    
    NSMutableData* data1 = [[NSMutableData alloc] initWithLength:[palette1 serializedLength]];
    [palette1 serializeToBuffer: [data1 mutableBytes]];
    
    if (![CSGSerializationTests IsSerializedViewEqualForData:data Data1:data1])
    {
        STFail(@"Threads palette serilization");
    }
}

@end