//
//  CSGDecodec.m
//  Cross Stitch Library
//
//  Created by 123 on 28.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGDecodec.h"

#import "CSGObjectsRegistry.h"
#import "CSGBinaryCoding.h"

#import "CSGThread.h"
#import "CSGThreadsBlend.h"

@interface CSGDecodec()

@property (nonatomic, retain) CSGBinaryDecoder* decoder;
@property (nonatomic, retain) CSGObjectsRegistry* registry;

@end

@implementation CSGDecodec

@synthesize decoder = anDecoder;//TODO: rename to decoder
@synthesize registry;

//initialization
-(id) initWithData: (NSData*) data
{
    if (self = [super init])
    {
        anDecoder = [[CSGBinaryDecoder alloc] initWithData:data];
    }
    return self;
}

-(void) deserilizeObjectsRegistry
{
    //TODO: implement when need
    registry = [[CSGObjectsRegistry alloc] init];
}

//deserialization
- (CSGThread*) deserializeThread
{
    [self deserilizeObjectsRegistry];
    return [self deserializeThreadImpl];
}

- (CSGThread*) deserializeThreadImpl
{
    const uint8_t *buf = [anDecoder readBytes:sizeof(uint8_t)];
    uint8_t iRed = *buf;
    
    buf = [anDecoder readBytes:sizeof(uint8_t)];
    uint8_t iGreen = *buf;
    
    buf = [anDecoder readBytes:sizeof(uint8_t)];
    uint8_t iBlue = *buf;
    
    CGFloat red = (CGFloat) iRed / 255.0;
    CGFloat green = (CGFloat) iGreen / 255.0;
    CGFloat blue = (CGFloat) iBlue / 255.0;
    UIColor* color = [[UIColor alloc] initWithRed: red green: green blue: blue alpha: 1.0];
    
    return [registry getThreadWithColor: color];
}


- (CSGThreadInBlend*) deserializeThreadInBlend
{
    [self deserilizeObjectsRegistry];
    return [self deserializeThreadInBlendImpl];
}

- (CSGThreadInBlend*) deserializeThreadInBlendImpl
{
    CSGThread *thread = [self deserializeThreadImpl];
    const uint8_t *buf1 = [anDecoder readBytes:sizeof(uint8_t)];
    uint8_t flossCount = *buf1;
    
    return [registry getThreadInBlendWithThread:thread FlossCount:flossCount];
}

- (CSGThreadsBlend*) deserializeThreadsBlend
{
    [self deserilizeObjectsRegistry];
    return [self deserializeThreadsBlendImpl];
}

- (CSGThreadsBlend*) deserializeThreadsBlendImpl
{
    const uint8_t *buf = [anDecoder readBytes:sizeof(uint8_t)];
    uint8_t length = *buf;
    
    NSMutableArray* threadsInBlend = [[NSMutableArray alloc] init];
    for(int i = 0; i < length; ++i)
    {
        CSGThreadInBlend* threadInBlend = [self deserializeThreadInBlendImpl];
        [threadsInBlend addObject:threadInBlend];
    }
    return [registry getThreadsBlendWithThreadsInBlend:threadsInBlend];
}

@end
