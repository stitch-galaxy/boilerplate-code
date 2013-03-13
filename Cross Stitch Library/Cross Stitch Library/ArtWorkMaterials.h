//
//  ArtWorkMaterials.h
//  Cross Stitch Library
//
//  Created by 123 on 19.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "CSGThread.h"

@protocol IArtWorkMaterialsCalulator <NSObject>

- (float) GetThreadMaterialLenghtInMeters: (id<CSGThread>) aThreadMaterial;

@end
