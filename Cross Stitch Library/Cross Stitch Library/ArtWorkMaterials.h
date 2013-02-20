//
//  ArtWorkMaterials.h
//  Cross Stitch Library
//
//  Created by 123 on 19.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "ThreadMaterial.h"
#import "BeadMaterial.h"

@protocol IArtWorkMaterialsCalulator <NSObject>

- (float) GetThreadMaterialLenghtInMeters: (id<IThreadMaterial>) aThreadMaterial;
- (int32_t) GetBeadElementsAmount: (id<IBeadMaterial>) aBeadMaterial;

@end
