//
//  SGSearchUIDatasource.h
//  stitchviewer
//
//  Created by 123 on 12.06.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SGDesignSearchResults.h"

@interface SGSearchUIDatasource : NSObject<UICollectionViewDataSource>

@property (nonatomic, assign, readwrite) NSUInteger itemsNumberPerSection;
@property (nonatomic, retain, readwrite) SGDesignSearchResults *searchResults;

-(id) initWithSearchResultsAccessor: (SGDesignSearchResults*) aSearhcResults ItemsCountPerSection: (NSUInteger) anItemsNumberPerSection;

@end

