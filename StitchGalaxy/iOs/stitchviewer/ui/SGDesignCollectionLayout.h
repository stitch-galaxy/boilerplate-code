//
//  BHPhotoAlbumLayout.h
//  CollectionViewTutorial
//
//  Created by Bryan Hansen on 11/3/12.
//  Copyright (c) 2012 Bryan Hansen. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SGDesignCollectionLayout : UICollectionViewLayout

@property (nonatomic) CGSize itemSize;
@property (nonatomic) NSInteger numberOfColumns;
@property (nonatomic) NSInteger numberOfRows;
@property (nonatomic) CGFloat titleHeight;

- (void)setNumberOfColumns:(NSInteger)numberOfColumns Rows: (NSInteger)numberOfRows;

@end
