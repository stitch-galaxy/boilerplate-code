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

- (CSGThread*) getThread: (CSGThread*) anInstance;
- (CSGThreadInBlend*) getThreadInBlend: (CSGThreadInBlend*) anInstance;
- (CSGThreadsBlend*) getThreadsBlend: (CSGThreadsBlend*) anInstance;
- (CSGStitchInCell*) getStitchInCell: (CSGStitchInCell*) anInstance;
- (CSGDesignCell*) getDesignCell: (CSGDesignCell*) anInstance;
- (CSGDesignPoint*) getDesignPoint: (CSGDesignPoint*) anInstance;
- (CSGDesignPoints*) getDesignPoints: (CSGDesignPoints*) anInstance;
- (CSGBackStitch*) getBackStitch: (CSGBackStitch*) anInstance;
- (CSGStraightStitch*) getStraightStitch: (CSGStraightStitch*) anInstance;
- (CSGDesign*) getDesign: (CSGDesign*) anInstance;

@end