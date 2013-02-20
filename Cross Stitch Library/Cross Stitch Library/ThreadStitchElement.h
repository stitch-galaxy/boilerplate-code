//
//  Thread.h
//  Cross Stitch Library
//
//  Created by 123 on 18.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "StitchElement.h"
#import "ThreadMaterial.h"

typedef enum
{
    THREAD_STITCH_LEFT = 0X01,
    THREAD_STITCH_RIGHT = 0X02,
    THREAD_STITCH_TOP = 0X04,
    THREAD_STITCH_BOTTOM = 0X08,
    THREAD_STITCH_DIVIVDE = 0X10,
    THREAD_STITCH_BACKSLASH = 0X20,
} THREAD_STITCHES;

@protocol IThreadStitchElement <IStitchElement>

@property (nonatomic, weak) ThreadMaterial* ThreadMaterial;
@property (nonatomic, assign) int8_t StitchType;

@end

@interface ThreadStitchElement : NSObject<IThreadStitchElement>

@end

@interface ThreadStitchElementBuilder : NSObject

@property (readonly, retain) ThreadStitchElement* Build;

- (ThreadStitchElementBuilder*) AttachToThreadMaterial: (ThreadMaterial*) aThreadMaterial;
- (ThreadStitchElementBuilder*) SelectStitchType: (int8_t) aStitchType;

@end
