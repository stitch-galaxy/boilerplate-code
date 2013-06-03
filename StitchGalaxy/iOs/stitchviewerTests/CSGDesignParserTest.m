//
//  CSGDesignParserTest.m
//  Cross Stitch Library
//
//  Created by 123 on 06.05.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGDesignParserTest.h"
#import "CSGDesignParser.h"
#import "CSGDesignXmlParser.h"

@implementation CSGDesignParserTest

- (void)setUp
{
    [super setUp];
}

- (void)tearDown
{
    [super tearDown];
}

- (void) test
{
    NSBundle *testBundle = [NSBundle bundleForClass:[self class]];
    NSString *sUrl = [testBundle pathForResource:@"TestDesign" ofType:@"xml"];
    NSURL* url = [[NSURL alloc] initFileURLWithPath:sUrl];
    
    CSGDesignXmlParser *parser = [[CSGDesignXmlParser alloc] initWithUrl:url];
    [parser parse];
}

//- (void) testParser
//{
//    NSBundle *testBundle = [NSBundle bundleForClass:[self class]];
//    NSString *sUrl = [testBundle pathForResource:@"TestDesign" ofType:@"xml"];
//    NSURL* url = [[NSURL alloc] initFileURLWithPath:sUrl];
//    id<CSGDesignParserDelegate> delegate = self;
//    CSGDesignParser* parser = [[CSGDesignParser alloc] initWithUrl:url delegate:delegate];
//    [parser getDesignData];
//    [NSThread sleepForTimeInterval:5.0];
//}
//
//- (void)parser:(CSGDesignParser *)parser addDesign:(CSGDesign *)design
//{
//}
//
//- (void)parser:(CSGDesignParser *)parser encounteredError:(NSError *)error
//{
//}
//
//- (void)parserFinished:(CSGDesignParser *)parser
//{
//}

@end
