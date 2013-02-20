//
//  Thread.m
//  Cross Stitch Library
//
//  Created by 123 on 18.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "ThreadStitchElement.h"

@implementation ThreadStitchElement

@synthesize ThreadMaterial = threadMaterial;

@synthesize StitchType = stitchType;

@end

@implementation ThreadStitchElementBuilder

@synthesize Build = threadStitchElement;

- (id)init
{
    if (self = [super init])
    {
        threadStitchElement = [[ThreadStitchElement alloc] init];
    }
    return self;
}

- (ThreadStitchElementBuilder*) AttachToThreadMaterial: (ThreadMaterial*) aThreadMaterial
{
    threadStitchElement.ThreadMaterial = aThreadMaterial;
    
    return self;
}

- (ThreadStitchElementBuilder*) SelectStitchType: (int8_t) aStitchType
{
    threadStitchElement.StitchType = aStitchType;
    
    return self;
}

@end