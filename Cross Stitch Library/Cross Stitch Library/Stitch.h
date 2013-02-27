//
//  Stitch.h
//  Cross Stitch Library
//
//  Created by 123 on 17.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "ThreadMaterial.h"
#import "BeadMaterial.h"
#import "ThreadMaterialCollection.h"
#import "BeadMaterialCollection.h"

typedef enum
{
    THREAD_STITCH_LEFT = 0X01,
    THREAD_STITCH_RIGHT = 0X02,
    THREAD_STITCH_TOP = 0X04,
    THREAD_STITCH_BOTTOM = 0X08,
    THREAD_STITCH_DIVIVDE = 0X10,
    THREAD_STITCH_BACKSLASH = 0X20,
} THREAD_STITCHES;

typedef enum
{
    BEAD_STITCH_NONE = 0x00,
    BEAD_STITCH_TYPE_DIVIDE = 0x01,
    BEAD_STITCH_TYPE_BACKSLASH = 0x02,
    BEAD_STITCH_TYPE_CENTER = 0x04,
} BEAD_STITCH_TYPE;


@protocol IStitch <NSObject>

@property (nonatomic, assign) BOOL Done;

@property (nonatomic, strong) ThreadMaterial* ThreadMaterial;
@property (nonatomic, assign) int8_t ThreadStitchType;

@property (nonatomic, strong) BeadMaterial* BeadMaterial;
@property (nonatomic, assign) int8_t BeadStitchType;

@end

@interface Stitch : NSObject<IStitch>

- (id) initWithThreadMaterial: (ThreadMaterial*) aThreadMaterail ThreadStitchType: (int8_t) aThreadStitchType BeadMaterial: (BeadMaterial*) aBeadMaterial BeadStitchType: (int8_t) aBeadStitchType;


- (BOOL) isEqual: (id) object;
- (BOOL) isEqualToStitch: (Stitch*) aStitch;
- (NSUInteger) hash;

@end

@interface Stitch (Serialization)

- (size_t) GetSerializedLength;
- (void) SerializeToBuffer: (void*) buffer WithThreadsCollection: (ThreadMaterialCollection*) threads AndBeadsCollection: (BeadMaterialCollection*) beads;
+ (id) DeserializeFromBuffer: (void*) buffer WithThreadsCollection: (ThreadMaterialCollection*) threads AndBeadsCollection: (BeadMaterialCollection*) beads;
@end