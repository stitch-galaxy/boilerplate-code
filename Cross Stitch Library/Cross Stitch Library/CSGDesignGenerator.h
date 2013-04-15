//
//  CSGDesignGenerator.h
//  Cross Stitch Library
//
//  Created by 123 on 15.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>


#import "CSGThread.h"
#import "CSGThreadsPalette.h"
#import "CSGThreadsBlend.h"
#import "CSGBinaryCoding.h"
#import "CSGStitchInCell.h"
#import "CSGDesignCell.h"
#import "CSGDesignPoint.h"
#import "CSGDesignPoints.h"
#import "CSGBackStitch.h"
#import "CSGStraightStitch.h"
#import "CSGDesign.h"

@interface CSGDesignGenerator : NSObject
{
    NSMutableArray *threads;
}

- (id) init;
- (CSGThread*) generateThread;
- (CSGThreadInBlend*) generateThreadInBlend;
- (CSGStitchInCell*) randomStitchInCell;
- (CSGDesignCell*) generateDesignCell;
- (CSGThreadsBlend*) generateThreadsBlend;
- (CSGDesignPoint*) generateDesignCoordinate;
- (CSGDesignPoints*) generateDesignPoints;
- (CSGBackStitch*) generateBackStitch;
- (CSGStraightStitch*) generateStraightStitch;
- (CSGDesign*) generateDesign;

@end
