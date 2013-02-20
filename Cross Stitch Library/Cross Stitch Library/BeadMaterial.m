//
//  BeadMaterial.m
//  Cross Stitch Library
//
//  Created by 123 on 20.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "BeadMaterial.h"

@implementation BeadMaterial

@synthesize Color = color;

- (id) initWithColor: (UIColor*) aColor
{
    if (self = [super init])
    {
        self.Color = aColor;
    }
    return self;
}

@end

