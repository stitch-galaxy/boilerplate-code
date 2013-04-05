//
//  CSGCrossStitch.m
//  Cross Stitch Library
//
//  Created by 123 on 15.03.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGStitchInCell.h"

@implementation CSGStitchInCell

@synthesize threadsBlend;

- (id) initWithThreadsBlend: (CSGThreadsBlend *) aThreadsBlend
{
    if (self = [super init])
    {
        threadsBlend = aThreadsBlend;
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
    
    return [self isEqualToCSGStitchInCell: object];
}

- (BOOL) isEqualToCSGStitchInCell: (CSGStitchInCell*) aStitch
{
    if (self == aStitch)
    {
        return YES;
    }
    if (![self.threadsBlend isEqual: aStitch.threadsBlend])
    {
        return NO;
    }
    
    return YES;
}

- (NSUInteger)hash
{
    NSUInteger hash = 17;
    hash = hash*31 + self.threadsBlend.hash;
    
    return hash;
}


@end

@implementation CSGStitchInCell (Serialization)

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
