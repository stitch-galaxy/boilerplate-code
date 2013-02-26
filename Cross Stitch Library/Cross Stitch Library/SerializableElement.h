//
//  SerializableElement.h
//  Cross Stitch Library
//
//  Created by 123 on 26.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <stdint.h>

@protocol ISerializableElement <NSObject>

- (int32_t) GetSerializedLength;
- (void) SerializeToBuffer: (void*) buffer;
+ (id) DeserializeFromBuffer: (void*) buffer;

@end
