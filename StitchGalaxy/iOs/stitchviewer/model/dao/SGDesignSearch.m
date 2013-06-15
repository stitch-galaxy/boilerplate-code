//
//  SGDesignSearchResults.m
//  stitchviewer
//
//  Created by 123 on 12.06.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "SGDesignSearch.h"
#import "AFJSONRequestOperation.h"

@interface SGDesignSearch()

@property (nonatomic, retain, readwrite) SGSearchCriteria *criteria;

@end

@implementation SGDesignSearch

@synthesize delegate;
@synthesize criteria;


- (id) initWithCriteria: (SGSearchCriteria*) aCriteria
{
    criteria = aCriteria;
}


- (uint32_t) totalResultsLoaded
{
}

- (NSURL*) getRequestUrlToLoadRangeFrom:(uint32_t) from To: (uint32_t) to
{
    NSURL *url = [NSURL URLWithString:@"https://stitchgalaxy.com/search.json"];
    return url;
}


- (void) parseJSONResponseForRangeFrom:(uint32_t) from To: (uint32_t) to JSON: (id) JSON
{
    NSDictionary* searchResults = (NSDictionary *) JSON;
    uint32_t totalResultsLoaded = [searchResults objectForKey:@"overalResults"];
    
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

- (SGDesignSearchResult*) getSearchResult: (uint32_t) index
{
}

@end
