//
//  Thread.h
//  Cross Stitch Library
//
//  Created by 123 on 18.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol IThread <NSObject>

@property (readonly) CGColorRef color;
@property (readonly) NSInteger elements;

@end

@interface Thread : NSObject

@end
