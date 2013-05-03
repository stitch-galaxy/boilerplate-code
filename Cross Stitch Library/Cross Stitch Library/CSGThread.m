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
