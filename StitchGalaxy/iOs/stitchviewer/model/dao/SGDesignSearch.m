//
//  SGDesignSearchResults.m
//  stitchviewer
//
//  Created by 123 on 12.06.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "SGDesignSearch.h"
#import "AFJSONRequestOperation.h"

@implementation SGDesignSearch

@synthesize delegate;
@synthesize criteria;
@synthesize searchResults;


- (id) init
{
    if (self = [super init])
    {
        searchResults = [[SGDesignSearchResults alloc] init];
    }
    return self;
}

- (NSURL*) getRequestUrlToLoadRangeFrom:(uint32_t) from To: (uint32_t) to
{
    NSURL *url = [NSURL URLWithString:@"https://stitchgalaxy.com/search.json"];
    return url;
}

- (void) parseJSONResponseForRangeFrom:(uint32_t) from To: (uint32_t) to JSON: (id) JSON
{
    [searchResults loadJSON:JSON];
    
    [delegate resultsLoaded];
}

- (void) loadRangeFrom: (uint32_t) from To: (uint32_t) to
{
    
    NSURL *url = [self getRequestUrlToLoadRangeFrom:from To: to];
    NSURLRequest *request = [NSURLRequest requestWithURL:url];
    
    AFJSONRequestOperation *operation = [AFJSONRequestOperation JSONRequestOperationWithRequest:request
                                                                                        success:^(NSURLRequest *request, NSHTTPURLResponse *response, id JSON)
    {
        [self parseJSONResponseForRangeFrom:from To:to JSON: JSON];
    }
                                                                                        failure:^(NSURLRequest *request , NSHTTPURLResponse *response , NSError *error , id JSON)
    {
        [delegate resultsLoadingError:error];
    }
                                         ];
    [operation start];
}

@end
