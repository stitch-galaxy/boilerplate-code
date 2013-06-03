//
//  CSGBinaryCoder.m
//  Cross Stitch Library
//
//  Created by 123 on 15.03.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGBinaryEncoder.h"

@interface CSGBinaryEncoder ()
{
    size_t offset;
}

@property (nonatomic, retain) NSMutableData* data;

@end

@implementation CSGBinaryEncoder

@synthesize data;

- (id) init
{
    NSAssert( false, @"Please use designated initializer" );
    
    return nil;
}

- (id) initWithLength: (size_t) length
{
    if (self = [super init])
    {
        data = [[NSMutableData alloc] initWithLength:length];
        offset = 0;
    }
    return self;
}

- (void *) modifyBytes: (size_t) length
{
    void *bytes = data.mutableBytes + offset;
    offset += length;
    return bytes;
}

- (NSMutableData*) data
{
    return data;
}


@end
