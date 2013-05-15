//
//  CSGXmlParser.h
//  Cross Stitch Library
//
//  Created by 123 on 15.05.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol CSGXmlParserDelegate;

@interface CSGXmlParser : NSObject

- (void) parse;
- (id) initWithUrl: (NSURL*) anUrl delegate: (id<CSGXmlParserDelegate>) delegate;

@end

@protocol CSGXmlParserDelegate <NSObject>

- (void) elementFound:(NSString*) elementName
       attributeNames:(NSArray*) attributeNames
       attributeValues:(NSArray*) attributeValues;

- (void) endElement:(NSString*) elementName
               text:(NSString*)text;

- (void) error:(NSError*) error;

- (void) endDocument;
 
@end


