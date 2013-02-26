//
//  ArtWork.m
//  Cross Stitch Library
//
//  Created by 123 on 18.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "ArtWorkDesign.h"

@implementation ArtWorkDesign

- (NSSet<IThreadMaterial>*) GetThreads
{
    return threads;
}

- (NSSet<IThreadMaterial>*) GetBeads
{
    return beads;
}

- (NSArray<IStitch>*) GetPicture;

- (int32_t) GetWidth;
- (int32_t) GetHeight;

-(id<IStitch>) GetStitchAtColum: (int32_t) column AndRow: (int32_t) row;

@end