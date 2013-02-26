//
//  ThreadMaterial.h
//  Cross Stitch Library
//
//  Created by 123 on 20.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "SerializableElement.h"

@protocol IThreadMaterial <ISerializableElement>

@property (nonatomic, strong) UIColor* Color;

@end

@interface ThreadMaterial : NSObject<IThreadMaterial>

- (id) initWithColor: (UIColor*) aColor;

- (BOOL) isEqual: (id) object;
- (BOOL) isEqualToThreadMaterial: (ThreadMaterial*) aBeadMaterial;
- (NSUInteger) hash;

@end
