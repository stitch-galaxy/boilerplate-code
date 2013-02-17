//
//  StitchLibrary.h
//  Cross Stitch Library
//
//  Created by 123 on 17.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol StitchCategory <NSObject>

@property (nonatomic, readonly) NSString *name;

- (NSSet*) getSubCategories;


@end

@interface StitchLibrary : NSObject

- (NSSet*) getCategories;

@end
