//
//  ThreadMaterial.h
//  Cross Stitch Library
//
//  Created by 123 on 20.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol CSGThread <NSObject>

- (UIColor*) color;

@end

@interface CSGThread : NSObject<CSGThread>

- (id) initWithColor: (UIColor*) aColor;

- (BOOL) isEqual: (id) object;
- (BOOL) isEqualToCSGThread: (CSGThread*) aThread;
- (NSUInteger) hash;

@end


@interface CSGThread (Serialization)

- (size_t) serializedLength;
- (void) serializeToBuffer: (void*) buffer;
+ (id) deserializeFromBuffer: (const void*) buffer;

@end
