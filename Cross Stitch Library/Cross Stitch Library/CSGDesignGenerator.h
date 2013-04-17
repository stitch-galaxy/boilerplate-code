//
//  CSGDesignGenerator.h
//  Cross Stitch Library
//
//  Created by 123 on 15.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "CSGThread.h"
#import "CSGThreadsBlend.h"
#import "CSGBinaryCoding.h"
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
- (CSGThread*) generateThread: (CSGObjectsRegistry*) registry;
- (CSGThreadInBlend*) generateThreadInBlend: (CSGObjectsRegistry*) registry;
- (CSGDesignCell*) generateDesignCell: (CSGObjectsRegistry*) registry;
- (CSGThreadsBlend*) generateThreadsBlend: (CSGObjectsRegistry*) registry;
- (CSGDesignPoint*) generateDesignCoordinate: (CSGObjectsRegistry*) registry;
- (CSGDesignPoints*) generateDesignPoints: (CSGObjectsRegistry*) registry;
- (CSGBackStitch*) generateBackStitch: (CSGObjectsRegistry*) registry;
- (CSGStraightStitch*) generateStraightStitch: (CSGObjectsRegistry*) registry;
- (CSGDesign*) generateDesign: (CSGObjectsRegistry*) registry;

@end
