//
//  CSGNeedlePerforationCoordinate.h
//  Cross Stitch Library
//
//  Created by 123 on 03.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "CSGBinaryEncoder.h"
#import "CSGBinaryDecoder.h"

@protocol CSGDesignPoint <NSObject>

-(uint32_t) x;
-(uint32_t) y;
-(uint8_t) cellX;
-(uint8_t) cellY;
-(uint8_t) cellDenominator;

@end

@interface CSGDesignPoint : NSObject<CSGDesignPoint>

- (id) initWithX: (uint32_t) aX Y: (uint32_t) anY CellX: (uint8_t) aCellX CellY: (uint8_t) aCellY CellDenominator: (uint8_t) aCellDenominator;

@end


@interface CSGDesignPoint (Serialization)

- (size_t) serializedLength;
- (void) serializeWithBinaryEncoder: (CSGBinaryEncoder *) anEncoder;
- (id) initWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder;

@end