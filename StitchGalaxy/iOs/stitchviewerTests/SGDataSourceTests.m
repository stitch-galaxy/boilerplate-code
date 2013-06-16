//
//  SGDataSourceTests.m
//  stitchviewer
//
//  Created by 123 on 16.06.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import <Foundation/NSJSONSerialization.h>

#import "SGDataSourceTests.h"
#import "SGDesignSearch.h"

@implementation SGDataSourceTests

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
    NSString *sUrl = [testBundle pathForResource:@"SearchResult" ofType:@"json"];
    NSURL* url = [[NSURL alloc] initFileURLWithPath:sUrl];
    NSData* data = [NSData dataWithContentsOfURL:url];
    
    NSError* error;
    NSJSONReadingOptions options = 0;
    NSDictionary* json = [NSJSONSerialization JSONObjectWithData:data options:options error:&error];
    if (error)
    {
        STFail(@"Cannot parse JSON");
    }
    
    SGDesignSearchResults *results = [[SGDesignSearchResults alloc] init];
    [results loadJSON:json];
}

@end
