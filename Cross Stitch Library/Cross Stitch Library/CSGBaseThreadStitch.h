//
//  CSGBaseThreadStitch.h
//  Cross Stitch Library
//
//  Created by 123 on 14.03.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "CSGThreadsBlend.h"

@protocol CSGBaseThreadStitch <NSObject>

@property (nonatomic, retain) CSGThreadsBlend *threadsBlend;

@end

@interface CSGBaseThreadStitch : NSObject<CSGBaseThreadStitch>

@end


@interface CSGBaseThreadStitch (Serialization)

@end