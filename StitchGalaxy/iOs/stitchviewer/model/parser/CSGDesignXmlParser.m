//
//  CSGDesignXmlParser.m
//  Cross Stitch Library
//
//  Created by 123 on 15.05.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGDesignXmlParser.h"

@interface CSGDesignXmlParser()

@property (nonatomic, retain) CSGXmlParser *parser;

@end

@implementation CSGDesignXmlParser

@synthesize parser;

- (void) parse
{
    [parser parse];
}

- (id) initWithUrl: (NSURL*) anUrl
{
    if (self = [super init])
    {
        parser = [[CSGXmlParser alloc] initWithUrl:anUrl delegate:self];
    }
    return self;
}

- (void) elementFound:(NSString*) elementName
       attributeNames:(NSArray*) attributeNames
      attributeValues:(NSArray*) attributeValues
{
    NSLog(@"Element found: %@", elementName);
    for (int i = 0; i < attributeNames.count; ++i)
    {
        NSLog(@"Attribute found: %@ value: %@", [attributeNames objectAtIndex:i], [attributeValues objectAtIndex:i]);
    }
}

- (void) endElement:(NSString*) elementName
               text:(NSString*)text
{
    NSLog(@"Element end: %@", elementName);
    NSLog(@"Element text: %@", text);
}

- (void) error:(NSError*) error
{
    NSLog(@"Error happened: %@", error);
}

- (void) endDocument
{
    NSLog(@"End document");
}

@end
