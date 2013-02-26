//
//  ICodec.m
//  Cross Stitch Library
//
//  Created by 123 on 19.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "Codec.h"

@interface CodecVer_1_0 : NSObject <ICodec>

@end


@implementation CodecVer_1_0

- (NSData*) Encode:(id<IArtWorkDesign>) anArtWork
{
    NSUInteger capacity = 0;
    NSMutableData* mutabledata = [[NSMutableData alloc] initWithCapacity: capacity];
    return mutabledata;
}

- (id<IArtWorkDesign>) Decode:(NSData*) aFileUrl
{
    id<IArtWorkDesign> design = nil;
    return design;
}

@end