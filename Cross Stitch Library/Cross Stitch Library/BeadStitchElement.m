//
//  Bead.m
//  Cross Stitch Library
//
//  Created by 123 on 18.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "BeadStitchElement.h"

@implementation BeadStitchElement

@synthesize BeadMaterial = beadMaterial;
@synthesize ThreadMaterial = threadMaterial;
@synthesize BeadStitchType = beadStitchType;

@end

@implementation BeadStitchElementBuilder

@synthesize Build = beadStitchElement;

- (id)init
{
    if (self = [super init])
    {
        beadStitchElement = [[BeadStitchElement alloc] init];
    }
    return self;
}

- (BeadStitchElementBuilder*) AttachToBeadMaterial: (BeadMaterial*) aBeadMaterial
{
    beadStitchElement.BeadMaterial = aBeadMaterial;
    
    return self;
}

- (BeadStitchElementBuilder*) AttachToThreadMaterial: (ThreadMaterial*) aThreadMaterial
{
    beadStitchElement.ThreadMaterial = aThreadMaterial;
    
    return self;
}

- (BeadStitchElementBuilder*) SelectBeadStitchType: (int8_t) aBeadStitchType
{
    beadStitchElement.BeadStitchType = aBeadStitchType;
    
    return self;
}

@end