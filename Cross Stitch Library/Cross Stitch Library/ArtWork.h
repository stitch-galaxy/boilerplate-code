//
//  ArtWork.h
//  Cross Stitch Library
//
//  Created by 123 on 26.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "ArtWorkInfo.h"
#import "ArtWorkDesign.h"
#import "ArtWorkMaterials.h"

@protocol IArtWork <NSObject>

@property (nonatomic, strong) id<IArtWorkInfo> Info;

@property (nonatomic, strong) id<IArtWorkDesign> Design;

-(id<IArtWorkMaterialsCalulator>) GetMaterialsCalculator;

@end

@interface ArtWork : NSObject

@end
