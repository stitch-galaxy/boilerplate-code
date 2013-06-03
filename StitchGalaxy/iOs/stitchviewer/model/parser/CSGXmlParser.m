//
//  CSGXmlParser.m
//  Cross Stitch Library
//
//  Created by 123 on 15.05.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGXmlParser.h"

//#import "AFHttpClient.h"
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


// called from libxml functions
@interface CSGXmlParser (LibXMLParserMethods)

- (void)elementFound:(const xmlChar *)localname
              prefix:(const xmlChar *)prefix
                 uri:(const xmlChar *)URI
      namespaceCount:(int)namespaceCount
          namespaces:(const xmlChar **)namespaces
      attributeCount:(int)attributeCount
defaultAttributeCount:(int)defaultAttributeCount
          attributes:(xmlSAX2Attributes *)attributes;

- (void)endElement:(const xmlChar *)localname
            prefix:(const xmlChar *)prefix
               uri:(const xmlChar *)URI;

- (void)charactersFound:(const xmlChar *)characters
                 length:(int)length;

- (void)parsingError:(const char *)msg, ...;

- (void)endDocument;

@end

// Forward reference. The structure is defined in full at the end of the file.
static xmlSAXHandler simpleSAXHandlerStruct;

@interface CSGXmlParser()
{
    xmlParserCtxtPtr xmlParserContext;
}

//url
@property(nonatomic, retain) NSURL* url;

//flags
@property(atomic, assign) BOOL done;

//callback
@property(nonatomic, assign) id<CSGXmlParserDelegate> delegate;

//element text
@property (nonatomic, retain) NSMutableArray *texts;

@end


@implementation CSGXmlParser

//Url
@synthesize url;
//flags
@synthesize done;

//callback
@synthesize delegate;

//element text holder
@synthesize texts;

-(id) initWithUrl: (NSURL*) anUrl delegate:(id<CSGXmlParserDelegate>)anDelegate
{
    if (self = [super init])
    {
        url = anUrl;
        delegate = anDelegate;
        done = false;
        texts = [[NSMutableArray alloc] init];
    }
    return self;
}

- (void) parse
{
    // create a connection
    NSURLRequest *request = [NSURLRequest requestWithURL:url];
    NSURLConnection *connection = [[NSURLConnection alloc]
                            initWithRequest:request
                            delegate:self];
    // This creates a context for "push" parsing in which chunks of data that are
    // not "well balanced" can be passed to the context for streaming parsing.
    // The handler structure defined above will be used for all the parsing. The
    // second argument, self, will be passed as user data to each of the SAX
    // handlers. The last three arguments are left blank to avoid creating a tree
    // in memory.
    xmlParserContext = xmlCreatePushParserCtxt(&simpleSAXHandlerStruct,
                                               (__bridge void *)(self),
                                               NULL,
                                               0,
                                               NULL);
    if(connection != nil)
    {
        do
        {
            [[NSRunLoop currentRunLoop] runMode:NSDefaultRunLoopMode beforeDate:[NSDate distantFuture]];
        } while (!done);
    }
}



#pragma mark Parsing Function Callback Methods

- (void)elementFound:(const xmlChar *)localname
              prefix:(const xmlChar *)prefix
                 uri:(const xmlChar *)URI
      namespaceCount:(int)namespaceCount
          namespaces:(const xmlChar **)namespaces
      attributeCount:(int)attributeCount
defaultAttributeCount:(int)defaultAttributeCount
          attributes:(xmlSAX2Attributes *)attributes
{
    
    NSUInteger sElementNameLength = xmlStrlen(localname);
    NSString *elementName = [[NSString alloc] initWithBytes:localname length:sElementNameLength encoding:NSUTF8StringEncoding];
    
    NSMutableArray *attributeNames = [[NSMutableArray alloc] initWithCapacity:attributeCount];
    NSMutableArray *attributeValues = [[NSMutableArray alloc] initWithCapacity:attributeCount];
    
    for(int i = 0; i < attributeCount; ++i)
    {
        NSUInteger sAttributeNameLength = xmlStrlen(attributes[i].localname);
        NSString *attributeName = [[NSString alloc] initWithBytes:attributes[i].localname length:sAttributeNameLength encoding:NSUTF8StringEncoding];
        [attributeNames addObject:attributeName];
        
        //It works so, we can not use xmlStrlen
        //NSUInteger sAttributeValueLength = xmlStrlen(attributes[i].value);
        NSUInteger sAttributeValueLength = attributes[i].end - attributes[i].value;
        NSString *attributeValue = [[NSString alloc] initWithBytes:attributes[i].value length:sAttributeValueLength encoding:NSUTF8StringEncoding];
        [attributeValues addObject:attributeValue];
    }
    
    [delegate elementFound:elementName
            attributeNames:attributeNames
           attributeValues:attributeValues];
    
    [texts addObject:[[NSMutableString alloc] init]];
}

- (void)endElement:(const xmlChar *)localname prefix:(const xmlChar *)prefix uri:(const xmlChar *)URI
{
    NSUInteger sElementNameLength = xmlStrlen(localname);
    NSString *elementName = [[NSString alloc] initWithBytes:localname length:sElementNameLength encoding:NSUTF8StringEncoding];
    
    NSString *text = texts.lastObject;
    [texts removeLastObject];
    [delegate endElement:elementName text:text];
}

