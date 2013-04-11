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

- (id) init
{
    NSAssert( false, @"Please use designated initializer" );
    
    return nil;
}


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

- (void) serializeWithBinaryEncoder: (CSGBinaryEncoder *) anEncoder
{
    [threadsBlend serializeWithBinaryEncoder:anEncoder];
}

+ (id) deserializeWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder ObjectsRegistry: (CSGObjectsRegistry*) registry;
{
    CSGThreadsBlend *aThreadsBlend = [CSGThreadsBlend deserializeWithBinaryDecoder:anDecoder ObjectsRegistry:registry];

    CSGStitchInCell *aStitch = [[CSGStitchInCell alloc] initWithThreadsBlend:aThreadsBlend];
    return [registry getStitchInCell: aStitch];
}


@end
