//
//  SGDesignSearchResults.h
//  stitchviewer
//
//  Created by 123 on 12.06.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SGDesignSearchResult.h"
#import "SGSearchCriteria.h"

@protocol SGDesignResultsLoadingProgress <NSObject>

- (void) resultsLoaded;
- (void) resultsLoadingError: (NSError*) error;

@end

@interface SGDesignSearch : NSObject

- (id) initWithCriteria: (SGSearchCriteria*) aCriteria;

@property (nonatomic, weak, readwrite) id<SGDesignResultsLoadingProgress> delegate;

- (uint32_t) totalResultsLoaded;
- (void) loadRangeFrom: (uint32_t) from To: (uint32_t) to;
- (SGDesignSearchResult*) getSearchResult: (uint32_t) index;

@end


