//
//  CSGDesignXmlParser.h
//  Cross Stitch Library
//
//  Created by 123 on 15.05.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "CSGXmlParser.h"

@interface CSGDesignXmlParser : NSObject<CSGXmlParserDelegate>

- (void) parse;
- (id) initWithUrl: (NSURL*) anUrl;

@end
