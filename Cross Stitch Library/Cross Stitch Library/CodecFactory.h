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

+ (id<ICodec>) GetCodec: (int32_t) codecVersion;
+ (id<ICodec>) GetLatesetCodec;

@end
