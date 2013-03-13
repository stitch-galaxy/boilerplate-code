//
//  ThreadsBlend.h
//  Cross Stitch Library
//
//  Created by 123 on 13.03.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <stdint.h>

#import "CSGThread.h"

@protocol IThreadsBlend <NSObject>

- (uint8_t) GetThreadsCount;
- (id<CSGThread>) GetThreadAtIndex: (uint8_t) aThreadIndex;

@end

@interface ThreadsBlend : NSObject<IThreadsBlend>
{
    NSHashTable* blends;
}

@end

@interface ThreadsBlend(Serialization)

@end


