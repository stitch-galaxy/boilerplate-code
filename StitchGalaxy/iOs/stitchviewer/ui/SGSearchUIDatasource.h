//
//  SGSearchUIDatasource.h
//  stitchviewer
//
//  Created by 123 on 12.06.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SGDesignSearch.h"

@interface SGSearchUIDatasource : NSObject<UICollectionViewDataSource>

@property (nonatomic, assign, readwrite) NSUInteger itemsNumberPerSection;
@property (nonatomic, retain, readwrite) SGDesignSearch *search;

-(id) initWithSearchResultsAccessor: (SGDesignSearch*) aSearch ItemsCountPerSection: (NSUInteger) anItemsNumberPerSection;

@end

