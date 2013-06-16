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

- (NSURL*) getRequestUrlToLoadPage:(uint32_t) pageIndex OfSize: (uint32_t) pageSize
{
    NSURL *url = [NSURL URLWithString:@"https://stitchgalaxy.com/search.json"];
    return url;
}

- (void) parseJSONResponseForPage:(uint32_t) pageIndex OfSize: (uint32_t) pageSize JSON: (id) JSON
{
    [searchResults loadJSON:JSON forPage: pageIndex OfSize: pageSize];
    [delegate resultsLoaded];
}

- (void) loadPage: (uint32_t) pageIndex OfSize: (uint32_t) pageSize
{
    
    NSURL *url = [self getRequestUrlToLoadPage:pageIndex OfSize: pageSize];
    NSURLRequest *request = [NSURLRequest requestWithURL:url];
    
    AFJSONRequestOperation *operation = [AFJSONRequestOperation JSONRequestOperationWithRequest:request
                                                                                        success:^(NSURLRequest *request, NSHTTPURLResponse *response, id JSON)
    {
        [self parseJSONResponseForPage:pageIndex OfSize:pageSize JSON: JSON];
    }
                                                                                        failure:^(NSURLRequest *request , NSHTTPURLResponse *response , NSError *error , id JSON)
    {
        [delegate resultsLoadingError:error];
    }
                                         ];
    [operation start];
}

@end
