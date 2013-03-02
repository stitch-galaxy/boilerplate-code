//
//  Cross_Stitch_LibraryTests.m
//  Cross Stitch LibraryTests
//
//  Created by 123 on 17.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <stdlib.h>

#import "Cross_Stitch_LibraryTests.h"
#import "ArtWorkDesign.h"

@interface ArtWorkDesign(Test)

+ (id) GenerateTestDesign;

@end

#define TEST_STITCH_WIDTH 10
#define TEST_STITCH_HEIGHT 10

@implementation ArtWorkDesign(Test)

+ (CGFloat) GetRandomColorComponent
{
    return ((CGFloat)(arc4random() % 265)) / 255.0;
}

+ (uint32_t) GetRandomIndex
{
    return arc4random() % 10;
}

+ (id) GenerateTestDesign
{
    CGFloat threadColors[10][3];
    CGFloat beadColors[10][4];
    
    for(int i =0; i < 10; ++i)
    {
        threadColors[i][0] = [ArtWorkDesign GetRandomColorComponent];
        threadColors[i][1] = [ArtWorkDesign GetRandomColorComponent];
        threadColors[i][2] = [ArtWorkDesign GetRandomColorComponent];
        
        beadColors[i][0] = [ArtWorkDesign GetRandomColorComponent];
        beadColors[i][1] = [ArtWorkDesign GetRandomColorComponent];
        beadColors[i][2] = [ArtWorkDesign GetRandomColorComponent];
        beadColors[i][3] = [ArtWorkDesign GetRandomColorComponent];
    }
    
    ThreadMaterialCollection* aThreadMaterials = [[ThreadMaterialCollection alloc] initWithCapacity:10];
    BeadMaterialCollection* aBeadMaterials = [[BeadMaterialCollection alloc] initWithCapacity:10];
    ArtWorkDesign* aTestDesign = [[ArtWorkDesign alloc] initWithWidth:TEST_STITCH_WIDTH AndHeight:TEST_STITCH_HEIGHT Threads:aThreadMaterials AndBeads:aBeadMaterials];
    
    for(uint32_t i = 0; i < TEST_STITCH_HEIGHT; ++i)
    {
        for(uint32_t j = 0; j < TEST_STITCH_WIDTH; ++j)
        {
            uint32_t tci = [ArtWorkDesign GetRandomIndex];
            UIColor *threadColor = [[UIColor alloc] initWithRed:threadColors[tci][0] green:threadColors[tci][1] blue:threadColors[tci][2] alpha:1.0];
            
            uint32_t bci = [ArtWorkDesign GetRandomIndex];
            UIColor *beadColor = [[UIColor alloc] initWithRed:beadColors[bci][0] green:beadColors[bci][1] blue:beadColors[bci][2] alpha:beadColors[bci][3]];
            
            ThreadMaterial* threadMaterial = [aThreadMaterials GetThreadMaterialByColor:threadColor];
            BeadMaterial* beadMaterial = [aBeadMaterials GetBeadMaterialByColor:beadColor AndSize:[NSDecimalNumber decimalNumberWithString:@"1.0"]];
            Stitch *aStitch = [Stitch alloc] initWithThreadMaterial:threadMaterial ThreadStitchType:<#(int8_t)#> BeadMaterial:beadMaterial BeadStitchType:<#(int8_t)#>
        }
    }
    return aTestDesign;
}

@end


@implementation Cross_Stitch_LibraryTests

- (void)setUp
{
    [super setUp];
    
    // Set-up code here.
}

- (void)tearDown
{
    // Tear-down code here.
    
    [super tearDown];
}

- (void)testExample
{
    ArtWorkDesign* design = [ArtWorkDesign GenerateTestDesign];
    NSMutableData* data = [[NSMutableData alloc] initWithCapacity:[design GetSerializedLength]];
    [design SerializeToBuffer:[data mutableBytes]];
    
    ArtWorkDesign* design1 = [ArtWorkDesign DeserializeFromBuffer:[data bytes]];
    NSMutableData* data1 = [[NSMutableData alloc] initWithCapacity:[design1 GetSerializedLength]];
    [design1 SerializeToBuffer:[data1 mutableBytes]];
    
    if (![data isEqual:data1])
    {
        STFail(@"Serialization deserialization works incorrectly");
    }
}

@end