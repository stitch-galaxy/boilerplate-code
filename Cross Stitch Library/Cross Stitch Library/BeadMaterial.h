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
@property (nonatomic, copy) NSDecimalNumber* Size;

@end

@interface BeadMaterial : NSObject<IBeadMaterial>


- (id) initWithColor: (UIColor *) aColor AndSize: (NSDecimalNumber *) aSize;

- (BOOL) isEqual: (id) object;
- (BOOL) isEqualToBeadMaterial: (BeadMaterial *) aBeadMaterial;
- (NSUInteger) hash;

@end

@interface BeadMaterial (Serialization)

- (size_t) GetSerializedLength;
- (void) SerializeToBuffer: (void*) buffer;
+ (id) DeserializeFromBuffer: (void*) buffer;

@end