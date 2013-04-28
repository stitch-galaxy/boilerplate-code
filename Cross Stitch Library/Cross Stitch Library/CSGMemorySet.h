//
//  CSGMemeorySet.h
//  Cross Stitch Library
//
//  Created by 123 on 17.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface CSGMemorySet : NSObject

- (id) member: (id) anObject;
- (void) putObject: (id) anObject;
- (uint32_t) count;

-(id) initWithCapacity: (uint32_t) aCapacity;

@end