//
//  SGDesignSearchResults.m
//  stitchviewer
//
//  Created by 123 on 16.06.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "SGDesignSearchResults.h"

static NSString * const SG_JSON_SEARCH_ITEMS_KEY = @"items";
static NSString * const SG_JSON_SEARCH_ITEMS_NUMBER = @"total";


@interface SGDesignSearchResults()

@property (nonatomic, retain, readwrite) NSArray *results;

@end

@implementation SGDesignSearchResults

@synthesize results;
@synthesize total;

- (id) init
{
    if (self = [super init])
    {
        results = [[NSArray alloc] init];
        total = 0;
    }
    return self;
}

- (void) loadJSON: (id) JSON
{
    NSDictionary* searchResults = (NSDictionary *) JSON;
    NSNumber *t = [searchResults objectForKey:SG_JSON_SEARCH_ITEMS_NUMBER];
    total = t.unsignedLongLongValue;
    
    
    NSArray* items = [searchResults objectForKey:SG_JSON_SEARCH_ITEMS_NUMBER];
    for(NSDictionary *item in items)
    {
        SGDesignSearchResult *result = [[SGDesignSearchResult alloc] init];
        [result loadJSON:item];
    }
}

- (SGDesignSearchResult*) getSearchResult: (uint32_t) index
{
    return [results objectAtIndex:index];
}

@end
