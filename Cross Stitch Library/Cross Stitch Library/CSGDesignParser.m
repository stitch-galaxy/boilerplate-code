//
//  CSGDesignParser.m
//  Cross Stitch Library
//
//  Created by 123 on 04.05.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "CSGDesignParser.h"
#import "CSGDesign.h"

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
@interface CSGDesignParser (LibXMLParserMethods)

- (void)elementFound:(const xmlChar *)localname prefix:(const xmlChar *)prefix
                 uri:(const xmlChar *)URI namespaceCount:(int)namespaceCount
          namespaces:(const xmlChar **)namespaces attributeCount:(int)attributeCount
defaultAttributeCount:(int)defaultAttributeCount attributes:(xmlSAX2Attributes *)attributes;
- (void)endElement:(const xmlChar *)localname prefix:(const xmlChar *)prefix uri:(const xmlChar *)URI;
- (void)charactersFound:(const xmlChar *)characters length:(int)length;
- (void)parsingError:(const char *)msg, ...;
- (void)endDocument;

@end

// Forward reference. The structure is defined in full at the end of the file.
static xmlSAXHandler simpleSAXHandlerStruct;


@interface CSGDesignParser()
{
    xmlParserCtxtPtr xmlParserContext;
}

//url
@property(nonatomic, retain) NSURL* url;
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

@property (nonatomic, retain) NSMutableString *title;
@property (nonatomic, assign) BOOL parsingTitle;

@end

@implementation CSGDesignParser

@synthesize title;
@synthesize parsingTitle;


//Url
@synthesize url;
//callback
@synthesize delegate;
//flags
@synthesize error;
@synthesize done;
//connection and threading
@synthesize connection;
@synthesize retrieverQueue = _retrieverQueue;
//Business objects
@synthesize currentDesign;


- (NSOperationQueue *)retrieverQueue
{
	if(nil == _retrieverQueue)
    {
        // lazy creation of the queue for retrieving the data
		_retrieverQueue = [[NSOperationQueue alloc] init];
		_retrieverQueue.maxConcurrentOperationCount = 1;
	}
	return _retrieverQueue;
}

- (void)getDesignData
{
    // make an operation so we can push it into the queue
	SEL method = @selector(parseForData);
	NSInvocationOperation *op = [[NSInvocationOperation alloc] initWithTarget:self selector:method object:nil];
	[self.retrieverQueue addOperation:op];
}

-(id) initWithUrl: (NSURL*) anUrl delegate:(id<CSGDesignParserDelegate>)anDelegate
{
    if (self = [super init])
    {
        url = anUrl;
        delegate = anDelegate;
        done = false;
        error = false;
    }
    return self;
}

- (BOOL)parseWithLibXML2Parser
{
    BOOL success = NO;
    // create a connection
    NSURLRequest *request = [NSURLRequest requestWithURL:url];
    NSURLConnection *con = [[NSURLConnection alloc]
                            initWithRequest:request
                            delegate:self];
    self.connection = con;
    // This creates a context for "push" parsing in which chunks of data that are
    // not "well balanced" can be passed to the context for streaming parsing.
    // The handler structure defined above will be used for all the parsing. The
    // second argument, self, will be passed as user data to each of the SAX
    // handlers. The last three arguments are left blank to avoid creating a tree
    // in memory.
    xmlParserContext = xmlCreatePushParserCtxt(&simpleSAXHandlerStruct, (__bridge void *)(self), NULL, 0, NULL);
    if(self.connection != nil)
    {
        do
        {
            [[NSRunLoop currentRunLoop] runMode:NSDefaultRunLoopMode beforeDate:[NSDate distantFuture]];
        } while (!done && !self.error);
    }
    if(self.error)
    {
        NSLog(@"parsing error");
        [self.delegate parser:self encounteredError:nil];
    }
    else
    {
        success = YES;
    }
    return success;
}

// this method is fired by the operation created in
// getDesignData so it is on an alternate thread
- (BOOL)parseForData {
    BOOL success = NO;
    //TODO: check how it works
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;

    success = [self parseWithLibXML2Parser];
    
    return success;
}

#pragma mark Parsing Function Callback Methods

/*
 Sample entry
 <design href="http://google.com">
 <title>Batterfly</title>
 </entry>
 */

static const char *kDesignElementName = "design";
static NSUInteger kDesignElementNameLength = 7;

static const char *kHrefAttributeName = "href";
static NSUInteger kHrefAttributeNameLength = 5;

static const char *kTitleElementName = "title";
static NSUInteger kTitleElementNameLength = 6;

- (void)elementFound:(const xmlChar *)localname prefix:(const xmlChar *)prefix
                 uri:(const xmlChar *)URI namespaceCount:(int)namespaceCount
          namespaces:(const xmlChar **)namespaces attributeCount:(int)attributeCount
