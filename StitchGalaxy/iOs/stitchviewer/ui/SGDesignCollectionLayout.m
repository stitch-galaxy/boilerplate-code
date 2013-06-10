//
//  BHPhotoAlbumLayout.m
//  CollectionViewTutorial
//
//  Created by Bryan Hansen on 11/3/12.
//  Copyright (c) 2012 Bryan Hansen. All rights reserved.
//

#import "SGDesignCollectionLayout.h"
#import "SGDesignCollectionDecorationView.h"

static NSString * const SGDesignCollectionViewCellKind = @"DesignCell";
static NSString * const SGDesignCollectionViewTitleKind = @"DesignTitle";
static NSString * const SGDesignCollectionViewDecorationKind = @"Decoration";

@interface SGDesignCollectionLayout ()

@property (nonatomic, strong) NSDictionary *layoutInfo;

- (CGRect)frameForDesignCellAtIndexPath:(NSIndexPath *)indexPath;
- (CGRect)frameForDesignCellTitleAtIndexPath:(NSIndexPath *)indexPath;
- (CGRect)frameForDecorationView;

@end

@implementation SGDesignCollectionLayout

#pragma mark - Properties

@synthesize layoutInfo;

@synthesize itemSize = _itemSize;
@synthesize numberOfColumns = _numberOfColumns;
@synthesize numberOfRows = _numberOfRows;
@synthesize titleHeight = _titleHeight;

- (void)setNumberOfColumns:(NSInteger)numberOfColumns Rows: (NSInteger)numberOfRows
{
    if (_numberOfColumns == numberOfColumns && _numberOfRows == numberOfRows) return;
    
    _numberOfColumns = numberOfColumns;
    _numberOfRows = numberOfRows;
    
    [self invalidateLayout];
}

#pragma mark - Lifecycle

- (id)init
{
    self = [super init];
    if (self) {
        [self setup];
    }
    
    return self;
}

- (id)initWithCoder:(NSCoder *)aDecoder
{
    self = [super init];
    if (self) {
        [self setup];
    }
    
    return self;
}

- (void)setup
{
    //TODO: detect initial rotation to make setup correctly
    self.itemSize = CGSizeMake(125.0f, 125.0f);
    
    if (UIDeviceOrientationIsLandscape([UIApplication sharedApplication].statusBarOrientation))
    //if (UIDeviceOrientationIsLandscape([UIDevice currentDevice].orientation))
    {
        [self setNumberOfColumns:3 Rows:2];
    }
    else
    {
        [self setNumberOfColumns:2 Rows:3];
    }
    self.titleHeight = 26.0f;
    
    [self registerClass:[SGDesignCollectionDecorationView class] forDecorationViewOfKind:SGDesignCollectionViewDecorationKind];
}


#pragma mark - Layout

- (void)prepareLayout
{
    NSMutableDictionary *newLayoutInfo = [NSMutableDictionary dictionary];
    NSMutableDictionary *cellLayoutInfo = [NSMutableDictionary dictionary];
    NSMutableDictionary *titleLayoutInfo = [NSMutableDictionary dictionary];
    
    NSInteger sectionCount = [self.collectionView numberOfSections];
    NSIndexPath *indexPath = [NSIndexPath indexPathForItem:0 inSection:0];
    
    UICollectionViewLayoutAttributes *decorationViewAttributes = [UICollectionViewLayoutAttributes
                                                          layoutAttributesForDecorationViewOfKind:SGDesignCollectionViewDecorationKind withIndexPath:indexPath];
    decorationViewAttributes.frame = [self frameForDecorationView];
    
    newLayoutInfo[SGDesignCollectionViewDecorationKind] = @{indexPath: decorationViewAttributes};
    
    for (NSInteger section = 0; section < sectionCount; section++)
    {
        NSInteger itemCount = [self.collectionView numberOfItemsInSection:section];
        
        for (NSInteger item = 0; item < itemCount; item++)
        {
            indexPath = [NSIndexPath indexPathForItem:item inSection:section];
            
            UICollectionViewLayoutAttributes *itemAttributes =
                [UICollectionViewLayoutAttributes layoutAttributesForCellWithIndexPath:indexPath];
            
            itemAttributes.frame = [self frameForDesignCellAtIndexPath:indexPath];
            
            cellLayoutInfo[indexPath] = itemAttributes;
            
            if (indexPath.item == 0)
            {
                UICollectionViewLayoutAttributes *titleAttributes =
                    [UICollectionViewLayoutAttributes layoutAttributesForSupplementaryViewOfKind:SGDesignCollectionViewTitleKind withIndexPath:indexPath];
                titleAttributes.frame = [self frameForDesignCellTitleAtIndexPath:indexPath];
                
                titleLayoutInfo[indexPath] = titleAttributes;
            }
        }
    }
    
    newLayoutInfo[SGDesignCollectionViewCellKind] = cellLayoutInfo;
    newLayoutInfo[SGDesignCollectionViewTitleKind] = titleLayoutInfo;
    
    self.layoutInfo = newLayoutInfo;
}

