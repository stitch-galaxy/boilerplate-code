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

- (void) serializeWithBinaryEncoder: (CSGBinaryEncoder *) anEncoder ThreadsPalette: (CSGThreadsPalette*) palette
{
    [threadsBlend serializeWithBinaryEncoder:anEncoder ThreadsPalette:palette];
}
- (id) initWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder ThreadsPalette: (CSGThreadsPalette*) palette
{
    CSGThreadsBlend *aThreadsBlend = [[CSGThreadsBlend alloc] initWithBinaryDecoder:anDecoder ThreadsPalette:palette];
    return [self initWithThreadsBlend:aThreadsBlend];
}


@end
