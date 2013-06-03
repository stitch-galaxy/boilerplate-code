//
//  CSGDesignCell.h
//  Cross Stitch Library
//
//  Created by 123 on 16.03.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "CSGBackStitch.h"
#import "CSGStraightStitch.h"
#import "CSGDesignCell.h"

@protocol CSGDesign <NSObject>

@property (nonatomic, assign) uint32_t width;
@property (nonatomic, assign) uint32_t height;
@property (nonatomic, retain) NSArray* cells;
@property (nonatomic, retain) NSArray* backStitches;
@property (nonatomic, retain) NSArray* straightStitches;

- (bool) isDoneAtX: (uint32_t) x Y: (uint32_t) y;
- (void) setDone: (bool) isDone AtX: (uint32_t) x Y: (uint32_t) y;
- (void) setDone: (bool) isDone FromX: (uint32_t) fromX Y: (uint32_t) fromY ToX:(uint32_t) toX Y: (uint32_t) toY;

- (void*) doneBitMask;

@end

@interface CSGDesign : NSObject<CSGDesign>

-(id) initWithWidth: (uint32_t) aWidth Height: (uint32_t) aHeight Cells: (NSArray*) aCells BackStitches: (NSArray*) aBackStitches StraightStitches: (NSArray*) aStraightStitches Done: (uint8_t*) aDone;

@end
