//
//  CSGCodec.m
//  Cross Stitch Library
//
//  Created by 123 on 28.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGCodec.h"

#import "CSGObjectsRegistry.h"
#import "CSGBinaryCoding.h"

#import "CSGThread.h"
#import "CSGThreadsBlend.h"

@interface CSGCodec()

@property (nonatomic, retain) CSGBinaryEncoder* encoder;
@property (nonatomic, retain) CSGObjectsRegistry* registry;

@end

@implementation CSGCodec

@synthesize encoder = anEncoder;//TODO: rename to encoder
@synthesize registry;

- (id) initWithObjectsRegistry: (CSGObjectsRegistry*) anRegistry
{
    if (self = [super init])
    {
        registry = anRegistry;
    }
    return self;
}

- (NSData*) data
{
    return anEncoder.data;
}

- (size_t) serializedObjectsRegistryLength
{
    //TODO: implement when necessary
    return 0;
}

- (void) serializeObjectsRegistry
{
    //TODO: implement when necessary
}

- (void) serializeThread: (CSGThread*) aThread
{
    size_t anSize =  self.serializedObjectsRegistryLength;
    anSize += [self serializedThreadLength: aThread];
    anEncoder = [[CSGBinaryEncoder alloc] initWithLength: anSize];
    [self serializeObjectsRegistry];
    
    [self serializeThreadImpl:aThread];
}

-(size_t) serializedThreadLength: (CSGThread*) aThread
{
    return sizeof(uint8_t) * 3;
}

- (void) serializeThreadImpl:(CSGThread *)aThread
{
    const CGFloat *components = CGColorGetComponents(aThread.color.CGColor);
    uint8_t red = lroundf(components[0] * 255.0);
    uint8_t green = lround(components[1] * 255.0);
    uint8_t blue = lround(components[2] * 255.0);
    
    uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
    *buf = red;
    buf = [anEncoder modifyBytes:sizeof(uint8_t)];
    *buf = green;
    buf = [anEncoder modifyBytes:sizeof(uint8_t)];
    *buf = blue;
}

- (void) serializeThreadInBlend:(CSGThreadInBlend *)aThread
{
    size_t anSize =  self.serializedObjectsRegistryLength;
    anSize += [self serializedThreadInBlendLength: aThread];
    anEncoder = [[CSGBinaryEncoder alloc] initWithLength: anSize];
    
    [self serializeObjectsRegistry];
    [self serializeThreadInBlendImpl:aThread];
}

-(size_t) serializedThreadInBlendLength: (CSGThreadInBlend*) aThread
{
    return [self serializedThreadLength:aThread.thread] + sizeof(uint8_t);
}

- (void) serializeThreadInBlendImpl:(CSGThreadInBlend*)aThread
{
    [self serializeThreadImpl:aThread.thread];
    
    uint8_t *buf = [anEncoder modifyBytes: sizeof(uint8_t)];
    *buf = aThread.flossCount;
}

- (void) serializeThreadsBlend: (CSGThreadsBlend*) aBlend
{
    size_t anSize =  self.serializedObjectsRegistryLength;
    anSize += [self serializedThreadsBlendLength: aBlend];
    anEncoder = [[CSGBinaryEncoder alloc] initWithLength: anSize];
    
    [self serializeObjectsRegistry];
    [self serializeThreadsBlendImpl:aBlend];
}

- (size_t) serializedThreadsBlendLength: (CSGThreadsBlend*) aBlend
{
    size_t result = sizeof(uint8_t);
    
    for(CSGThreadInBlend *thread in aBlend.threadsInBlend)
    {
        result += [self serializedThreadInBlendLength:thread];
    }
    return result;
}

- (void) serializeThreadsBlendImpl: (CSGThreadsBlend*) aBlend
{
    uint8_t* buf = [anEncoder modifyBytes:sizeof(uint8_t)];
    *buf = aBlend.threadsInBlend.count;
    
    for(CSGThreadInBlend *thread in aBlend.threadsInBlend)
    {
        [self serializeThreadInBlendImpl:thread];
    }
}


@end

