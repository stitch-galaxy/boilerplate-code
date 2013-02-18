//
//  Stitch.h
//  Cross Stitch Library
//
//  Created by 123 on 17.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "Thread.h"
#import "Bead.h"

@protocol IStitch <NSObject>

@property (nonatomic, strong) id <IThread> Thread;
@property (nonatomic, strong) id <IBead> Bead;

- (BOOL) HasLeftStitch;
- (BOOL) HasRightStitch;
- (BOOL) HasTopStitch;
- (BOOL) HasBottomStitch;
- (BOOL) HasDivideStitch;
- (BOOL) HasBackslashStitch;

@end
