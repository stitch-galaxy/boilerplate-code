//
//  CSGCodec.h
//  Cross Stitch Library
//
//  Created by 123 on 28.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

@class CSGThread;
@class CSGObjectsRegistry;
@class CSGThreadInBlend;
@class CSGThreadsBlend;
@class CSGDesignCell;
@class CSGDesignPoint;
@class CSGDesignPoints;
@class CSGBackStitch;
@class CSGStraightStitch;
@class CSGDesign;

@interface CSGCodec : NSObject

//initialization
- (id) initWithObjectsRegistry: (CSGObjectsRegistry*) anRegistry;
//serilized data accessor
- (NSData*) data;

//serilization methods
- (void) serializeThread: (CSGThread*) aThread;
- (void) serializeThreadInBlend: (CSGThreadInBlend*) aThread;
- (void) serializeThreadsBlend: (CSGThreadsBlend*) aBlend;
- (void) serializeDesignCell: (CSGDesignCell*) aCell;
- (void) serializeDesignPoint: (CSGDesignPoint*) aPoint;
- (void) serializeDesignPoints: (CSGDesignPoints*) aPoints;
- (void) serializeBackStitch: (CSGBackStitch*) aStitch;
- (void) serializeStraightStitch: (CSGStraightStitch*) aStitch;
- (void) serializeDesign:(CSGDesign*) aDesign;

@end
