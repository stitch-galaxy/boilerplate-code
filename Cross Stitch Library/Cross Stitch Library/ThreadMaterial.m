//
//  ThreadMaterial.m
//  Cross Stitch Library
//
//  Created by 123 on 20.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "ThreadMaterial.h"

@implementation ThreadMaterial

@synthesize Color = color;

- (id) initWithColor: (UIColor*) aColor
{
    if (self = [super init])
    {
        self.Color = aColor;
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
    
    return [self isEqualToThreadMaterial: object];
}

- (BOOL) isEqualToThreadMaterial: (ThreadMaterial*) aThreadMaterial
{
    if (self == aThreadMaterial)
    {
        return YES;
    }
    if (![color isEqual: aThreadMaterial.Color])
    {
        return NO;
    }
    
    return YES;
}

- (NSUInteger)hash
{
    NSUInteger hash = 17;
    hash = hash*31 + color.hash;
    
    return hash;
}

@end

@implementation ThreadMaterial (Serialization)

- (size_t) GetSerializedLength
{
    return BYTE_SIZE * 3;
}

- (void) SerializeToBuffer: (void*) buffer
{
    uint8_t *buf = (uint8_t*) buffer;
    
    const CGFloat *components = CGColorGetComponents(color.CGColor);
    uint8_t red = lroundf(components[0] * 255.0);
    uint8_t green = lround(components[1] * 255.0);
    uint8_t blue = lround(components[2] * 255.0);
    
    *buf = red;
    *(buf + 1) = green;
    *(buf + 2) = blue;    
}

+ (id) DeserializeFromBuffer: (void*) buffer
{
    uint8_t *buf = (uint8_t*) buffer;
    
    uint8_t iRed = *buf;
    uint8_t iGreen = *(buf + 1);
    uint8_t iBlue = *(buf + 2);
    
    
    CGFloat red = (CGFloat) iRed / 255.0;
    CGFloat green = (CGFloat) iGreen / 255.0;
    CGFloat blue = (CGFloat) iBlue / 255.0;
    UIColor* color = [[UIColor alloc] initWithRed: red green: green blue: blue alpha: 1.0];
    ThreadMaterial* material = [[ThreadMaterial alloc] initWithColor: color];
    return material;
}

@end
