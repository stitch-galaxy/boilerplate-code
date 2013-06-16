//
//  SGDesignSearchResults.h
//  stitchviewer
//
//  Created by 123 on 16.06.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SGDesignSearchResult.h"

@interface SGDesignSearchResults : NSObject

- (void) loadJSON: (id) JSON;
- (SGDesignSearchResult*) getSearchResult: (uint32_t) index;

@property (nonatomic, assign, readonly) uint64_t total;

@end
