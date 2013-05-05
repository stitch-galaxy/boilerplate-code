//
//  CSGDesignParser.h
//  Cross Stitch Library
//
//  Created by 123 on 04.05.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <libxml/tree.h>

// define a struct to hold the attribute info
struct _xmlSAX2Attributes {
    const xmlChar* localname;
    const xmlChar* prefix;
    const xmlChar* uri;
    const xmlChar* value;
    const xmlChar* end;
};
typedef struct _xmlSAX2Attributes xmlSAX2Attributes;

@class CSGDesign;
@protocol CSGDesignParserDelegate;

@interface CSGDesignParser : NSObject
{
////    CSGDesign *currentDesign;
////    id<CSGDesignParserDelegate> delegate;
////    NSURLConnection *connection;
//    BOOL done;
//    BOOL error;
//    
    xmlParserCtxtPtr xmlParserContext;
//    NSOperationQueue *retrieverQueue;
}

//url
@property(nonatomic, retain) NSString* url;
//callback
@property(nonatomic, assign) id<CSGDesignParserDelegate> delegate;
//flags
@property(nonatomic, assign) BOOL error;
@property(nonatomic, assign) BOOL done;
//connection and threading
@property(nonatomic, retain) NSURLConnection *connection;
@property(nonatomic, retain) NSOperationQueue *retrieverQueue;
//business object
@property(nonatomic, retain) CSGDesign *currentDesign;

- (void)getDesignData;
-(id) initWithUrl: (NSString*) anUrl;

@end


@protocol CSGDesignParserDelegate <NSObject>

- (void)parser:(CSGDesignParser *)parser addDesign:(CSGDesign *)design;
- (void)parser:(CSGDesignParser *)parser encounteredError:(NSError *)error;
- (void)parserFinished:(CSGDesignParser *)parser;

@end