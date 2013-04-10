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
#import "CSGThreadsPalette.h"

@protocol CSGDesign <NSObject>

//Cross stitch
@property (nonatomic, retain) CSGThreadsPalette* palette;
@property (nonatomic, assign) uint32_t width;
@property (nonatomic, assign) uint32_t height;
@property (nonatomic, retain) NSArray* cells;
@property (nonatomic, retain) NSArray* backStitches;
@property (nonatomic, retain) NSArray* straightStitches;

@end

@interface CSGDesignCell : NSObject<CSGDesign>

@end

@interface CSGDesign (Serialization)

- (size_t) serializedLength;
- (void) serializeWithBinaryEncoder: (CSGBinaryEncoder *) anEncoder;
- (id) initWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder;

@end
