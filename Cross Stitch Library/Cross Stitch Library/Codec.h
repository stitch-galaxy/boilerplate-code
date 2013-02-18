//
//  ICodec.h
//  Cross Stitch Library
//
//  Created by 123 on 19.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ArtWork.h"

@protocol ICodec <NSObject>

- (void) Encode:(id<IArtWork>) artWork ToFile: (NSString*) fileUrl;
- (id<IArtWork>) Decode:(NSString*) fileUrl;

@end