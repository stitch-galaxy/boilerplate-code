//
//  CSGObjectsSetInMemory.h
//  Cross Stitch Library
//
//  Created by 123 on 15.04.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface CSGMemorySetWithIndex : NSObject

- (id) member: (id) anObject;

- (void) putObject: (id) anObject;

- (id) getObjectByIndex: (uint32_t) anIndex;

-(uint32_t) getIndexByObject: (id) anObject;

@property (nonatomic, retain) NSHashTable* objectsSet;
@property (nonatomic, retain) NSMutableArray* objectsArray;
@property (nonatomic, retain) NSMapTable* objectToIndexMap;

@end
