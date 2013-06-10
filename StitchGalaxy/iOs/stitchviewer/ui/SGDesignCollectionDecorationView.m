//
//  BHEmblemView.m
//  CollectionViewTutorial
//
//  Created by Bryan Hansen on 11/6/12.
//  Copyright (c) 2012 Bryan Hansen. All rights reserved.
//

#import "SGDesignCollectionDecorationView.h"

static NSString * const SGDesignCollectionDecorationImageName = @"decoration";

@implementation SGDesignCollectionDecorationView

+ (CGSize)defaultSize
{
    return [UIImage imageNamed:SGDesignCollectionDecorationImageName].size;
}

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        UIImage *image = [UIImage imageNamed:SGDesignCollectionDecorationImageName];
        UIImageView *imageView = [[UIImageView alloc] initWithImage:image];
        imageView.frame = self.bounds;
        
        [self addSubview:imageView];
    }
    return self;
}

@end
