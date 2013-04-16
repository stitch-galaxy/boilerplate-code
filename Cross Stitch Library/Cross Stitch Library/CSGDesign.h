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

@end

@interface CSGDesign : NSObject<CSGDesign>

-(id) initWithWidth: (uint32_t) aWidth Height: (uint32_t) aHeight Cells: (NSArray*) aCells BackStitches: (NSArray*) aBackStitches StraightStitches: (NSArray*) aStraightStitches;

@end

@interface CSGDesign (Serialization)

- (size_t) serializedLength;
- (void) serializeWithBinaryEncoder: (CSGBinaryEncoder *) anEncoder ObjectsRegistry: (CSGObjectsRegistry*) registry;
+ (id) deserializeWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder ObjectsRegistry: (CSGObjectsRegistry*) registry;

@end
