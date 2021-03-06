//
//  SGCollectionViewController.m
//  stitchviewer
//
//  Created by 123 on 07.06.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "SGCollectionViewController.h"
#import "SGDesignCollectionLayout.h"
#import "SGDesignViewerCell.h"

#import "UIImageView+AFNetworking.h"


static NSString * const PhotoCellIdentifier = @"DesignCell";

@interface SGCollectionViewController ()

@property (nonatomic, weak) IBOutlet SGDesignCollectionLayout *photoAlbumLayout;

@end

@implementation SGCollectionViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    
    UIImage *patternImage = [UIImage imageNamed:@"viewerbg"];
    self.collectionView.backgroundColor = [UIColor colorWithPatternImage:patternImage];
    
    //TODO: replace this with configuration in IB
    [self.collectionView registerClass:[SGDesignViewerCell class] forCellWithReuseIdentifier:PhotoCellIdentifier];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return 1;
}

// The cell that is returned must be retrieved from a call to -dequeueReusableCellWithReuseIdentifier:forIndexPath:
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    SGDesignViewerCell *photoCell = [collectionView dequeueReusableCellWithReuseIdentifier:PhotoCellIdentifier forIndexPath:indexPath];
    
    [photoCell.imageView setImageWithURL:[NSURL URLWithString:@"http://stitchgalaxy.com/design.jpg"] placeholderImage:[UIImage imageNamed:@"placeholder"]];
    
    return photoCell;
}

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView
{
    return 5;
}

- (void) willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration
{
    if (UIInterfaceOrientationIsLandscape(toInterfaceOrientation))
    {
        [self.photoAlbumLayout setNumberOfColumns: 3 Rows: 2];
    }
    else
    {
        [self.photoAlbumLayout setNumberOfColumns: 2 Rows: 3];
    }
}


@end
