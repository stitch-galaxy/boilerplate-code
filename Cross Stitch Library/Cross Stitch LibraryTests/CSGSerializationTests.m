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

//#import "ArtWorkDesign.h"
//
//@interface ArtWorkDesign(Test)
//
//+ (id) GenerateTestDesign;
//
//@end
//
//#define TEST_STITCH_WIDTH 10
//#define TEST_STITCH_HEIGHT 10
//
//@implementation ArtWorkDesign(Test)
//
//+ (CGFloat) GetRandomColorComponent
//{
//    return ((CGFloat)(arc4random() % 265)) / 255.0;
//}
//
//+ (uint32_t) GetRandomIndex
//{
//    return arc4random() % 10;
//}
//
//+ (id) GenerateTestDesign
//{
//    CGFloat threadColors[10][3];
//    CGFloat beadColors[10][4];
//    
//    for(int i =0; i < 10; ++i)
//    {
//        threadColors[i][0] = [ArtWorkDesign GetRandomColorComponent];
//        threadColors[i][1] = [ArtWorkDesign GetRandomColorComponent];
//        threadColors[i][2] = [ArtWorkDesign GetRandomColorComponent];
//        
//        beadColors[i][0] = [ArtWorkDesign GetRandomColorComponent];
//        beadColors[i][1] = [ArtWorkDesign GetRandomColorComponent];
//        beadColors[i][2] = [ArtWorkDesign GetRandomColorComponent];
//        beadColors[i][3] = [ArtWorkDesign GetRandomColorComponent];
//    }
//    
//    ThreadMaterialCollection* aThreadMaterials = [[ThreadMaterialCollection alloc] initWithCapacity:10];
//    BeadMaterialCollection* aBeadMaterials = [[BeadMaterialCollection alloc] initWithCapacity:10];
//    ArtWorkDesign* aTestDesign = [[ArtWorkDesign alloc] initWithWidth:TEST_STITCH_WIDTH AndHeight:TEST_STITCH_HEIGHT Threads:aThreadMaterials AndBeads:aBeadMaterials];
//    
//    for(uint32_t i = 0; i < TEST_STITCH_HEIGHT; ++i)
//    {
//        for(uint32_t j = 0; j < TEST_STITCH_WIDTH; ++j)
//        {
//            uint32_t tci = [ArtWorkDesign GetRandomIndex];
//            UIColor *threadColor = [[UIColor alloc] initWithRed:threadColors[tci][0] green:threadColors[tci][1] blue:threadColors[tci][2] alpha:1.0];
//            
//            uint32_t bci = [ArtWorkDesign GetRandomIndex];
//            UIColor *beadColor = [[UIColor alloc] initWithRed:beadColors[bci][0] green:beadColors[bci][1] blue:beadColors[bci][2] alpha:beadColors[bci][3]];
//            
//            CSGThread* threadMaterial = [aThreadMaterials GetThreadMaterialByColor:threadColor];
//            BeadMaterial* beadMaterial = [aBeadMaterials GetBeadMaterialByColor:beadColor AndSize:[NSDecimalNumber decimalNumberWithString:@"1.0"]];
//            Stitch *aStitch = [Stitch alloc] initWithThreadMaterial:threadMaterial ThreadStitchType:<#(int8_t)#> BeadMaterial:beadMaterial BeadStitchType:<#(int8_t)#>
//        }
//    }
//    return aTestDesign;
//}
//
//@end

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
    return [[threadsPalette threadByIndex: [CSGSerializationTestHelper randomIndexFor:[threadsPalette size]]] color];
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

- (void) testThreadSerialization
{
    CSGThread* thread = [[CSGThread alloc] initWithColor:[testhelper randomThreadColor]];
    
    NSMutableData* data = [[NSMutableData alloc] initWithLength:[thread serializedLength]];
    [thread serializeToBuffer: [data mutableBytes]];
    
    CSGThread* thread1 = [CSGThread deserializeFromBuffer:[data bytes]];
    NSMutableData* data1 = [[NSMutableData alloc] initWithLength:[thread1 serializedLength]];
    [thread1 serializeToBuffer: [data1 mutableBytes]];
    
    if (![CSGSerializationTests IsSerializedViewEqualForData:data Data1:data1])
    {
        STFail(@"Serialization deserialization works incorrectly for threads");
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
    NSMutableData* data1 = [[NSMutableData alloc] initWithLength:[palette1 serializedLength]];
    [palette1 serializeToBuffer: [data1 mutableBytes]];
    
    if (![CSGSerializationTests IsSerializedViewEqualForData:data Data1:data1])
    {
        STFail(@"Serialization deserialization works incorrectly for threads palette");
    }
}

//- (void)testExample
//{
//    ArtWorkDesign* design = [ArtWorkDesign GenerateTestDesign];
//    NSMutableData* data = [[NSMutableData alloc] initWithCapacity:[design GetSerializedLength]];
//    [design SerializeToBuffer:[data mutableBytes]];
//    
//    ArtWorkDesign* design1 = [ArtWorkDesign DeserializeFromBuffer:[data bytes]];
//    NSMutableData* data1 = [[NSMutableData alloc] initWithCapacity:[design1 GetSerializedLength]];
//    [design1 SerializeToBuffer:[data1 mutableBytes]];
//    
//    if (![data isEqual:data1])
//    {
//        STFail(@"Serialization deserialization works incorrectly");
//    }
//}

@end