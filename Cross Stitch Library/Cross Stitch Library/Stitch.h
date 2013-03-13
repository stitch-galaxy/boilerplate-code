//
//  Stitch.h
//  Cross Stitch Library
//
//  Created by 123 on 17.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "CSGThread.h"
#import "CSGThreadsPalette.h"

typedef enum
{
    THREAD_STITCH_LEFT = 0X01,
    THREAD_STITCH_RIGHT = 0X02,
    THREAD_STITCH_TOP = 0X04,
    THREAD_STITCH_BOTTOM = 0X08,
    THREAD_STITCH_DIVIVDE = 0X10,
    THREAD_STITCH_BACKSLASH = 0X20,
} THREAD_STITCHES;


@protocol IStitch <NSObject>

@property (nonatomic, assign) BOOL Done;

@property (nonatomic, strong) CSGThread* ThreadMaterial;
@property (nonatomic, assign) int8_t ThreadStitchType;

@end

@interface Stitch : NSObject<IStitch>

- (id) initWithThreadMaterial: (CSGThread*) aThreadMaterail ThreadStitchType: (int8_t) aThreadStitchType;


- (BOOL) isEqual: (id) object;
- (BOOL) isEqualToStitch: (Stitch*) aStitch;
- (NSUInteger) hash;

@end

@interface Stitch (Serialization)

- (size_t) GetSerializedLength;
- (void) SerializeToBuffer: (void*) buffer WithThreadsCollection: (CSGThreadsPalette*) threads;
+ (id) DeserializeFromBuffer: (void*) buffer WithThreadsCollection: (CSGThreadsPalette*) threads;
@end