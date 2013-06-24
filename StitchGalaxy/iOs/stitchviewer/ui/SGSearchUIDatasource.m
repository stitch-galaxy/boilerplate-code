//
//  SGSearchUIDatasource.m
//  stitchviewer
//
//  Created by 123 on 12.06.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "SGSearchUIDatasource.h"
#import "SGDesignViewerCell.h"
#import "SGCollectionViewKinds.h"
#import "UIImageView+AFNetworking.h"

@implementation SGSearchUIDatasource

@synthesize itemsNumberPerSection;
@synthesize search;


-(id) initWithSearch: (SGDesignSearch*) aSearch ItemsCountPerSection: (NSUInteger) anItemsNumberPerSection
{
    self = [super init];
    if (self)
    {
        itemsNumberPerSection = anItemsNumberPerSection;
        search = aSearch;
    }
    return self;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return itemsNumberPerSection;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    SGDesignViewerCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:SGCollectionViewCellIdentifier forIndexPath:indexPath];
    
    uint32_t itemIndex = indexPath.section * itemsNumberPerSection + indexPath.item;
    SGDesignSearchResult *searchResult = [search.searchResults getSearchResult: itemIndex];
    
    [cell.imageView setImageWithURL:searchResult.imageSmallUrl placeholderImage:[UIImage imageNamed:@"placeholder"]];
    
    return cell;
}


- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView
{
    return (search.searchResults.total - 1) / itemsNumberPerSection + 1;
}

- (UICollectionReusableView *)collectionView:(UICollectionView *)collectionView viewForSupplementaryElementOfKind:(NSString *)kind atIndexPath:(NSIndexPath *)indexPath
{
    return nil;
}

@end
