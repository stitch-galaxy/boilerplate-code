//
//  ThreadMaterial.m
//  Cross Stitch Library
//
//  Created by 123 on 20.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

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

- (void) serializeToBuffer: (void*) buffer
{
    uint8_t *buf = (uint8_t*) buffer;
    
    const CGFloat *components = CGColorGetComponents(CSG_color.CGColor);
    uint8_t red = lroundf(components[0] * 255.0);
    uint8_t green = lround(components[1] * 255.0);
    uint8_t blue = lround(components[2] * 255.0);
    
    *buf = red;
    *(buf + 1) = green;
    *(buf + 2) = blue;    
}

+ (id) deserializeFromBuffer: (const void*) buffer
{
    uint8_t *buf = (uint8_t*) buffer;
    
    uint8_t iRed = *buf;
    uint8_t iGreen = *(buf + 1);
    uint8_t iBlue = *(buf + 2);
    
    CGFloat red = (CGFloat) iRed / 255.0;
    CGFloat green = (CGFloat) iGreen / 255.0;
    CGFloat blue = (CGFloat) iBlue / 255.0;
    UIColor* color = [[UIColor alloc] initWithRed: red green: green blue: blue alpha: 1.0];
    CSGThread* material = [[CSGThread alloc] initWithColor: color];
    return material;
}

@end
