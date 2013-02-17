//
//  Stitch.h
//  Cross Stitch Library
//
//  Created by 123 on 17.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol IStitch <NSObject>

@property (copy, readonly) NSDate *date;
@property (readonly) NSInteger *rating;
@property (readonly) NSDecimal *price;
@property (readonly) NSInteger * complexity;

@property (strong) NSArray *threads;
@property (strong) NSArray *beads;

@end

@interface Stitch : NSObject

@end
