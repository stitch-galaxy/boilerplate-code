//
//  BeadMaterial.h
//  Cross Stitch Library
//
//  Created by 123 on 20.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol IBeadMaterial <NSObject>

@property (nonatomic, strong) UIColor* Color;

@end

@interface BeadMaterial : NSObject<IBeadMaterial>

- (id) initWithColor: (UIColor*) aColor;

@end