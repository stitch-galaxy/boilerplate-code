//
//  CSGDesignParser.h
//  Cross Stitch Library
//
//  Created by 123 on 04.05.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>


@class CSGDesign;
@protocol CSGDesignParserDelegate;

@interface CSGDesignParser : NSObject

- (void)getDesignData;
-(id) initWithUrl: (NSURL*) anUrl delegate: (id<CSGDesignParserDelegate>) delegate;

@end


@protocol CSGDesignParserDelegate <NSObject>

- (void)parser:(CSGDesignParser *)parser addDesign:(CSGDesign *)design;
- (void)parser:(CSGDesignParser *)parser encounteredError:(NSError *)error;
- (void)parserFinished:(CSGDesignParser *)parser;

@end