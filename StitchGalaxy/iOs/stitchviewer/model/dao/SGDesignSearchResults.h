//
//  SGDesignSearchResults.h
//  stitchviewer
//
//  Created by 123 on 12.06.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol SGDesignResultsLoadingProgress <NSObject>

- (void) resultsLoaded;
- (void) resultsLoadingError: (NSString*) error;
- (void) searchFinished;

@end

@interface SGDesignSearchResult : NSObject

@property (nonatomic, retain, readwrite) NSString *imageSmallUrl;
@property (nonatomic, retain, readwrite) NSString *imageLargeUrl;
@property (nonatomic, assign, readwrite) uint32_t width;
@property (nonatomic, assign, readwrite) uint32_t height;
@property (nonatomic, assign, readwrite) NSDecimal price;

@end

@interface SGDesignSearchResults : NSObject

@property (nonatomic, weak, readwrite) id<SGDesignResultsLoadingProgress> delegate;

- (uint32_t) totalResultsLoaded;
- (void) loadRangeFrom: (uint32_t) from To: (uint32_t) to;
- (SGDesignSearchResult*) getSearchResult: (uint32_t) index;

@end
