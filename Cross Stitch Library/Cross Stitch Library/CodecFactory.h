//
//  CodecFactory.h
//  Cross Stitch Library
//
//  Created by 123 on 19.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "Codec.h"

@interface CodecFactory : NSObject

+ (id<ICodec>) GetCodecForVersion: (int32_t) version;

@end
