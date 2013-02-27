//
//  BeadMaterial.m
//  Cross Stitch Library
//
//  Created by 123 on 20.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "BeadMaterial.h"

@implementation BeadMaterial

@synthesize Color = color;

@synthesize Size = size;


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
    
    return [self isEqualToBeadMaterial:object];
}

- (BOOL) isEqualToBeadMaterial: (BeadMaterial*) aBeadMaterial
{
    if (self == aBeadMaterial)
    {
        return YES;
    }
    if (![color isEqual: aBeadMaterial.Color])
    {
        return NO;
    }
    
    if (![size isEqual: aBeadMaterial.Size])
    {
        return NO;
    }
    
    return YES;
}

- (NSUInteger)hash
{
    NSUInteger hash = 17;
    hash = hash*31 + color.hash;
    hash = hash*31 + size.hash;
    
    return hash;
}

- (id) initWithColor: (UIColor *) aColor AndSize: (NSDecimalNumber *) aSize
{
    if (self = [super init])
    {
        self.Color = aColor;
        self.Size = aSize;
    }
    return self;
}

@end

@implementation BeadMaterial (Serialization)

- (size_t) GetSerializedLength
{
    return sizeof(uint8_t) * 4 + sizeof(NSDecimal);
}

- (void) SerializeToBuffer: (void*) buffer
{
    uint8_t *buf = (uint8_t*) buffer;
    
    const CGFloat *components = CGColorGetComponents(color.CGColor);
    uint8_t red = lroundf(components[0] * 255.0);
    uint8_t green = lround(components[1] * 255.0);
    uint8_t blue = lround(components[2] * 255.0);
    uint8_t alpha = lround(components[3] * 255.0);
    
    *buf = red;
    *(buf + 1) = green;
    *(buf + 2) = blue;
    *(buf + 3) = alpha;
    
    NSDecimal *dBuf = (NSDecimal *) (buf + 4);
    *dBuf = size.decimalValue;
}

+ (id) DeserializeFromBuffer: (void*) buffer
{
    uint8_t *buf = (uint8_t*) buffer;
    
    uint8_t iRed = *buf;
    uint8_t iGreen = *(buf + 1);
    uint8_t iBlue = *(buf + 2);
    uint8_t iAlpha = *(buf + 3);
    
    
    CGFloat red = (CGFloat) iRed / 255.0;
    CGFloat green = (CGFloat) iGreen / 255.0;
    CGFloat blue = (CGFloat) iBlue / 255.0;
    CGFloat alpha = (CGFloat) iAlpha / 255.0;
    UIColor* color = [[UIColor alloc] initWithRed: red green: green blue: blue alpha: alpha];
    
    const NSDecimal *sBuf = (const NSDecimal *) (buf + 4);
    NSDecimalNumber *aSize = [[NSDecimalNumber alloc] initWithDecimal: *sBuf];
    
    BeadMaterial* material = [[BeadMaterial alloc] initWithColor: color AndSize: aSize];
    return material;
}

@end

