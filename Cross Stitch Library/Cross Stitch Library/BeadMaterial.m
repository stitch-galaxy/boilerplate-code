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
    if (![size isEqual: aBeadMaterial.Size ])
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

- (id) initWithColor: (UIColor*) aColor AndSize: (NSDecimalNumber*) aSize
{
    if (self = [super init])
    {
        self.Color = aColor;
        self.Size = aSize;
    }
    return self;
}

- (size_t) GetSerializedLength
{
    size_t aSize = sizeof(uint8_t) * 4;
    
    NSString* sSize;
    
    NSUInteger bytesLength = [sSize lengthOfBytesUsingEncoding: NSASCIIStringEncoding];
    bytesLength += 1;
    
    aSize += bytesLength;
    
    return aSize;
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
    
    char *sBuf = (char *) (buf + 4);
    
    NSString* sSize;
    
    NSUInteger bytesLength = [sSize lengthOfBytesUsingEncoding: NSASCIIStringEncoding];
    
    [sSize getCString: sBuf maxLength: bytesLength + 1 encoding: NSASCIIStringEncoding];
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
    
    const char *sBuf = (const char *) (buf + 4);
    
    NSString* sSize = [[NSString alloc] initWithCString: sBuf encoding: NSASCIIStringEncoding];
    
    NSNumberFormatter *formatter = [[NSNumberFormatter alloc] init];
    [formatter setFormatterBehavior:NSNumberFormatterBehavior10_4];
    [formatter setGeneratesDecimalNumbers:YES];
    NSDecimalNumber *aSize = [formatter numberFromString: sSize];
    
    BeadMaterial* material = [[BeadMaterial alloc] initWithColor: color AndSize: aSize];
    return material;
}


@end

