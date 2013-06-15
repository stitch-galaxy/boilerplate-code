//
//  SGDesignSearchBuilder.h
//  stitchviewer
//
//  Created by 123 on 16.06.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum
{
    SG_SEARCH_RECENT = 0,
    SG_SEARCH_POPULAR,
    SG_SEARCH_TOP_RATED,
    SG_SEARCH_RANDOM,
} SGSearchOrder;

@interface SGSearchCriteria : NSObject

@property (nonatomic, retain, readwrite) NSArray* wordsToSearch;
@property (nonatomic, retain, readwrite) NSSet* selectedCategories;
@property (nonatomic, assign, readwrite) SGSearchOrder searchOrder;
@property (nonatomic, assign, readwrite) uint32_t maxResults;

@end
