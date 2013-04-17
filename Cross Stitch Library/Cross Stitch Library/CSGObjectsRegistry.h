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
- (CSGThreadsBlend*) getThreadsBlendWithThreadsInBlend: (NSArray* ) threadsInBlend;
//TODO: Prototype->Builder
- (CSGDesignCell*) getDesignCellPrototype;
- (CSGDesignCell*) getDesignCellByPrototype: (CSGDesignCell*) anPrototype;
- (uint32_t) getDesignCellIndex: (CSGDesignCell*) aCell;
- (CSGDesignCell*) getDesignCellByIndex: (uint32_t) anIndex;



- (CSGDesignPoint*) getDesignPoint: (CSGDesignPoint*) anInstance;
- (CSGDesignPoints*) getDesignPoints: (CSGDesignPoints*) anInstance;
- (CSGBackStitch*) getBackStitch: (CSGBackStitch*) anInstance;
- (CSGStraightStitch*) getStraightStitch: (CSGStraightStitch*) anInstance;
- (CSGDesign*) getDesign: (CSGDesign*) anInstance;

@end