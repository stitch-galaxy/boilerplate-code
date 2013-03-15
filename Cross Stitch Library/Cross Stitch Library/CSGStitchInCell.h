//
//  CSGCrossStitch.h
//  Cross Stitch Library
//
//  Created by 123 on 15.03.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGBaseThreadStitch.h"

@protocol CSGStitchInCell <CSGBaseThreadStitch>
@end

@interface CSGStitchInCell : CSGBaseThreadStitch<CSGStitchInCell>

- (id) initWithThreadsBlend: (CSGThreadsBlend *) aThreadsBlend;

@end
