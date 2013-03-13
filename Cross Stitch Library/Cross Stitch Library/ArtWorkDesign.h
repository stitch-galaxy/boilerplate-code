//
//  ArtWork.h
//  Cross Stitch Library
//
//  Created by 123 on 18.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <stdint.h>

#import "Stitch.h"
#import "CSGThreadsPalette.h"

@protocol IArtWorkDesign <NSObject>

@property (nonatomic, strong) CSGThreadsPalette* Threads;

- (uint32_t) GetWidth;
- (uint32_t) GetHeight;

-(id<IStitch>) GetStitchAtRow: (uint32_t) row AndColumn: (uint32_t) column;
- (void) SetSticth: (id<IStitch>) stitch AtRow: (uint32_t) row AndColumn: (uint32_t) column;

@end

@interface ArtWorkDesign : NSObject<IArtWorkDesign, NSCoding>
{
    uint32_t width;
    uint32_t height;
    NSPointerArray* picture;
}

- (id) initWithWidth: (uint32_t) aWidth AndHeight: (uint32_t) anHeight Threads: (CSGThreadsPalette *) aThreads;

- (void)encodeWithCoder:(NSCoder *)aCoder;
- (id)initWithCoder:(NSCoder *)aDecoder;

@end

@interface ArtWorkDesign(Serialization)

- (size_t) GetSerializedLength;
- (void) SerializeToBuffer: (void*) buffer;
+ (id) DeserializeFromBuffer: (const void*) buffer;

@end
