//
//  CSGLinearStitch.h
//  Cross Stitch Library
//
//  Created by 123 on 03.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "CSGDesignPoint.h"

@protocol CSGDesignPoints <NSObject>

@property (nonatomic, retain) NSArray* points;

@end

@interface CSGDesignPoints : NSObject<CSGDesignPoints>

- (id) initWithPoints: (NSArray*) aPoints;

@end
