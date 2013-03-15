//
//  CSGBaseThreadStitch.m
//  Cross Stitch Library
//
//  Created by 123 on 14.03.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGBaseThreadStitch.h"

@implementation CSGBaseThreadStitch

@synthesize threadsBlend;

- (id) initWithThreadsBlend: (CSGThreadsBlend *) aThreadsBlend
{
    if (self = [super init])
    {
        threadsBlend = aThreadsBlend;
    }
    return self;
}


- (size_t) serializedLength
{
    return threadsBlend.serializedLength;
}

- (void) serializeToBuffer: (void*) buffer WithThreadsPalette: (CSGThreadsPalette*) palette
{
}

+ (id) deserializeFromBuffer: (const void*) buffer WithThreadsPalette: (CSGThreadsPalette*) palette
{
}


@end
