//
//  CSGBinaryDecoder.m
//  Cross Stitch Library
//
//  Created by 123 on 15.03.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGBinaryDecoder.h"

@interface CSGBinaryDecoder ()
{
    size_t offset;
}

@property (nonatomic, retain) NSData* data;

@end

@implementation CSGBinaryDecoder

@synthesize data;

- (id) initWithData: (NSData *) aData
{
    if (self = [super init])
    {
        data = aData;
        offset = 0;
    }
    return self;
}

- (const void *) readBytes: (size_t) length
{
    const void *bytes = data.bytes + offset;
    offset += length;
    return bytes;
}


@end
