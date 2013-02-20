//
//  Bead.h
//  Cross Stitch Library
//
//  Created by 123 on 18.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "StitchElement.h"
#import "BeadMaterial.h"
#import "ThreadMaterial.h"

typedef enum
{
    BEAD_STITCH_TYPE_DIVIDE,
    BEAD_STITCH_TYPE_BACKSLASH,
    BEAD_STITCH_TYPE_CENTER,
} BEAD_STITCH_TYPE;

@protocol IBeadStitchElement <IStitchElement>

@property (nonatomic, weak) BeadMaterial* BeadMaterial;
@property (nonatomic, weak) ThreadMaterial* ThreadMaterial;
@property (nonatomic, assign) int8_t BeadStitchType;

@end

@interface BeadStitchElement : NSObject<IBeadStitchElement>

@end

@interface BeadStitchElementBuilder : NSObject

@property (readonly, retain) BeadStitchElement* Build;

- (BeadStitchElementBuilder*) AttachToBeadMaterial: (BeadMaterial*) aBeadMaterial;
- (BeadStitchElementBuilder*) AttachToThreadMaterial: (ThreadMaterial*) aThreadMaterial;
- (BeadStitchElementBuilder*) SelectBeadStitchType: (int8_t) aBeadStitchType;

@end

