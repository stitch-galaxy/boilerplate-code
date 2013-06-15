//
//  SGDesignSearchBuilder.m
//  stitchviewer
//
//  Created by 123 on 16.06.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "SGSearchCriteria.h"

@implementation SGSearchCriteria

@synthesize wordsToSearch;
@synthesize selectedCategories;
@synthesize searchOrder;
@synthesize maxResults;

- (id) init
{
    if (self = [super init])
    {
        searchOrder = SG_SEARCH_RECENT;
        maxResults = 0;
        wordsToSearch = [[NSArray alloc] init];
        selectedCategories = [[NSSet alloc] init];
    }
    return self;
}

@end
