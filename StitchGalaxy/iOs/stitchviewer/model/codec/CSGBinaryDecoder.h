//
//  CSGBinaryDecoder.h
//  Cross Stitch Library
//
//  Created by 123 on 15.03.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol CSGBinaryDecoder <NSObject>

- (id) initWithData: (NSData *) data;

- (const void *) readBytes: (size_t) length;

@end

@interface CSGBinaryDecoder : NSObject<CSGBinaryDecoder>

@end