- (CGSize)collectionViewContentSize
{
    return CGSizeMake(self.collectionView.bounds.size.width, self.collectionView.bounds.size.height);
}

- (NSArray *)layoutAttributesForElementsInRect:(CGRect)rect
{
    NSMutableArray *allAttributes = [NSMutableArray arrayWithCapacity:self.layoutInfo.count];
    
    [self.layoutInfo enumerateKeysAndObjectsUsingBlock:^(NSString *elementIdentifier, NSDictionary *elementsInfo, BOOL *stop)
    {
        [elementsInfo enumerateKeysAndObjectsUsingBlock:^(NSIndexPath *indexPath, UICollectionViewLayoutAttributes *attributes, BOOL *innerStop)
        {
            if (CGRectIntersectsRect(rect, attributes.frame))
            {
                [allAttributes addObject:attributes];
            }
        }];
    }];

    return allAttributes;
}

- (UICollectionViewLayoutAttributes *)layoutAttributesForItemAtIndexPath:(NSIndexPath *)indexPath
{
    return self.layoutInfo[SGDesignCollectionViewCellKind][indexPath];
}

- (UICollectionViewLayoutAttributes *)layoutAttributesForSupplementaryViewOfKind:(NSString *)kind
                                                                     atIndexPath:(NSIndexPath *)indexPath
{
    return self.layoutInfo[SGDesignCollectionViewTitleKind][indexPath];
}

- (UICollectionViewLayoutAttributes *)layoutAttributesForDecorationViewOfKind:(NSString*)decorationViewKind atIndexPath:(NSIndexPath *)indexPath
{
    return self.layoutInfo[SGDesignCollectionViewDecorationKind][indexPath];
}


#pragma mark - Private

- (CGRect)frameForDesignCellAtIndexPath:(NSIndexPath *)indexPath
{
    CGRect frame = [self frameForDesignCellTitleAtIndexPath:indexPath];
    frame.origin.y += frame.size.height;
    frame.size.height = self.itemSize.height;
    return frame;
}

- (CGRect)frameForDesignCellTitleAtIndexPath:(NSIndexPath *)indexPath
{
    
    NSInteger row = indexPath.section / self.numberOfColumns;
    NSInteger column = indexPath.section % self.numberOfColumns;
    
    
    CGFloat spacingY = self.collectionView.bounds.size.height - self.numberOfRows * (self.itemSize.height + self.titleHeight);
    spacingY = spacingY / (self.numberOfRows + 1);
    
    
    
    CGFloat spacingX = self.collectionView.bounds.size.width - (self.numberOfColumns * self.itemSize.width);
    spacingX = spacingX / (self.numberOfColumns + 1);
    
    CGFloat originX = floorf((self.itemSize.width + spacingX) * column + spacingX);
    CGFloat originY = floor((self.itemSize.height + self.titleHeight + spacingY) * row + spacingY);
    
    return CGRectMake(originX, originY, self.itemSize.width, self.titleHeight);

    
    
}

- (CGRect)frameForDecorationView
{
    CGSize size = [SGDesignCollectionDecorationView defaultSize];
    
    CGFloat originX = floorf((self.collectionView.bounds.size.width - size.width) * 0.5f);
    CGFloat originY = -size.height - 30.0f;
    
    return CGRectMake(originX, originY, size.width, size.height);
}

@end
