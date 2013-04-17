//
//  ThreadMaterialCollection.h
//  Cross Stitch Library
//
//  Created by 123 on 27.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <stdint.h>

@class CSGThread;
@class CSGThreadInBlend;
@class CSGThreadsBlend;
@class CSGStitchInCell;
@class CSGDesignCell;
@class CSGDesignPoint;
@class CSGDesignPoints;
@class CSGBackStitch;
@class CSGStraightStitch;
@class CSGDesign;

@interface CSGObjectsRegistry : NSObject

- (CSGThread*) getThreadWithColor: (UIColor*) aColor;
-(CSGThreadInBlend*) getThreadInBlendWithThread: (CSGThread*) aThread FlossCount: (uint8_t) aFlossCount;

//- (CSGThreadsBlend*) getThreadsBlend: (CSGThreadsBlend*) anInstance;
- (CSGThreadsBlend*) getThreadsBlendWithThreadsInBlend: (NSArray* ) threadsInBlend;

- (CSGStitchInCell*) getStitchInCell: (CSGStitchInCell*) anInstance;
- (CSGDesignCell*) getDesignCell: (CSGDesignCell*) anInstance;
- (CSGDesignPoint*) getDesignPoint: (CSGDesignPoint*) anInstance;
- (CSGDesignPoints*) getDesignPoints: (CSGDesignPoints*) anInstance;
- (CSGBackStitch*) getBackStitch: (CSGBackStitch*) anInstance;
- (CSGStraightStitch*) getStraightStitch: (CSGStraightStitch*) anInstance;
- (CSGDesign*) getDesign: (CSGDesign*) anInstance;

@end