//
//  BeadMaterial.h
//  Cross Stitch Library
//
//  Created by 123 on 20.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "SerializableElement.h"

@protocol IBeadMaterial <ISerializableElement>

@property (nonatomic, strong) UIColor* Color;
@property (nonatomic, copy) NSDecimalNumber* Size;

@end

@interface BeadMaterial : NSObject<IBeadMaterial>

- (id) initWithColor: (UIColor*) aColor;

- (BOOL) isEqual: (id) object;
- (BOOL) isEqualToBeadMaterial: (BeadMaterial*) aBeadMaterial;
- (NSUInteger) hash;


@end