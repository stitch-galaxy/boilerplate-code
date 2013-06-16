//
//  SGDesignSearchResults.h
//  stitchviewer
//
//  Created by 123 on 12.06.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SGDesignSearchResults.h"
#import "SGSearchCriteria.h"

@protocol SGDesignResultsLoadingProgress <NSObject>

- (void) resultsLoaded;
- (void) resultsLoadingError: (NSError*) error;

@end

@interface SGDesignSearch : NSObject

@property (nonatomic, retain, readwrite) SGSearchCriteria *criteria;
@property (nonatomic, weak, readwrite) id<SGDesignResultsLoadingProgress> delegate;
@property (nonatomic, retain, readwrite) SGDesignSearchResults *searchResults;

- (void) loadPage: (uint32_t) pageIndex OfSize: (uint32_t) pageSize;


@end


