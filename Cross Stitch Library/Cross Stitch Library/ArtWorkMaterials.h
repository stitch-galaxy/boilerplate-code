//
//  ArtWorkMaterials.h
//  Cross Stitch Library
//
//  Created by 123 on 19.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Thread.h"
#import "Bead.h"

@protocol IArtWorkMaterials <NSObject>

- (NSDictionary<IThread>*) ThreadMaterials;
- (NSDictionary<IBead>*) BeadMaterials;

@end
