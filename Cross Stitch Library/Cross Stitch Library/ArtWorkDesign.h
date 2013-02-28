//
//  ArtWork.h
//  Cross Stitch Library
//
//  Created by 123 on 18.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <stdint.h>

#import "Stitch.h"


@protocol IArtWorkDesign <NSObject>
 
- (int32_t) GetWidth;
- (int32_t) GetHeight;

-(id<IStitch>) GetStitchAtColum: (int32_t) column AndRow: (int32_t) row;

@end

@interface ArtWorkDesign : NSObject<IArtWorkDesign>
{
    NSMutableArray<IStitch>* picture;
}

@end
