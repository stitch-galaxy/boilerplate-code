//
//  ThreadsBlend.h
//  Cross Stitch Library
//
//  Created by 123 on 13.03.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <stdint.h>

#import "CSGBinaryCoding.h"
#import "CSGThread.h"

@protocol CSGThreadInBlend <NSObject>

- (CSGThread*) thread;
- (uint8_t) flossCount;

@end

@interface CSGThreadInBlend : NSObject<CSGThreadInBlend>

-(id) initWithThread: (CSGThread*) aThread FlossCount: (uint8_t) aFlossCount;

@end

@interface CSGThreadInBlend (Serialization)

- (size_t) serializedLength;
- (void) serializeWithBinaryEncoder: (CSGBinaryEncoder *) anEncoder ObjectsRegistry: (CSGObjectsRegistry*) registry;
+ (id) deserializeWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder ObjectsRegistry: (CSGObjectsRegistry*) registry;

@end


@protocol CSGThreadsBlend

- (NSArray*) threadsInBlend;

@end

@interface CSGThreadsBlend : NSObject<CSGThreadsBlend>

- (id) initWithThreadsInBlend: (NSArray* ) threadsInBlend;

@end

@interface CSGThreadsBlend (Serialization)

- (size_t) serializedLength;
- (void) serializeWithBinaryEncoder: (CSGBinaryEncoder *) anEncoder ObjectsRegistry: (CSGObjectsRegistry*) registry;
+ (id) deserializeWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder ObjectsRegistry: (CSGObjectsRegistry*) registry;

@end
