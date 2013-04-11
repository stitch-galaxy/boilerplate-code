#import "CSGThread.h"

@interface CSGThread ()

@property (nonatomic, retain) UIColor * CSG_color;

@end

@implementation CSGThread

@synthesize CSG_color;

- (UIColor*) color
{
    return CSG_color;
}

- (id) init
{
    NSAssert( false, @"Please use designated initializer" );
    
    return nil;
}

- (id) initWithColor: (UIColor*) aColor
{
    if (self = [super init])
    {
        self.CSG_color = aColor;
    }
    return self;
}

- (BOOL) isEqual: (id) object
{
    if (object == self)
    {
        return YES;
    }
    if (!object || ![object isKindOfClass:[self class]])
    {
        return NO;
    }
    
    return [self isEqualToCSGThread: object];
}

- (BOOL) isEqualToCSGThread: (CSGThread*) aThread
{
    if (self == aThread)
    {
        return YES;
    }
    if (![CSG_color isEqual: [aThread color]])
    {
        return NO;
    }
    
    return YES;
}

- (NSUInteger)hash
{
    NSUInteger hash = 17;
    hash = hash*31 + CSG_color.hash;
    
    return hash;
}

@end

@implementation CSGThread (Serialization)

- (size_t) serializedLength
{
    return sizeof(uint8_t) * 3;
}

- (void) serializeWithBinaryEncoder: (CSGBinaryEncoder *) anEncoder
{
    const CGFloat *components = CGColorGetComponents(CSG_color.CGColor);
    uint8_t red = lroundf(components[0] * 255.0);
    uint8_t green = lround(components[1] * 255.0);
    uint8_t blue = lround(components[2] * 255.0);
    
    uint8_t *buf = [anEncoder modifyBytes:sizeof(uint8_t)];
    *buf = red;
    buf = [anEncoder modifyBytes:sizeof(uint8_t)];
    *buf = green;
    buf = [anEncoder modifyBytes:sizeof(uint8_t)];
    *buf = blue;
}

+ (id) deserializeWithBinaryDecoder: (CSGBinaryDecoder*) anDecoder ObjectsRegistry: (CSGObjectsRegistry*) registry;
{
    const uint8_t *buf = [anDecoder readBytes:sizeof(uint8_t)];
    uint8_t iRed = *buf;
    
    buf = [anDecoder readBytes:sizeof(uint8_t)];
    uint8_t iGreen = *buf;
    
    buf = [anDecoder readBytes:sizeof(uint8_t)];
    uint8_t iBlue = *buf;
    
    
    CGFloat red = (CGFloat) iRed / 255.0;
    CGFloat green = (CGFloat) iGreen / 255.0;
    CGFloat blue = (CGFloat) iBlue / 255.0;
    UIColor* color = [[UIColor alloc] initWithRed: red green: green blue: blue alpha: 1.0];
    CSGThread* thread = [[CSGThread alloc] initWithColor: color];
    return [registry getThread: thread];
}

@end
