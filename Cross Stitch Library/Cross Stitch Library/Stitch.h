//
//  Stitch.h
//  Cross Stitch Library
//
//  Created by 123 on 17.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "ThreadStitchElement.h"
#import "BeadStitchElement.h"

@protocol IStitch <NSObject>

@property (nonatomic, assign) BOOL Done;

@property (nonatomic, strong) id <IThreadStitchElement> Thread;
@property (nonatomic, strong) id <IBeadStitchElement> Bead;

@end

@interface Stitch : NSObject<IStitch>

@end
