//
//  CSGBackStitch.h
//  Cross Stitch Library
//
//  Created by 123 on 06.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "CSGDesignPoints.h"
#import "CSGThreadsBlend.h"

@protocol CSGBackStitch <NSObject>

@property (nonatomic, retain) CSGThreadsBlend *threadBlend;
@property (nonatomic, retain) CSGDesignPoints *curve;

@end

@interface CSGBackStitch : NSObject<CSGBackStitch>

-(id) initWithThreadsBlend: (CSGThreadsBlend*) aThreadBlend Curve:(CSGDesignPoints*) aCurve;

@end

@interface CSGBackStitch (Serialization)

- (size_t) serializedLength;
- (void) serializeWithBinaryEncoder: (CSGBinaryEncoder *) anEncoder ObjectsRegistry: (CSGObjectsRegistry*) registry;
+ (id) deserializeWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder ObjectsRegistry: (CSGObjectsRegistry*) registry;

@end


