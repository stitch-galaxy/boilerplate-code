//
//  Stitch.m
//  Cross Stitch Library
//
//  Created by 123 on 17.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "Stitch.h"

typedef enum
{
    STITCH_TYPE_LEFT = 0x1,
    STITCH_TYPE_RIGHT = 0x2,
    STITCH_TYPE_TOP = 0x4,
    STITCH_TYPE_BOTTOM = 0x8,
    STITCH_TYPE_DIVIDE = 0x10,
    STITCH_TYPE_BACKSLASH = 0x20,
} StitchType;