defaultAttributeCount:(int)defaultAttributeCount attributes:(xmlSAX2Attributes *)attributes
{
    if(0 == strncmp((const char *)localname, kDesignElementName, kDesignElementNameLength))
    {
        for(int i = 0;i < attributeCount;i++)
        {
            if (0 == strncmp((const char *)attributes[i].localname, kHrefAttributeName, kHrefAttributeNameLength))
            {
                int valueLength = (int) (attributes[i].end - attributes[i].value);
                NSString *value = [[NSString alloc] initWithBytes:attributes[i].value length:valueLength encoding:NSUTF8StringEncoding];
            }
        }
        //TODO: put business logic here
        //self.currentDesign = [[CSGDesign alloc] init];
    }

    else if (0 == strncmp((const char *)localname, kTitleElementName, kTitleElementNameLength))
    {
        title = [[NSMutableString alloc] init];
        parsingTitle = YES;
    }
}

- (void)endElement:(const xmlChar *)localname prefix:(const xmlChar *)prefix uri:(const xmlChar *)URI
{
    if(0 == strncmp((const char *)localname, kTitleElementName, kTitleElementNameLength))
    {
        //Title formed
        //title = nil; to release object
        parsingTitle = NO;
    }
    else if(0 == strncmp((const char *)localname, kDesignElementName, kDesignElementNameLength))
    {
        SEL selector = @selector(parser:addEarthquake:);
        NSMethodSignature *sig = [(id)self.delegate methodSignatureForSelector:selector];
        if(nil != sig && [self.delegate respondsToSelector:selector])
        {
            NSInvocation *invocation = [NSInvocation invocationWithMethodSignature:sig];
            [invocation retainArguments];
            [invocation setTarget:self.delegate];
            [invocation setSelector:selector];
            CSGDesignParser *parser = self;
            [invocation setArgument:&parser atIndex:2];
            [invocation setArgument:&currentDesign atIndex:3];
            [invocation performSelectorOnMainThread:@selector(invoke) withObject:NULL waitUntilDone:NO];
        }
    }
}

- (void)charactersFound:(const xmlChar *)characters length:(int)length
{
    if(parsingTitle)
    {
        NSString *value = [[NSString alloc] initWithBytes:(const void *)characters length:length encoding: NSUTF8StringEncoding];
        [title appendString:value];
    }
}

- (void)parsingError:(const char *)msg, ... {
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
    
    SEL selector = @selector(parse:encounteredError:);
    NSMethodSignature *sig = [(id)self.delegate methodSignatureForSelector:selector];
    if(nil != sig && [self.delegate respondsToSelector:selector]) {
        NSInvocation *invocation = [NSInvocation invocationWithMethodSignature:sig];
        [invocation retainArguments];
        [invocation setTarget:self.delegate];
        [invocation setSelector:selector];
        CSGDesignParser *parser = self;
        [invocation setArgument:&parser atIndex:2];
        [invocation setArgument:&anError atIndex:3];
        [invocation performSelectorOnMainThread:@selector(invoke) withObject:NULL waitUntilDone:NO];
    }
    done = YES;
}

-(void)endDocument
{
    [(id)[self delegate] performSelectorOnMainThread:@selector(parserFinished:)
                                          withObject:self
                                       waitUntilDone:NO];
    //TODO: check how it works
    [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
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
    SEL selector = @selector(parser:encounteredError:);
    NSMethodSignature *sig = [(id)self.delegate methodSignatureForSelector:selector];
    if(nil != sig && [self.delegate respondsToSelector:selector])
    {
        NSInvocation *invocation = [NSInvocation invocationWithMethodSignature:sig];
        [invocation retainArguments];
        [invocation setTarget:self.delegate];
        [invocation setSelector:selector];
        CSGDesignParser *parser = self;
        [invocation setArgument:&parser atIndex:2];
        [invocation setArgument:&anError atIndex:3];
        [invocation performSelectorOnMainThread:@selector(invoke) withObject:NULL waitUntilDone:NO];
    }
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
    self.connection = nil;
    
    xmlFreeParserCtxt(xmlParserContext);
    xmlParserContext = NULL;
    
    self.currentDesign = nil;
    self.delegate = nil;
    self.retrieverQueue = nil;
}

@end


#pragma mark SAX Parsing Callbacks

static void startElementSAX(void *ctx, const xmlChar *localname, const xmlChar *prefix,
                            const xmlChar *URI, int nb_namespaces, const xmlChar **namespaces,
                            int nb_attributes, int nb_defaulted, const xmlChar **attributes) {
    [((__bridge CSGDesignParser *)ctx) elementFound:localname prefix:prefix uri:URI
                             namespaceCount:nb_namespaces namespaces:namespaces
                             attributeCount:nb_attributes defaultAttributeCount:nb_defaulted
                                 attributes:(xmlSAX2Attributes*)attributes];
}

static void	endElementSAX(void *ctx, const xmlChar *localname, const xmlChar *prefix,
                          const xmlChar *URI) {
    [((__bridge CSGDesignParser *)ctx) endElement:localname prefix:prefix uri:URI];
}

static void	charactersFoundSAX(void *ctx, const xmlChar *ch, int len) {
    [((__bridge CSGDesignParser *)ctx) charactersFound:ch length:len];
}

static void errorEncounteredSAX(void *ctx, const char *msg, ...) {
    va_list argList;
    va_start(argList, msg);
    [((__bridge CSGDesignParser *)ctx) parsingError:msg, argList];
}

static void endDocumentSAX(void *ctx) {
    [((__bridge CSGDesignParser *)ctx) endDocument];
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

