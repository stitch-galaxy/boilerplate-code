//
//  CSGBinaryCoder.h
//  Cross Stitch Library
//
//  Created by 123 on 15.03.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol CSGBinaryEncoder <NSObject>

- (id) initWithLength: (size_t) length;

- (void *) modifyBytes: (size_t) length;

- (NSMutableData*) data;

@end

@interface CSGBinaryEncoder : NSObject<CSGBinaryEncoder>

@end


