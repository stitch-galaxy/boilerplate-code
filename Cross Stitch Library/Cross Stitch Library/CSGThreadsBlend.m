//
//  ThreadsBlend.m
//  Cross Stitch Library
//
//  Created by 123 on 13.03.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGThreadsBlend.h"

@interface CSGThreadInBlend ()

@property (nonatomic, retain) CSGThread* CSG_thread;
@property (nonatomic, assign) uint8_t CSG_flossCount;

@end

@implementation CSGThreadInBlend

@synthesize CSG_thread;
@synthesize CSG_flossCount;


- (id) init
{
    NSAssert( false, @"Please use designated initializer" );
    
    return nil;
}

-(id) initWithThread: (CSGThread*) aThread FlossCount: (uint8_t) aFlossCount
{
    if (self = [super init])
    {
        CSG_thread = aThread;
        CSG_flossCount = aFlossCount;
    }
    return self;
}

- (CSGThread*) thread
{
    return CSG_thread;
}

- (uint8_t) flossCount
{
    return CSG_flossCount;
}


- (BOOL) isEqual: (id) object
{
    if (object == self)
    {
        return YES;
    }
    if (!object || ![object isKindOfClass:[self class]])
    {
        return NO;
    }
    
    return [self isEqualToCSGThreadInBlend: object];
}

- (BOOL) isEqualToCSGThreadInBlend: (CSGThreadInBlend*) aThread
{
    if (self == aThread)
    {
        return YES;
    }
    if (![CSG_thread isEqual: [aThread thread]])
    {
        return NO;
    }
    if (CSG_flossCount != [aThread flossCount])
    {
        return NO;
    }
    
    return YES;
}

- (NSUInteger)hash
{
    NSUInteger hash = 17;
    hash = hash * 31 + CSG_thread.hash;
    hash = hash * 31 + CSG_flossCount;
    
    return hash;
}

@end


@implementation CSGThreadInBlend (Serialization)

- (size_t) serializedLength
{
    return sizeof(uint32_t) + sizeof(uint8_t);
    //return CSG_thread.serializedLength + sizeof(uint8_t);
}

- (void) serializeWithBinaryEncoder: (CSGBinaryEncoder *) anEncoder ObjectsRegistry: (CSGObjectsRegistry*) registry
{
    uint32_t *tIndex = [anEncoder modifyBytes: sizeof(uint32_t)];
    *tIndex = [registry getThreadIndex:CSG_thread];
    //[CSG_thread serializeWithBinaryEncoder: anEncoder ObjectsRegistry:registry];
        
    uint8_t *buf = [anEncoder modifyBytes: sizeof(uint8_t)];
    *buf = CSG_flossCount;
}

+ (id) deserializeWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder ObjectsRegistry: (CSGObjectsRegistry*) registry;
{
    //CSGThread *thread = [CSGThread deserializeWithBinaryDecoder: anDecoder ObjectsRegistry: registry];
    const uint32_t *tIndex = [anDecoder readBytes:sizeof(uint32_t)];
    CSGThread *thread = [registry getThreadByIndex: *tIndex];
    
    const uint8_t *buf1 = [anDecoder readBytes:sizeof(uint8_t)];
    uint8_t flossCount = *buf1;
    
    CSGThreadInBlend* threadInBlend = [[CSGThreadInBlend alloc] initWithThread: thread FlossCount: flossCount];
    
    return [registry getThreadInBlend: threadInBlend];
}

@end

@interface CSGThreadsBlend ()

@property NSArray* CSG_threads;

@end

@implementation CSGThreadsBlend

@synthesize CSG_threads;

- (NSArray*) threadsInBlend
{
    return CSG_threads;
}

- (id) init
{
    NSAssert( false, @"Please use designated initializer" );
    
    return nil;
}


- (id) initWithThreadsInBlend: (NSArray* ) threadsInBlend
{
    if (self = [super init])
    {
        CSG_threads = threadsInBlend;
    }
    return self;
}

- (BOOL) isEqual: (id) object
{
    if (object == self)
    {
        return YES;
    }
    if (!object || ![object isKindOfClass:[self class]])
    {
        return NO;
    }
    
    return [self isEqualToCSGThreadsBlend: object];
}

- (BOOL) isEqualToCSGThreadsBlend: (CSGThreadsBlend*) aBlend
{
    if (self == aBlend)
    {
        return YES;
    }
    
    //Smart comparison
    if (CSG_threads.count != aBlend.threadsInBlend.count)
    {
        return NO;
    }
    
    for (CSGThreadInBlend *threadInBlend in CSG_threads)
    {
        if (![aBlend.threadsInBlend containsObject:threadInBlend])
        {
            return NO;
        }
    }
    
    return YES;
}

- (NSUInteger)hash
{
    //Smart hash
    NSUInteger hash = 0;
    
    
    //Multiple by constant factor and XOR
    for (CSGThreadInBlend *threadInBlend in CSG_threads)
    {
        hash ^= threadInBlend.hash * 31;
    }
    
    return hash;
}


@end

@implementation CSGThreadsBlend (Serialization)

- (size_t) serializedLength
{
    size_t result = sizeof(uint8_t);
    
    for(CSGThreadInBlend *thread in CSG_threads)
    {
        result += [thread serializedLength];
    }
    return result;
}

- (void) serializeWithBinaryEncoder: (CSGBinaryEncoder *) anEncoder ObjectsRegistry: (CSGObjectsRegistry*) registry
{
    uint8_t* buf = [anEncoder modifyBytes:sizeof(uint8_t)];
    *buf = [CSG_threads count];
    
    for(CSGThreadInBlend *thread in CSG_threads)
    {
        [thread serializeWithBinaryEncoder:anEncoder ObjectsRegistry:registry];
    }
}

+ (id) deserializeWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder ObjectsRegistry: (CSGObjectsRegistry*) registry;
{
    const uint8_t *buf = [anDecoder readBytes:sizeof(uint8_t)];
    uint8_t length = *buf;
    
    NSMutableArray* threadsInBlend = [[NSMutableArray alloc] init];
    for(int i = 0; i < length; ++i)
    {
        CSGThreadInBlend* threadInBlend = [CSGThreadInBlend deserializeWithBinaryDecoder:anDecoder ObjectsRegistry:registry];
        [threadsInBlend addObject:threadInBlend];
    }
    CSGThreadsBlend* blend = [[CSGThreadsBlend alloc] initWithThreadsInBlend: threadsInBlend];

    return [registry getThreadsBlend: blend];
}

@end

