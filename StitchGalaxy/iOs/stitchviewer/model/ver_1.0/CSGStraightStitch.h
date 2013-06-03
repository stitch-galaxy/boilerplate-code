//
//  CSGStraightStitch.h
//  Cross Stitch Library
//
//  Created by 123 on 06.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "CSGDesignPoints.h"
#import "CSGThreadsBlend.h"

@protocol CSGStraightStitch <NSObject>

@property (nonatomic, retain) CSGThreadsBlend *threadBlend;
@property (nonatomic, retain) CSGDesignPoints *curve;

@end

@interface CSGStraightStitch : NSObject<CSGStraightStitch>

-(id) initWithThreadsBlend: (CSGThreadsBlend*) aThreadBlend Curve:(CSGDesignPoints*) aCurve;

@end