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
}

- (void) serializeToBuffer: (void*) aBuffer WithThreadsPalette: (CSGThreadsPalette*) aPalette
{
    uint32_t *buf = (uint32_t *) aBuffer;
    *buf = [aPalette threadIndex: CSG_thread];
    ++buf;
    
    uint8_t *buf1 = (uint8_t *)buf;
    *buf1 = CSG_flossCount;
}

+ (id) deserializeFromBuffer: (const void*) buffer WithThreadsPalette: (CSGThreadsPalette*) palette
{
    
    uint32_t *buf = (uint32_t *) buffer;
    uint32_t threadIndex = *buf;
    ++buf;
    
    uint8_t *buf1 = (uint8_t *)buf;
    uint8_t flossCount = *buf1;
    
    CSGThread *thread = [palette threadAtIndex: threadIndex];
    
    CSGThreadInBlend *threadInBlend = [[CSGThreadInBlend alloc] initWithThread: thread FlossCount: flossCount];
    
    return threadInBlend;
}

@end



@interface CSGThreadsBlend ()

@property NSArray* CSG_threads;

@end

@implementation CSGThreadsBlend

@synthesize CSG_threads;

- (NSEnumerator*) threadsInBlend
{
    return [CSG_threads objectEnumerator];
}


@end
