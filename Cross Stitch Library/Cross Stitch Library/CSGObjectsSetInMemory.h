//
//  CSGObjectsSetInMemory.h
//  Cross Stitch Library
//
//  Created by 123 on 15.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface CSGObjectsSetInMemory : NSObject

- (id) member: (id) anObject;

- (void) putObject: (id) anObject;

@property (nonatomic, retain) NSHashTable* objectsSet;

@end