- (void)charactersFound:(const xmlChar *)characters length:(int)length
{
    NSString *value = [[NSString alloc] initWithBytes:(const void *)characters length:length encoding: NSUTF8StringEncoding];
    [texts.lastObject appendString:value];
}

- (void)parsingError:(const char *)msg, ...
{
    NSString *format = [[NSString alloc] initWithBytes:msg length:strlen(msg)
                                              encoding:NSUTF8StringEncoding];
    
    CFStringRef resultString = NULL;
    va_list argList;
    va_start(argList, msg);
    resultString = CFStringCreateWithFormatAndArguments(NULL, NULL,
                                                        (CFStringRef)format,
                                                        argList);
    va_end(argList);

    NSDictionary *userInfo = [NSDictionary dictionaryWithObject:(NSString*)CFBridgingRelease(resultString)
                                                         forKey:@"error_message"];
    NSError *anError = [NSError errorWithDomain:@"ParsingDomain"
                                           code:101
                                       userInfo:userInfo];
    [delegate error:anError];
}

-(void)endDocument
{
    [delegate endDocument];
}

#pragma mark NSURLConnection Delegate methods

- (NSCachedURLResponse *)connection:(NSURLConnection *)connection
                  willCacheResponse:(NSCachedURLResponse *)cachedResponse {
    // returning nil prevents the response from being cached
    return nil;
}

// Forward errors to the delegate.
- (void)connection:(NSURLConnection *)connection
  didFailWithError:(NSError *)anError
{
    [delegate error:anError];
    done = YES;
}

// Called when a chunk of data has been downloaded.
- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data
{
    // Process the downloaded chunk of data.
    xmlParseChunk(xmlParserContext, (const char *)[data bytes], [data length], 0);
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{
    // Signal the context that parsing is complete by passing "1" as the last parameter.
    xmlParseChunk(xmlParserContext, NULL, 0, 1);
    done = YES;
}

- (void) dealloc
{
    xmlFreeParserCtxt(xmlParserContext);
    xmlParserContext = NULL;
    
    self.delegate = nil;
}

@end

#pragma mark SAX Parsing Callbacks

static void startElementSAX(void *ctx, const xmlChar *localname, const xmlChar *prefix,
                            const xmlChar *URI, int nb_namespaces, const xmlChar **namespaces,
                            int nb_attributes, int nb_defaulted, const xmlChar **attributes) {
    [((__bridge CSGXmlParser *)ctx) elementFound:localname prefix:prefix uri:URI
                                     namespaceCount:nb_namespaces namespaces:namespaces
                                     attributeCount:nb_attributes defaultAttributeCount:nb_defaulted
                                         attributes:(xmlSAX2Attributes*)attributes];
}

static void	endElementSAX(void *ctx, const xmlChar *localname, const xmlChar *prefix,
                          const xmlChar *URI) {
    [((__bridge CSGXmlParser *)ctx) endElement:localname prefix:prefix uri:URI];
}

static void	charactersFoundSAX(void *ctx, const xmlChar *ch, int len) {
    [((__bridge CSGXmlParser *)ctx) charactersFound:ch length:len];
}

static void errorEncounteredSAX(void *ctx, const char *msg, ...) {
    va_list argList;
    va_start(argList, msg);
    [((__bridge CSGXmlParser *)ctx) parsingError:msg, argList];
}

static void endDocumentSAX(void *ctx) {
    [((__bridge CSGXmlParser *)ctx) endDocument];
}

static xmlSAXHandler simpleSAXHandlerStruct = {
    NULL,                       /* internalSubset */
    NULL,                       /* isStandalone   */
    NULL,                       /* hasInternalSubset */
    NULL,                       /* hasExternalSubset */
    NULL,                       /* resolveEntity */
    NULL,                       /* getEntity */
    NULL,                       /* entityDecl */
    NULL,                       /* notationDecl */
    NULL,                       /* attributeDecl */
    NULL,                       /* elementDecl */
    NULL,                       /* unparsedEntityDecl */
    NULL,                       /* setDocumentLocator */
    NULL,                       /* startDocument */
    endDocumentSAX,             /* endDocument */
    NULL,                       /* startElement*/
    NULL,                       /* endElement */
    NULL,                       /* reference */
    charactersFoundSAX,         /* characters */
    NULL,                       /* ignorableWhitespace */
    NULL,                       /* processingInstruction */
    NULL,                       /* comment */
    NULL,                       /* warning */
    errorEncounteredSAX,        /* error */
    NULL,                       /* fatalError //: unused error() get all the errors */
    NULL,                       /* getParameterEntity */
    NULL,                       /* cdataBlock */
    NULL,                       /* externalSubset */
    XML_SAX2_MAGIC,             // initialized? not sure what it means just do it
    NULL,                       // private
    startElementSAX,            /* startElementNs */
    endElementSAX,              /* endElementNs */
    NULL,                       /* serror */
};

