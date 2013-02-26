//
//  ICodec.h
//  Cross Stitch Library
//
//  Created by 123 on 19.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ArtWorkDesign.h"

@protocol ICodec <NSObject>

- (NSData*) Encode:(id<IArtWorkDesign>) anArtWork;
- (id<IArtWorkDesign>) Decode:(NSData*) aFileUrl;

@end